package com.playcentric.controller.playfellow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.model.playfellow.PfOrder;
import com.playcentric.service.member.MemberService;
import com.playcentric.service.playfellow.PfOrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class pfOrderMsg {

	@Autowired
	PfOrderService pfOrderService;
	@Autowired
	MemberService memberService;

	@PostMapping("/playFellow/pfOrderMsg")
	public String addImage(HttpSession session, Model model) {
        LoginMemDto loginMember = (LoginMemDto) session.getAttribute("loginMember");
        
        Integer memId = loginMember.getMemId();
		System.out.println(memId);

		Member member = memberService.findById(memId);
		
		if (member != null) {
			List<PfOrder> pfOrder = pfOrderService.findByMember(member);
			model.addAttribute("pfOrder", pfOrder);

		}

		return "playFellow/pfMemberOrderMsg";
	}
}
