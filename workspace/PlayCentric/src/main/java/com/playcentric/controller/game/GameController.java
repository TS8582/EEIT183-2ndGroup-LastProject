package com.playcentric.controller.game;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.ImageLib;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.primary.GameDiscount;
import com.playcentric.model.game.primary.GameDiscountSet;
import com.playcentric.model.game.primary.GameTypeLib;
import com.playcentric.service.ImageLibService;
import com.playcentric.service.game.GameDiscountSetService;
import com.playcentric.service.game.GameService;
import com.playcentric.service.game.GameTypeService;

@Controller
public class GameController {

	@Autowired
	private GameService gService;
	@Autowired
	private GameTypeService gtService;
	@Autowired
	private GameDiscountSetService gdsService;
	@Autowired
	private ImageLibService iService;

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
		List<GameDiscountSet> allDiscount = gdsService.findAll();
		model.addAttribute("allType", allType);
		model.addAttribute("allDiscount", allDiscount);
		return "game/insert-game";
	}

	// 進行新增遊戲
	@PostMapping("/game/insertGame")
	public String insertGame(@ModelAttribute Game game, @RequestParam List<Integer> typeId,
			@RequestParam("photos") MultipartFile[] photos, @RequestParam BigDecimal discountRate,
			@RequestParam Integer discountId) throws IOException {
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
			game.setGameDiscounts(gameDiscounts);
			discountSet.setGameDiscounts(gameDiscounts);
		}
		//重新存入帶有圖片與優惠的遊戲
		gService.save(newGame);
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

}
