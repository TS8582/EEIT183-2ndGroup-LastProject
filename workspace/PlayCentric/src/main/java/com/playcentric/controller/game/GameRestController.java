package com.playcentric.controller.game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.primary.GameDiscount;
import com.playcentric.model.game.primary.GameTypeLib;
import com.playcentric.service.game.GameService;

@RestController
public class GameRestController {
	
	@Autowired
	private GameService gService;
	
	//所有遊戲
	@GetMapping("/game/getGamePage")
	public Page<Game> getGamePage(@RequestParam Integer pg) {
		Pageable pgb = PageRequest.of(pg, 9);
		Page<Game> findGames = gService.findShowInStore(pgb);
		for (Game game : findGames) {
			System.out.println(game);
			GameDiscount nowDiscount = gService.findNowDiscount(game.getGameId());
			if (nowDiscount != null) {
				Double oldRate = Double.parseDouble(nowDiscount.getDiscountRate().toString());
				int rate = (int) (oldRate * 100);
				game.setRate(rate);
			}
		}
		return findGames;
	}
	
	//用分類找遊戲
	@GetMapping("/game/getGamePageByType")
	public Page<Game> getGamePageByType(@RequestParam Integer pg,
			@RequestParam List<Integer> typeId) {
		Pageable pgb = PageRequest.of(pg, 9);
		List<Game> gamePage = gService.findShowInStore(pgb).stream()
				.filter(game -> game.getGameTypeLibs().stream()
						.map(GameTypeLib::getGameTypeId)
						.collect(Collectors.toSet())
						.containsAll(typeId))
				.collect(Collectors.toList());
		if (!gamePage.isEmpty()) {
			return new PageImpl<>(gamePage,pgb,gamePage.size());
		}
		return null;
	}
	
	//價格找遊戲
	@GetMapping("/game/getGamePageByPrice")
	public Page<Game> getGamePageByPrice(@RequestParam Integer pg,
			@RequestParam Integer minPrice,@RequestParam Integer maxPrice) {
		Pageable pgb = PageRequest.of(pg, 9);
		Page<Game> gamePage = gService.findByPriceBetween(minPrice, maxPrice, pgb);
		if (!gamePage.isEmpty()) {
			return gamePage;
		}
		return null;
	}
	
	
}
