package com.playcentric.controller.playfellow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.playcentric.model.member.Member;
import com.playcentric.model.playfellow.PfOrder;
import com.playcentric.service.member.MemberService;
import com.playcentric.service.playfellow.PfOrderService;


@Controller
public class pfOrderMsg {

	@Autowired
	PfOrderService pfOrderService;
	@Autowired
	MemberService memberService;

	@GetMapping("/playFellow/pfOrderMsg/{memId}")
	public String addImage(@PathVariable("memId") Integer memId, Model model) {
	    System.out.println( memId); 

		Member member = memberService.findById(memId);
		if(member!=null) {
			List<PfOrder> pfOrder = pfOrderService.findByMember(member);
			model.addAttribute("pfOrder", pfOrder);
			
		}
		
		return "playFellow/pfMemberOrderMsg";
	}
}
