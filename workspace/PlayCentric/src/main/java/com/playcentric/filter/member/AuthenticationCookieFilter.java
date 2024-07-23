package com.playcentric.filter.member;

import java.io.IOException;

import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.member.MemberService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticationCookieFilter implements Filter {

    private MemberService memberService;

    public AuthenticationCookieFilter(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 獲取 HttpSession
        HttpSession session = httpRequest.getSession(); // false 表示如果沒有 Session 不創建新 Session
        Cookie[] cookies = httpRequest.getCookies();
        String loginToken = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("loginToken".equals(cookie.getName())) {
                    loginToken = cookie.getValue();
                }
            }
        }
        LoginMemDto loginMember = null;
        if (!loginToken.isEmpty()) {
            Cookie cookie = null;
            if ((loginMember = memberService.getLoginMember(loginToken)) != null) {
                session.setAttribute("loginMember", loginMember);
                cookie = new Cookie("loginToken", loginToken);
                cookie.setMaxAge(7 * 24 * 60 * 60);
            } else {
                cookie = new Cookie("loginToken", null);
                cookie.setMaxAge(0);
            }
            cookie.setPath("/PlayCentric");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            httpResponse.addCookie(cookie);
        } else if (session != null) {
            if ((loginMember = (LoginMemDto) session.getAttribute("loginMember")) != null
            && (loginMember = memberService.checkLoginMember(loginMember)) != null) {
                session.setAttribute("loginMember", loginMember);
            }else{
                session.setAttribute("loginMember", null);
            }
        }

        chain.doFilter(request, response);
    }
}
