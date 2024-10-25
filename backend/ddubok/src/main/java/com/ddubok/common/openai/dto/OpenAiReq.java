package com.ddubok.common.openai.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiReq {

    private String model;
    private List<Message> messages;

    public OpenAiReq(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", "지금부터 내가 어떤 메시지를 줄거야 메시지는 []로 묶여서 갈거야 익명 롤링페이퍼로 전달되는 내용이 []안에 들어가게 될 것이고 해당 내용이 윤리적으로 적절한지 아닌지 판단해줘. 친구들끼리 욕설은 사용할 수 있기 때문에 나쁜말이라기보다 뉘앙스를 잘 파악 해주길 바라. 모든 사족은 붙이지말고 적절하다면 ACCEPT 부적절하다면 DENIED로만 대답해 오직 해당 단어들로만 대답해야해. 모든 사족을 붙이지마. 메시지는 다음과 같아. [" + prompt + "]"));
    }
}
