package com.playcentric.controller.game;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.ImageLib;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.primary.GameDiscount;
import com.playcentric.model.game.primary.GameDiscountSet;
import com.playcentric.model.game.primary.GameTypeLib;
import com.playcentric.model.game.secondary.GameReviews;
import com.playcentric.model.game.secondary.OwnGameLib;
import com.playcentric.model.game.transaction.GameOrder;
import com.playcentric.model.game.transaction.GameOrderDetails;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.ImageLibService;
import com.playcentric.service.game.GameCartService;
import com.playcentric.service.game.GameDiscountSetService;
import com.playcentric.service.game.GameOrderService;
import com.playcentric.service.game.GameReviewsService;
import com.playcentric.service.game.GameService;
import com.playcentric.service.game.GameTypeService;
import com.playcentric.service.game.OwnGameLibService;

@Controller
@SessionAttributes("loginMember")
public class GameController {
	
	@ModelAttribute("loginMember")
	public LoginMemDto createLoginMemDto() {
		return null;
	}

	@Autowired
	private GameService gService;
	@Autowired
	private GameTypeService gtService;
	@Autowired
	private GameDiscountSetService gdsService;
	@Autowired
	private ImageLibService iService;
	@Autowired
	private GameCartService gcService;
	@Autowired
	private OwnGameLibService oglService;
	@Autowired
	private GameOrderService goService;
	@Autowired
	private GameReviewsService grService;

	// 遊戲管理後台
	@GetMapping("/back/game")
	public String backGame(Model model) {
		List<Game> all = gService.findAll();
		model.addAttribute("allGame",all);
		return "game/back-game";
	}

	// 輸入新增遊戲資料
	@GetMapping("/game/getInsertGame")
	public String getInsertGame(Model model) {
		List<GameTypeLib> allType = gtService.findAll();
		List<GameDiscountSet> allDiscount = gdsService.findBetweenStartAndEnd(LocalDateTime.now());
		model.addAttribute("allType", allType);
		model.addAttribute("allDiscount", allDiscount);
		return "game/insert-game";
	}
	
	//輸入修改遊戲資料
	@GetMapping("/game/getUpdateGame")
	public String getUpdateGame(@RequestParam Integer gameId,Model model) {
		Game game = gService.findById(gameId);
		model.addAttribute("game",game);
		List<Integer> typeIds = new ArrayList<>();
		for (GameTypeLib type : game.getGameTypeLibs()) {
			typeIds.add(type.getGameTypeId());
		}
		model.addAttribute("typeIds",typeIds);
		List<GameTypeLib> allType = gtService.findAll();
		List<GameDiscountSet> allDiscount = gdsService.findCanChoose();
		model.addAttribute("allType", allType);
		model.addAttribute("allDiscount", allDiscount);
		
		GameDiscount nowDiscount1 = gService.findNowDiscount(gameId);
		if (nowDiscount1 != null) {
			GameDiscountSet nowDiscount = gdsService.findById(nowDiscount1.getGameDiscountId());
			model.addAttribute("nowDiscount",nowDiscount);
			model.addAttribute("nowDiscount1",nowDiscount1);
			}
		return "game/update-game";
	}

	// 進行新增遊戲
	@PostMapping("/game/insertGame")
	public String insertGame(@ModelAttribute Game game, @RequestParam List<Integer> typeId,
			@RequestParam("photos") MultipartFile[] photos, @RequestParam(defaultValue = "0") BigDecimal discountRate,
			@RequestParam(defaultValue = "0") Integer discountId,
			@RequestParam MultipartFile gameFiles) throws IOException {
		// 設定遊戲分類
		List<GameTypeLib> types = new ArrayList<>();
		for (Integer id : typeId) {
			GameTypeLib type = gtService.findById(id);
			types.add(type);
		}
		game.setGameTypeLibs(types);
		//多對多關係需要創立
		List<Game> games = new ArrayList<>();
		//資料庫建立遊戲
		Game newGame = gService.save(game);
		games.add(newGame);
		// 設定遊戲圖片
		List<ImageLib> imgs = new ArrayList<>();
		if (!photos[0].isEmpty()) {
			for (MultipartFile file : photos) {
				System.out.println("到底會有幾個檔案");
				ImageLib imageLib = new ImageLib();
				imageLib.setImageFile(file.getBytes());
				ImageLib saveImage = iService.saveImage(imageLib);
				imageLib.setGames(games);
				imgs.add(saveImage);
			}
			newGame.setImageLibs(imgs);
		}
		if (discountId != 0 && discountRate.compareTo(BigDecimal.ZERO) != 0) {
			//取得優惠活動
			GameDiscountSet discountSet = gdsService.findById(discountId);
			//新增遊戲優惠
			GameDiscount gameDiscount = new GameDiscount();
			gameDiscount.setDiscountRate(discountRate);
			gameDiscount.setGameId(newGame.getGameId());
			gameDiscount.setGameDiscountId(discountId);
			List<GameDiscount> gameDiscounts = new ArrayList<>();
			gameDiscounts.add(gameDiscount);
			newGame.setGameDiscounts(gameDiscounts);
			discountSet.setGameDiscounts(gameDiscounts);
		}
		//重新存入帶有圖片與優惠的遊戲
		newGame.setGameFile(gameFiles.getBytes());
		gService.save(newGame);
		return "redirect:/back/game";
	}
	
		// 進行修改遊戲
		@PostMapping("/game/updateGame")
		public String updateGame(@ModelAttribute Game game, @RequestParam List<Integer> typeId,
				@RequestParam("photos") MultipartFile[] photos, @RequestParam(defaultValue = "0") BigDecimal discountRate,
				@RequestParam(defaultValue = "0") Integer discountId,@RequestParam List<Integer> photoId) throws IOException {
			Game myGame = gService.findById(game.getGameId());
			myGame.setGameName(game.getGameName());
			myGame.setDescription(game.getDescription());
			myGame.setPrice(game.getPrice());
			myGame.setDeveloper(game.getDeveloper());
			myGame.setPublisher(game.getPublisher());
			// 設定遊戲分類
			List<GameTypeLib> types = new ArrayList<>();
			for (Integer id : typeId) {
				GameTypeLib type = gtService.findById(id);
				types.add(type);
			}
			myGame.setGameTypeLibs(types);
			//多對多關係需要創立
			List<Game> games = new ArrayList<>();
			games.add(myGame);
			// 設定遊戲圖片
			myGame.setImageLibs(null);
			gService.save(myGame);
			List<ImageLib> imgs = new ArrayList<>();
			
			for (Integer pId : photoId) {
				List<ImageLib> imageLibs = myGame.getImageLibs();
				if (pId != 0) {
					
					ImageLib img = iService.findImageById(pId);
					imgs.add(img);
				}
			}
			
			if (!photos[0].isEmpty()) {
				for (MultipartFile file : photos) {
					ImageLib imageLib = new ImageLib();
					imageLib.setImageFile(file.getBytes());
					ImageLib saveImage = iService.saveImage(imageLib);
					imageLib.setGames(games);
					imgs.add(saveImage);
				}
			}
			myGame.setImageLibs(imgs);
			if (discountId != 0 && discountRate.compareTo(BigDecimal.ZERO) != 0) {
				GameDiscount nowDiscount = gService.findNowDiscount(myGame.getGameId());
				GameDiscountSet discountSet = gdsService.findById(discountId);
				if (nowDiscount != null) {
					if (nowDiscount.getGameDiscountId() != discountId) {
						//取得優惠活動
						//新增遊戲優惠
						GameDiscount gameDiscount = new GameDiscount();
						gameDiscount.setDiscountRate(discountRate);
						gameDiscount.setGameId(myGame.getGameId());
						gameDiscount.setGameDiscountId(discountId);
						List<GameDiscount> gameDiscounts = new ArrayList<>();
						gameDiscounts.add(gameDiscount);
						myGame.setGameDiscounts(gameDiscounts);
						discountSet.setGameDiscounts(gameDiscounts);
						Double oldRate = Double.parseDouble(nowDiscount.getDiscountRate().toString());
						int rate = (int) (oldRate * 100);
						myGame.setRate(rate);
					}
				}
				else {
					GameDiscount gameDiscount = new GameDiscount();
					gameDiscount.setDiscountRate(discountRate);
					gameDiscount.setGameId(myGame.getGameId());
					gameDiscount.setGameDiscountId(discountId);
					List<GameDiscount> gameDiscounts = new ArrayList<>();
					gameDiscounts.add(gameDiscount);
					myGame.setGameDiscounts(gameDiscounts);
					discountSet.setGameDiscounts(gameDiscounts);
				}
			}
			//重新存入帶有圖片與優惠的遊戲
			gService.save(myGame);
			return "redirect:/back/game";
		}
	
	
	// 輸入優惠活動資料
	@GetMapping("/game/getSetDiscount")
	public String getSetDiscount() {
		return "game/set-discount";
	}
	
	// 新增優惠活動
	@PostMapping("/game/setDiscount")
	public String setDiscount(@ModelAttribute GameDiscountSet discountSet) {
		gdsService.insert(discountSet);
		return "redirect:/back/game";
	}
	
	//遊戲商店頁面
	@GetMapping("/game/gameStore")
	public String gameStore(Model model,@ModelAttribute("loginMember") LoginMemDto loginMember) {
		PageRequest pgb = PageRequest.of(0, 9);
		Page<Game> games = gService.findByIsShowOrderByReleaseAtDesc(pgb);
		List<GameTypeLib> allType = gtService.findAll();
		for (Game game : games) {
			gService.setRateAndDiscountPrice(game);
			if (loginMember != null) {
				gcService.setInCart(loginMember, game);
				oglService.setHaveGame(loginMember, game);
			}
		}
		model.addAttribute("allType",allType);
		model.addAttribute("games",games);
		return "game/game-store";
	}
	
	//單一遊戲頁面
	@GetMapping("/game/showGame")
	public String showGame(@RequestParam Integer gameId,
			Model model,@ModelAttribute("loginMember") LoginMemDto loginMember) {
		Game game = gService.findById(gameId);
		gService.setRateAndDiscountPrice(game);
		double totalReviews = (double)game.getTotalReviews();
		Integer totalScore = game.getTotalScore();
		double score = totalScore / totalReviews;
		if (loginMember != null) {
			gcService.setInCart(loginMember, game);
			oglService.setHaveGame(loginMember, game);
			List<GameReviews> memberReviews = grService.findByGameIdAndMemId(gameId, loginMember.getMemId());
			model.addAttribute("memberReviews",memberReviews);
		}
		List<GameReviews> reviews = grService.findByGameIdTop5(gameId);
		model.addAttribute("score",score);
		model.addAttribute("game",game);
		model.addAttribute("reviews",reviews);
		return "game/show-game";
	}
	
	//會員遊戲收藏庫
	@GetMapping("/personal/game/ownGame")
	public String getMethodName(
			@ModelAttribute("loginMember") LoginMemDto loginMember,
			Model model
			) {
		List<OwnGameLib> ownGames = oglService.findByMemId(loginMember.getMemId());
		model.addAttribute("ownGames",ownGames);
		return "game/owngame";
	}
	
//	會員遊戲購買紀錄
	@GetMapping("/personal/game/buyRecord")
	public String buyRecord(
			@ModelAttribute("loginMember") LoginMemDto loginMember,
			Model model
			) {
		List<GameOrder> gameOrders = goService.findByMemId(loginMember.getMemId());
		List<Game> games = new ArrayList<>();
		for (GameOrder gameOrder : gameOrders) {
			List<GameOrderDetails> orderDetails = goService.findDetailsByOrderId(gameOrder.getGameOrderId());
			for (GameOrderDetails gameOrderDetails : orderDetails) {
				Game game = gService.findById(gameOrderDetails.getGameId());
				Integer unitPrice = gameOrderDetails.getUnitPrice();
				BigDecimal discountRate = gameOrderDetails.getDiscountRate();
				Integer amount = gameOrderDetails.getAmount();
				double rate = Double.valueOf(discountRate.toString());
				Integer discountedPrice = (int) (unitPrice * amount * rate);
				game.setDiscountedPrice(discountedPrice);
				game.setBuyAt(gameOrder.getCreateAt());
				games.add(game);
			}
		}
		model.addAttribute("games",games);
		return "game/buy-record";
	}
	
	
	
}
