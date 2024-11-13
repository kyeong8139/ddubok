package com.ddubok.common.openai.service;

import java.io.File;

public interface FineTuneService {

    /**
     * 파일을 바탕으로 gpt에 finetuning요청을 보냄
     *
     * @param jsonlFile
     */
    void startFineTuning(File jsonlFile);
}
