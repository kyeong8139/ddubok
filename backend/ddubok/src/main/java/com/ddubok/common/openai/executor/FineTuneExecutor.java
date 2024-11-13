package com.ddubok.common.openai.executor;

import com.ddubok.api.card.entity.Card;
import com.ddubok.api.card.repository.CardRepository;
import com.ddubok.api.report.entity.Report;
import com.ddubok.api.report.entity.State;
import com.ddubok.common.openai.service.FineTuneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

        List<ObjectNode> acceptSamples = new ArrayList<>();
        List<ObjectNode> deniedSamples = new ArrayList<>();

        for (Object[] row : cardReportData) {
            Card card = (Card) row[0];
            Report report = (Report) row[1];

            String content = card.getContent();
            boolean isDenied = report != null && report.getState().equals(State.ACCEPT);

            ObjectNode chatFormat = objectMapper.createObjectNode();
            ArrayNode messages = objectMapper.createArrayNode();

            ObjectNode userMessage = objectMapper.createObjectNode();
            userMessage.put("role", "user");
            userMessage.put("content", String.format("이 메시지는 적절한가요? 메시지: '%s'", content));

            ObjectNode assistantMessage = objectMapper.createObjectNode();
            assistantMessage.put("role", "assistant");
            assistantMessage.put("content", isDenied ? "DENIED" : "ACCEPT");

            messages.add(userMessage);
            messages.add(assistantMessage);
            chatFormat.set("messages", messages);

            if (isDenied) {
                deniedSamples.add(chatFormat);
            } else {
                acceptSamples.add(chatFormat);
            }
        }

        int maxSamples = Math.max(acceptSamples.size(), deniedSamples.size());
        acceptSamples = oversample(acceptSamples, maxSamples);
        deniedSamples = oversample(deniedSamples, maxSamples);

        try (FileWriter writer = new FileWriter(tempFile)) {
            for (ObjectNode sample : acceptSamples) {
                writer.write(sample.toString());
                writer.write("\n");
            }
            for (ObjectNode sample : deniedSamples) {
                writer.write(sample.toString());
                writer.write("\n");
            }
        }

        return tempFile;
    }

    private static List<ObjectNode> oversample(List<ObjectNode> samples, int targetSize) {
        List<ObjectNode> oversampled = new ArrayList<>(samples);
        while (oversampled.size() < targetSize) {
            oversampled.addAll(samples);
        }
        return oversampled.subList(0, targetSize);
    }
}