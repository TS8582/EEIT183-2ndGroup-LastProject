package com.playcentric.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.playcentric.filter.AuthenticationFilter;
import com.playcentric.service.member.MemberService;

@Configuration
public class AppConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    FilterRegistrationBean<AuthenticationFilter> authenticationFilter(MemberService memberService) {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter(memberService));
        registrationBean.addUrlPatterns("/member/back/*"); // 設定 Filter 的 URL pattern
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 設定 Filter 的執行順序
        return registrationBean;
    }
}
