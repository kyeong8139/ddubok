package com.ddubok.common.openai.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.io.File;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Map;
import com.ddubok.common.openai.repository.ModelIdRepository;

@Service
@RequiredArgsConstructor
public class FineTuneServiceImpl implements FineTuneService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.finetuning}")
    private String baseUrl;

    private final ModelIdRepository modelIdRepository;
    private WebClient webClient;

    @PostConstruct
    private void initWebClient() {
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .build();
    }

    public void startFineTuning(File jsonlFile) {
        try {
            byte[] fileData = Files.readAllBytes(jsonlFile.toPath());
            ByteArrayResource fileResource = new ByteArrayResource(fileData) {
                @Override
                public String getFilename() {
                    return "file.jsonl";
                }
            };

            MultiValueMap<String, Object> fileBody = new LinkedMultiValueMap<>();
            fileBody.add("file", fileResource);
            fileBody.add("purpose", "fine-tune");

            webClient.post()
                .uri("/files")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(fileBody)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(response -> {
                    String fileId = (String) response.get("id");
                    Map<String, Object> fineTuneRequest = Map.of(
                        "training_file", fileId,
                        "model", "gpt-4o-mini-2024-07-18",
                        "suffix", "ddubok-model"
                    );

                    return webClient.post()
                        .uri("/fine_tuning/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(fineTuneRequest)
                        .retrieve()
                        .bodyToMono(Map.class);
                })
                .flatMap(fineTuneResponse -> {
                    String jobId = (String) fineTuneResponse.get("id");
                    modelIdRepository.updateJobId(jobId);
                    return pollJobStatus(jobId);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(modelId -> {
                    modelIdRepository.updateModelId(modelId);
                }, error -> {
                    error.printStackTrace();
                });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jsonlFile.exists()) {
                jsonlFile.delete();
            }
        }
    }

    private Mono<String> pollJobStatus(String jobId) {
        return webClient.get()
            .uri("/fine_tuning/jobs/" + jobId)
            .retrieve()
            .bodyToMono(Map.class)
            .flatMap(jobResponse -> {
                String status = (String) jobResponse.get("status");

                if ("succeeded".equalsIgnoreCase(status)) {
                    String modelId = (String) jobResponse.get("fine_tuned_model");
                    return Mono.just(modelId);
                } else if ("failed".equalsIgnoreCase(status)) {
                    return Mono.error(new RuntimeException("Fine-tuning job failed."));
                }

                return Mono.delay(Duration.ofSeconds(60))
                    .flatMap(delay -> pollJobStatus(jobId));
            });
    }
}