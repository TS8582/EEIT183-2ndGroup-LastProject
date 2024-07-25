package com.playcentric.service.game;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
@PropertySource("ecPay.properties")
public class GameOrderService {
	
	@Autowired
	private GameOrderRepository oRepo;
	@Autowired
	private GameOrderDetailsRepository odRepo;
	
	@Value("${ecpay.merchant_trade_date_format}")
    private String merchantTradeDateFormat;
    
    @Value("${ecpay.gameorder_return_url}")
	private String returnUrl;
    
    @Value("${ecpay.gameorder_client_back_url}")
    private String backUrl;
    
    @Value("${ecpay.gameorder_trade_desc}")
    private String tradeDesc;
	
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
	
	private final AllInOne allInOne = new AllInOne("");
	
	public GameOrder save(GameOrder gameOrder) {
		return oRepo.save(gameOrder);
	}
	
	public GameOrderDetails saveDetails(GameOrderDetails orderDetails) {
		return odRepo.save(orderDetails);
	}
	
	public GameOrder findById(Integer gameOrderId) {
		return oRepo.findById(gameOrderId).get();
	}
	
	public String startEcpayOrder(LoginMemDto loginMember) {
		AioCheckOutALL obj = new AioCheckOutALL();
        SimpleDateFormat sdf = new SimpleDateFormat(merchantTradeDateFormat);
        String currentDate = sdf.format(new Date());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(merchantTradeDateFormat);
        String tradeNo = "PLCTCGO";
		LocalDateTime now = LocalDateTime.now();
		tradeNo = tradeNo + now.getYear() + now.getMonthValue() + now.getDayOfMonth();
		Random random = new Random();
		int ran = 100 + random.nextInt(900);
		tradeNo = tradeNo + "T" + ran;
		int total = 0;
		String itemname = "";
		List<GameCarts> carts = gcService.findByMemId(loginMember.getMemId());
		for (GameCarts gameCarts : carts) {
			Game game = gameCarts.getGame();
			gService.setRateAndDiscountPrice(game);
			total += game.getPrice();
			itemname += game.getGameName() + 
					" NT$" + game.getDiscountedPrice()+
					"#";
		}
		itemname.substring(0,itemname.length() - 1);
		
        obj.setMerchantTradeNo(tradeNo);
        obj.setMerchantTradeDate(currentDate);
        obj.setTotalAmount(String.valueOf(total));
        obj.setTradeDesc(tradeDesc);
        obj.setItemName(itemname);
        obj.setReturnURL(returnUrl);
        obj.setClientBackURL(backUrl);
        obj.setNeedExtraPaidInfo("N");

        String form = allInOne.aioCheckOut(obj, null);

        return form;
	}
	
	public void createOrder(LoginMemDto loginMember,String tradeNo) {
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
			ownGameLib.setBuyAt(LocalDateTime.now());
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
