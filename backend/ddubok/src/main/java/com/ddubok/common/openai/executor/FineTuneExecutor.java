package com.ddubok.common.openai.executor;

import com.ddubok.api.card.entity.Card;
import com.ddubok.api.card.repository.CardRepository;
import com.ddubok.api.report.entity.Report;
import com.ddubok.api.report.entity.State;
import com.ddubok.common.openai.service.FineTuneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FineTuneExecutor {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private FineTuneService fineTuneService;

    public void executeFineTuning() throws IOException {
        List<Object[]> cardReportData = cardRepository.findAllCardsWithReports();
        File jsonlFile = createTrainingDataFile(cardReportData);
        fineTuneService.startFineTuning(jsonlFile);
    }

    private File createTrainingDataFile(List<Object[]> cardReportData) throws IOException {
        File tempFile = Files.createTempFile("training_data", ".jsonl").toFile();
        ObjectMapper objectMapper = new ObjectMapper();

        List<ObjectNode> acceptSamples = cardReportData.stream()
            .map(row -> createChatFormat((Card) row[0], (Report) row[1], objectMapper))
            .filter(sample -> !sample.isDenied())
            .map(Sample::getData)
            .collect(Collectors.toList());

        List<ObjectNode> deniedSamples = cardReportData.stream()
            .map(row -> createChatFormat((Card) row[0], (Report) row[1], objectMapper))
            .filter(Sample::isDenied)
            .map(Sample::getData)
            .collect(Collectors.toList());

        int maxSamples = Math.max(acceptSamples.size(), deniedSamples.size());
        acceptSamples = oversample(acceptSamples, maxSamples);
        deniedSamples = oversample(deniedSamples, maxSamples);

        try (FileWriter writer = new FileWriter(tempFile)) {
            Stream.concat(acceptSamples.stream(), deniedSamples.stream())
                .forEach(sample -> writeSampleToFile(writer, sample));
        }

        return tempFile;
    }

    private static List<ObjectNode> oversample(List<ObjectNode> samples, int targetSize) {
        return Stream.generate(() -> samples)
            .flatMap(List::stream)
            .limit(targetSize)
            .collect(Collectors.toList());
    }

    private static Sample createChatFormat(Card card, Report report, ObjectMapper objectMapper) {
        String content = card.getContent();
        boolean isDenied = report != null && report.getState().equals(State.ACCEPT);

        ObjectNode chatFormat = objectMapper.createObjectNode();
        ArrayNode messages = objectMapper.createArrayNode();

        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", String.format("이 메시지는 적정한가요? 메시지: '%s'", content));

        ObjectNode assistantMessage = objectMapper.createObjectNode();
        assistantMessage.put("role", "assistant");
        assistantMessage.put("content", isDenied ? "DENIED" : "ACCEPT");

        messages.add(userMessage);
        messages.add(assistantMessage);
        chatFormat.set("messages", messages);

        return new Sample(chatFormat, isDenied);
    }

    private static void writeSampleToFile(FileWriter writer, ObjectNode sample) {
        try {
            writer.write(sample.toString());
            writer.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class Sample {
        private final ObjectNode data;
        private final boolean denied;

        public Sample(ObjectNode data, boolean denied) {
            this.data = data;
            this.denied = denied;
        }

        public ObjectNode getData() {
            return data;
        }

        public boolean isDenied() {
            return denied;
        }
    }
}
