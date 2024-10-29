package com.ddubok.common.openai.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiRes {

    private List<Choice> choices;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {

        private int index;
        private Message message;

    }
}
