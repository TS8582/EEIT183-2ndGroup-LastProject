package com.playcentric.controller.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.secondary.GameCarts;
import com.playcentric.model.game.transaction.GameOrder;
import com.playcentric.model.game.transaction.GameOrderDetails;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.PaymentService;
import com.playcentric.service.game.GameCartService;
import com.playcentric.service.game.GameOrderService;
import com.playcentric.service.game.GameService;
import com.playcentric.service.member.MemberService;

@Controller
@SessionAttributes("loginMember")
public class GameOrderController {
	
	@ModelAttribute("loginMember")
	public LoginMemDto getLoginMember() {
		return null;
	}
	@Autowired
	private GameService gService;
	@Autowired
	private GameCartService gcService;
	@Autowired
	private GameOrderService goService;
	@Autowired
	private PaymentService pService;
	@Autowired
	private MemberService mService;
	
//	public String gameOrderStart(@ModelAttribute("loginMember") LoginMemDto loginMember
//			,@RequestParam Integer paymentId) {
//		List<GameCarts> carts = gcService.findByMemId(loginMember.getMemId());
//		GameOrder gameOrder = new GameOrder();
//		gameOrder.setStatus(0);
//		gameOrder.setPaymentId(paymentId);
//		gameOrder.setPayment(pService.findById(paymentId));
//		gameOrder.setMemId(loginMember.getMemId());
//		gameOrder.setMember(mService.findById(loginMember.getMemId()));
//		String tradeNo = "PLCTCGO";
//		LocalDateTime now = LocalDateTime.now();
//		tradeNo = tradeNo + now.getYear() + now.getMonthValue() + now.getDayOfMonth();
//		
//		
//		List<GameOrderDetails> detailses = new ArrayList<>();
//		for (GameCarts gameCarts : carts) {
//			GameOrderDetails gameOrderDetails = new GameOrderDetails();
//			Game game = gService.findById(gameCarts.getGameId());
//			gService.setRateAndDiscountPrice(game);
//			
//		}
//	}
	
}
