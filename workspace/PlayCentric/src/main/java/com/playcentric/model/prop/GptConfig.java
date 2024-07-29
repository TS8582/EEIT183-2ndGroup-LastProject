package com.playcentric.model.prop;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:gpt.properties")
public class GptConfig {
}

