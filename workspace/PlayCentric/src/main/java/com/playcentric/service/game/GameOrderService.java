package com.playcentric.service.game;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.secondary.GameCarts;
import com.playcentric.model.game.secondary.OwnGameLib;
import com.playcentric.model.game.transaction.GameOrder;
import com.playcentric.model.game.transaction.GameOrderDetails;
import com.playcentric.model.game.transaction.GameOrderDetailsRepository;
import com.playcentric.model.game.transaction.GameOrderRepository;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.service.PaymentService;
import com.playcentric.service.member.MemberService;

@Service
public class GameOrderService {
	
	@Autowired
	private GameOrderRepository oRepo;
	@Autowired
	private GameOrderDetailsRepository odRepo;
	
	
	@Autowired
	private GameCartService gcService;
	@Autowired
	private GameService gService;
	@Autowired
	private PaymentService pService;
	@Autowired
	private MemberService mService;
	@Autowired
	private OwnGameLibService oglService;
	
	
	public GameOrder save(GameOrder gameOrder) {
		return oRepo.save(gameOrder);
	}
	
	public GameOrderDetails saveDetails(GameOrderDetails orderDetails) {
		return odRepo.save(orderDetails);
	}
	
	public GameOrder findById(Integer gameOrderId) {
		return oRepo.findById(gameOrderId).get();
	}
	
	public void createOrder(LoginMemDto loginMember) {
		List<GameCarts> carts = gcService.findByMemId(loginMember.getMemId());
		Member member = mService.findById(loginMember.getMemId());
		Integer total = 0;
//		設定訂單屬性
		GameOrder gameOrder = new GameOrder();
		gameOrder.setStatus(0);
		gameOrder.setPaymentId(1);
		gameOrder.setPayment(pService.findById(1));
		gameOrder.setMemId(loginMember.getMemId());
		gameOrder.setMember(member);
		gameOrder.setTotal(total);
		gameOrder.setCreateAt(LocalDateTime.now());
		GameOrder myorder = save(gameOrder);
		String tradeNo = "PLCTCGO";
		LocalDateTime now = LocalDateTime.now();
		tradeNo = tradeNo + now.getYear() + now.getMonthValue() + now.getDayOfMonth();
		tradeNo = tradeNo + "T" + myorder.getGameOrderId();
		myorder.setTradeNo(tradeNo);
		
		List<GameOrderDetails> detailses = new ArrayList<>();
		for (GameCarts gameCarts : carts) {
			GameOrderDetails gameOrderDetails = new GameOrderDetails();
			Game game = gService.findById(gameCarts.getGameId());
			gService.setRateAndDiscountPrice(game);
			gameOrderDetails.setAmount(1);
			BigDecimal rate = BigDecimal.ONE;
			if (game.getRate() != null) {
				BigDecimal rateOrigin = BigDecimal.valueOf(game.getRate());
				BigDecimal hundred = BigDecimal.valueOf(100);
				rate = rateOrigin.divide(hundred,2,BigDecimal.ROUND_HALF_UP);
			}
			gameOrderDetails.setDiscountRate(rate);
			total += game.getDiscountedPrice();
			gameOrderDetails.setGame(game);
			gameOrderDetails.setGameId(game.getGameId());
			gameOrderDetails.setUnitPrice(game.getPrice());
			gameOrderDetails.setGameOrderId(myorder.getGameOrderId());
			gameOrderDetails.setGameOrder(myorder);
			saveDetails(gameOrderDetails);
			gcService.remove(gameCarts.getGameId(), gameCarts.getMemId());
			OwnGameLib ownGameLib = new OwnGameLib();
			ownGameLib.setBuyAt(now);
			ownGameLib.setGame(game);
			ownGameLib.setGameId(game.getGameId());
			ownGameLib.setMember(member);
			ownGameLib.setMemId(member.getMemId());
			oglService.save(ownGameLib);
		}
		myorder.setTotal(total);
		save(myorder);
		member.setPoints(member.getPoints() - total);
		loginMember.setPoints(member.getPoints() - total);
		mService.save(member);
	}
	
}
