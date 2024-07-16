package com.playcentric.controller.game;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
	
	@GetMapping("/game/getGamePageByType")
	public Page<Game> getGamePageByType(@RequestParam Integer pg,
			@RequestParam List<Integer> typeId) {
		Pageable pgb = PageRequest.of(pg, 9);
		return (Page<Game>) gService.findShowInStore(pgb).stream()
		.filter(game -> game.getGameTypeLibs().stream()
				.map(GameTypeLib::getGameTypeId)
				.collect(Collectors.toSet())
				.containsAll(typeId))
		.collect(Collectors.toList());
	}
	
	
}