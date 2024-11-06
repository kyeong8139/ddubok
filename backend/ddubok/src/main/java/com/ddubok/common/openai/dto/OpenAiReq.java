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
        this.messages.add(new Message("user", "지금부터 내가 어떤 메시지를 줄거야 메시지는 []로 묶여서 갈거야 익명 롤링페이퍼로 전달되는 내용이 []안에 들어가게 될 것이고 해당 내용이 법적으로 적절한지 아닌지 판단해줘. 메시지에는 친구들 간의 일반적인 대화에서 사용할 수 있는 욕설이 포함될 수 있지만, 성희롱, 저주, 차별적 언어는 절대 포함될 수 없어. 또한, 메시지 내용이 타인을 심각하게 불쾌하게 할 수 있는 표현도 제외해야해. 적절한 경우: 욕설은 대체로 맥락에 맞고, 상대방에게 불쾌감을 주지 않거나 부정적인 영향을 미치지 않는 경우. 부적절한 경우: 성희롱적 언어, 타인을 저주하거나 심각하게 상처를 줄 수 있는 표현. 또한, 모든 표현에 대해 성의 없는 메시지라도 부적절로 판단하지 말고, 법적 문제의 가능성이 있는지에만 집중해줘. 모든 사족은 붙이지말고 적절하다면 ACCEPT 부적절하다면 DENIED로만 대답해 오직 해당 단어들로만 대답해야해. 모든 사족을 붙이지마. 메시지는 다음과 같아. [" + prompt + "]"));
    }
}
