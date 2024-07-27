package com.playcentric.model.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GptProperties {

    @Value("${openai.token}")
    private String openaiToken;

    public String getOpenaiToken() {
        return openaiToken;
    }
}
