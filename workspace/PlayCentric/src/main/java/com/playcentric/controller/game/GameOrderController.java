package com.playcentric.controller.game;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.secondary.GameCarts;
import com.playcentric.model.game.transaction.GameOrder;
import com.playcentric.model.game.transaction.GameOrderDetails;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.service.PaymentService;
import com.playcentric.service.game.GameCartService;
import com.playcentric.service.game.GameOrderService;
import com.playcentric.service.game.GameService;
import com.playcentric.service.game.OwnGameLibService;
import com.playcentric.service.member.MemberService;

@Controller
@RequestMapping("/gameorder")
@SessionAttributes("loginMember")
@PropertySource("ecPay.properties")
public class GameOrderController {
	
	@Value("${ecpay.merchant_trade_date_format}")
    private String merchantTradeDateFormat;

    @Value("${ecpay.hash_key}")
    private String hashKey;

    @Value("${ecpay.hash_iv}")
    private String hashIV;
	
	@ModelAttribute("loginMember")
	public LoginMemDto getLoginMember() {
		return null;
	}
	@Autowired
	private GameOrderService goService;
	
	
	@GetMapping("/return")
	public String gameorder(@RequestParam Integer paymentId) {
		if (paymentId == 1) {
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
			goService.createPcOrder(loginMember);
		}
		
		return "game/gameorder-ok";
	}
	
	
//	@PostMapping("/ecpay")
//	public String ecpay(@ModelAttribute("loginMember") LoginMemDto loginMember) {
//		List<GameCarts> carts = gcService.findByMemId(loginMember.getMemId());
//		設定訂單屬性
//		GameOrder gameOrder = new GameOrder();
//		Member member = mService.findById(loginMember.getMemId());
//		gameOrder.setStatus(0);
//		gameOrder.setPaymentId(2);
//		gameOrder.setPayment(pService.findById(2));
//		gameOrder.setMemId(loginMember.getMemId());
//		gameOrder.setMember(member);
//		GameOrder myorder = goService.save(gameOrder);
//		String tradeNo = "PLCTCGO";
//		LocalDateTime now = LocalDateTime.now();
//		tradeNo = tradeNo + now.getYear() + now.getMonthValue() + now.getDayOfMonth();
//		tradeNo = tradeNo + "T" + myorder.getGameOrderId();
//		myorder.setTradeNo(tradeNo);
//		Integer total = 0;
//		
//		List<GameOrderDetails> detailses = new ArrayList<>();
//		for (GameCarts gameCarts : carts) {
//			GameOrderDetails gameOrderDetails = new GameOrderDetails();
//			Game game = gService.findById(gameCarts.getGameId());
//			gService.setRateAndDiscountPrice(game);
//			gameOrderDetails.setAmount(1);
//			BigDecimal rateOrigin = BigDecimal.valueOf(game.getRate());
//			BigDecimal hundred = BigDecimal.valueOf(100);
//			BigDecimal rate = rateOrigin.divide(hundred,2,BigDecimal.ROUND_HALF_UP);
//			gameOrderDetails.setDiscountRate(rate);
//			gameOrderDetails.setGame(game);
//			gameOrderDetails.setUnitPrice(game.getPrice());
//			gameOrderDetails.setGameOrderId(myorder.getGameOrderId());
//		}
//		member.setPoints(member.getPoints() - total);
//		loginMember.setPoints(member.getPoints() - total);
//		mService.save(member);
//	}
	
}
