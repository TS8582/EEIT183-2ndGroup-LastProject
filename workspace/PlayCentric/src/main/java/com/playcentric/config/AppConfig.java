package com.playcentric.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.playcentric.filter.member.AuthenticationCookieFilter;
import com.playcentric.filter.member.AuthenticationRoleFilter;
import com.playcentric.service.member.MemberService;

@Configuration
public class AppConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    FilterRegistrationBean<AuthenticationCookieFilter> authenticationTokenFilter(MemberService memberService) {
        FilterRegistrationBean<AuthenticationCookieFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationCookieFilter(memberService));
        registrationBean.addUrlPatterns("/*"); // 設定 Filter 的 URL pattern
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 設定 Filter 的執行順序
        return registrationBean;
    }

    @Bean
    FilterRegistrationBean<AuthenticationRoleFilter> authenticationRoleFilter(MemberService memberService) {
        FilterRegistrationBean<AuthenticationRoleFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationRoleFilter(memberService));
        registrationBean.addUrlPatterns("/member/back/*", "/member/personal/*"); // 設定 Filter 的 URL pattern
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1); // 設定 Filter 的執行順序
        return registrationBean;
    }
}
