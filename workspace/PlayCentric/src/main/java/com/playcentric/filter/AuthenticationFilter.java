package com.playcentric.filter;

import java.io.IOException;

import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.member.MemberService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {

    private MemberService memberService;

    public AuthenticationFilter(MemberService memberService){
        this.memberService = memberService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 獲取 HttpSession
        HttpSession session = httpRequest.getSession(false); // false 表示如果沒有 Session 不創建新 Session
        if (session!=null) {
            LoginMemDto loginMember = (LoginMemDto) session.getAttribute("loginMember");
            loginMember = memberService.checkLoginMember(loginMember);
            if (loginMember==null) {
                httpResponse.sendRedirect("/PlayCentric/member/showLoginErr/"+"notLogin");
                return;
            }
            if (loginMember.getRole() != 1) {
                httpResponse.sendRedirect("/PlayCentric/member/homeShowErr/"+"notMng");
                return;
            }
        } else{
            httpResponse.sendRedirect("/PlayCentric/member/showLoginErr/"+"notLogin");
            return;
        }

        chain.doFilter(request, response);
    }

}
