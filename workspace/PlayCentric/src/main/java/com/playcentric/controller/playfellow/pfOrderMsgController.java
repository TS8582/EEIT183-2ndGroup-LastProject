package com.playcentric.controller.playfellow;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

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

		if (member != null) {
			PlayFellowMember havePlayFellowMember = playFellowMemberService.findByMember(member);
			List<PfOrder> orders = pfOrderService.findpfMemberOfpfOrderByMember(memId);
			model.addAttribute("orders", orders);
			model.addAttribute("havePlayFellowMember", havePlayFellowMember);

			return "playFellow/pfMemberOrderMsg";
			 
		}
		return null;
	}
	
	
//	@GetMapping("/playFellow/findMemberOfpfMember")
//	public String findMemberOfpfMember(HttpSession session, Model model) {
//	    LoginMemDto loginMember = (LoginMemDto) session.getAttribute("loginMember");
//	    Integer memId = loginMember.getMemId();
//	    Member member = memberService.findById(memId);
//	    if (member != null) {
//	        PlayFellowMember havePlayFellowMember = playFellowMemberService.findByMember(member);
//	        if (havePlayFellowMember != null) {
//	            model.addAttribute("havePlayFellowMember", havePlayFellowMember);
//	        } else {
//	            model.addAttribute("havePlayFellowMember", null);
//	        }
//
//	        return "playFellow/playFellow";
//	    }
//	    return null;
//	}

	
	
	

	@ResponseBody
	@PostMapping("/playFellow/currentPaymentStatus")
	public String paymentStatusFinish(@RequestParam Integer pfOrderId, 
			@RequestParam Integer paymentStatus) {

		Optional<PfOrder> optPforder = pfOrderService.findbyId(pfOrderId);
		PfOrder pfOrder = optPforder.get();
		pfOrder.setPaymentStatus(paymentStatus);
		pfOrderService.savePfOrder(pfOrder);
		// 狀態轉3
		return "";
	}

}
