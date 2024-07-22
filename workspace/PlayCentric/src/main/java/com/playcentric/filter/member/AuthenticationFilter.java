package com.playcentric.filter.member;

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

    public AuthenticationFilter(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String err = "notLogin";

        String servletPath = httpRequest.getServletPath();

        // 獲取 HttpSession
        HttpSession session = httpRequest.getSession(); // false 表示如果沒有 Session 不創建新 Session
        if (session != null) {
            LoginMemDto loginMember = (LoginMemDto) session.getAttribute("loginMember");
            if (loginMember == null) {
                doUnauthorized(httpResponse,err);
                if (!servletPath.contains("/api/")) {
                    httpResponse.sendRedirect("/PlayCentric/member/showLoginErr/" + err);
                }
                return;
            }
            loginMember = memberService.checkLoginMember(loginMember);
            session.setAttribute("loginMember", loginMember);
            if (loginMember == null) {
                err = "loginAgain";
                doUnauthorized(httpResponse, err);
                if (!servletPath.contains("/api/")) {
                    httpResponse.sendRedirect("/PlayCentric/member/showLoginErr/" + err);
                }
                return;
            }
            if (servletPath.contains("back") && loginMember.getRole() != 1) {
                err = "notMng";
                doUnauthorized(httpResponse, err);
                if (!servletPath.contains("/api/")) {
                    httpResponse.sendRedirect("/PlayCentric/member/homeShowErr/" + err);
                }
                return;
            }
        } else {
            doUnauthorized(httpResponse, err);
            if (!servletPath.contains("/api/")) {
                httpResponse.sendRedirect("/PlayCentric/member/showLoginErr/" + err);
            }
            return;
        }

        chain.doFilter(request, response);
    }

    private void doUnauthorized(HttpServletResponse httpResponse, String err) throws IOException{
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write(err);
    }

}
