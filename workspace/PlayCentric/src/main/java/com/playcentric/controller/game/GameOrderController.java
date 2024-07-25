package com.playcentric.controller.game;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.ECPay.ECPayService;
import com.playcentric.service.game.GameOrderService;
import com.playcentric.service.member.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/gameorder")
@SessionAttributes("loginMember")
public class GameOrderController {
	
	@ModelAttribute("loginMember")
	public LoginMemDto getLoginMember() {
		return null;
	}
	@Autowired
	private GameOrderService goService;
	@Autowired
	private MemberService mService;
	@Autowired
	private ECPayService ecPayService;
	
	
	@GetMapping("/return")
	public String gameorder(
			@RequestParam Integer paymentId,
			@ModelAttribute("loginMember") LoginMemDto loginMember
			) {
		System.out.println(paymentId);
		if (loginMember == null) {
			return "redirect:/member/showLoginErr/notLogin";
		}
		else if (paymentId == 1) {
			return "redirect:/gameorder/pcwallet";
		}
		else if (paymentId == 2) {
			return "redirect:/gameorder/ecpay";
		}
		return null;
	}
	
	@GetMapping("/pcwallet")
	public String pcwallet(@ModelAttribute("loginMember") LoginMemDto loginMember) {
		
		if (loginMember != null) {
			String tradeNo = "PLCTCGO";
			LocalDateTime now = LocalDateTime.now();
			tradeNo = tradeNo + now.getYear() + now.getMonthValue() + now.getDayOfMonth();
			Random random = new Random();
			int ran = 100 + random.nextInt(900);
			tradeNo = tradeNo + "T" + ran;
			goService.createOrder(loginMember,tradeNo);
		}
		
		return "game/gameorder-ok";
	}
	
	
	@GetMapping("/ecpay")
	@ResponseBody
	public String ecpay(
			@ModelAttribute("loginMember") LoginMemDto loginMember
			) {
		return goService.startEcpayOrder(loginMember);
	}
	
	@GetMapping("/done")
	public String ecpayDine(
			HttpSession session,
			@RequestParam(name = "TradeNo", required = false) String tradeNo
			) {
		System.out.println("有進來嗎");
		LoginMemDto loginMember = (LoginMemDto) session.getAttribute("loginMember");
		goService.createOrder(loginMember,tradeNo);
		return "game/gameorder-ok";
	}
	
	@PostMapping("/ECPayReturn")
    @ResponseBody
    public String rechargeReturn(@RequestParam Map<String, String> params) {
		return "1|OK";
    }
	
}
