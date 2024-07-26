package com.playcentric.controller.playfellow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.model.playfellow.PfOrder;
import com.playcentric.model.playfellow.PlayFellowMember;
import com.playcentric.service.member.MemberService;
import com.playcentric.service.playfellow.PfOrderService;
import com.playcentric.service.playfellow.PlayFellowMemberService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class pfOrderMsgController {

	@Autowired
	PfOrderService pfOrderService;
	@Autowired
	MemberService memberService;
	@Autowired
	PlayFellowMemberService playFellowMemberService;
	
	

	@GetMapping("/playFellow/pfOrderMsg")
	public String findMemberpfOrder(HttpSession session, Model model) {
		LoginMemDto loginMember = (LoginMemDto) session.getAttribute("loginMember");

		Integer memId = loginMember.getMemId();
		System.out.println(memId);

		Member member = memberService.findById(memId);
		
		List<PfOrder> pfOrder = pfOrderService.findByMember(member);
		model.addAttribute("pfOrder", pfOrder);
		
		List<PfOrder> orders = pfOrderService.findpfMemberOfpfOrderByMember(memId);
	    model.addAttribute("orders", orders);

		return "playFellow/pfMemberOrderMsg";
	}
	
	

	
}
