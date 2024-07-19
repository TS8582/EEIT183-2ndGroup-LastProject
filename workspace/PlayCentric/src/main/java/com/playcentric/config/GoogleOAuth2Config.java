package com.playcentric.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Getter
@Configuration
@PropertySource("googleOAuth2.properties")
public class GoogleOAuth2Config {
	
	@Value("${client_id}")
	private String clientId;
	
	@Value("${client_secret}")
	private String clientSecret;
	
	@Value("${redirect_uris}")
	private String redirectUri;
	
	@Value("${server_uri}")
	private String serverUri;
	
	@Value("${controller_path}")
	private String controllerPath;

	public String getRedirectUri(){
		return serverUri+controllerPath;
	}
}
