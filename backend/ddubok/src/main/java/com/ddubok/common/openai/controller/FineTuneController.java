package com.ddubok.common.openai.controller;

import com.ddubok.common.openai.executor.FineTuneExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fine-tune")
public class FineTuneController {

    private final FineTuneExecutor fineTuneExecutor;

    /**
     * Fine-tuning 작업을 수동으로 실행하는 엔드포인트
     * @return 성공 메시지 또는 에러 메시지
     */
    @GetMapping("/execute")
    public void executeFineTuning() {
        try {
            fineTuneExecutor.executeFineTuning();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
