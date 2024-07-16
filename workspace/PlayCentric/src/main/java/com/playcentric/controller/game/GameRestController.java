package com.playcentric.controller.game;

import java.util.Arrays;
import java.util.Collections;
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
	    List<Game> gamePage = gService.findByIsShow().stream()
	            .filter(game -> game.getGameTypeLibs().stream()
	                    .map(GameTypeLib::getGameTypeId)
	                    .collect(Collectors.toSet())
	                    .containsAll(typeId))
	            .collect(Collectors.toList());
	    
	    int start = (int) pgb.getOffset();
	    int end = Math.min((start + pgb.getPageSize()), gamePage.size());
	    List<Game> subList = gamePage.subList(start, end);

	    Page<Game> game = new PageImpl<>(subList, pgb, gamePage.size());
	    return game;
	}
	
	//價格找遊戲
	@GetMapping("/game/getGamePageByPrice")
	public Page<Game> getGamePageByPrice(
			@RequestParam Integer pg,
			@RequestParam Integer minPrice,
			@RequestParam Integer maxPrice
			) {
		Pageable pgb = PageRequest.of(pg, 9);
		Page<Game> game = gService.findByPriceBetween(minPrice, maxPrice, pgb);
		return game;
	}
	
	//價格+分類篩選遊戲
	@GetMapping("/game/getGamePageByPriceAndType")
	public Page<Game> getGamePageByPriceAndType(@RequestParam Integer pg,
	                                            @RequestParam Integer minPrice,
	                                            @RequestParam Integer maxPrice,
	                                            @RequestParam List<Integer> typeId) {
	    Pageable pgb = PageRequest.of(pg, 9);
	    // 根據條件查詢遊戲
	    List<Game> gamePage = gService.findByPriceList(minPrice, maxPrice).stream()
	            .filter(game -> game.getGameTypeLibs().stream()
	                    .map(GameTypeLib::getGameTypeId)
	                    .collect(Collectors.toSet())
	                    .containsAll(typeId))
	            .collect(Collectors.toList());
	    System.out.println(gService.findByPriceList(minPrice, maxPrice));
	    // 如果查詢結果為空，返回空的分頁結果
	    if (gamePage.isEmpty()) {
	        return new PageImpl<>(Collections.emptyList(), pgb, 0);
	    }
	    
	    // 計算分頁索引
	    int start = (int) pgb.getOffset();
	    int end = Math.min(start + pgb.getPageSize(), gamePage.size());
	    
	    // 檢查分頁索引是否超出遊戲列表範圍
	    if (start <= end) {
	        List<Game> subList = gamePage.subList(start, end);
	        return new PageImpl<>(subList, pgb, gamePage.size());
	    } else {
	        return new PageImpl<>(Collections.emptyList(), pgb, gamePage.size());
	    }
	}

	
	
}
