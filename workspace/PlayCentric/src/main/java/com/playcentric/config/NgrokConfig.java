package com.playcentric.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
// @Getter
@Configuration
@PropertySource("ngrok.properties")
public class NgrokConfig {
	
	@Value("${ngrok.url}")
	private String url;
}
