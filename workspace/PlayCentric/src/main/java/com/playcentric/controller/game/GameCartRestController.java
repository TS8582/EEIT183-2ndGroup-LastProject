package com.playcentric.controller.game;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.playcentric.model.game.secondary.GameCarts;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.game.GameCartService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/gamecart")
public class GameCartRestController {
	
	private GameCartService gcService;
	
	@GetMapping("/insert")
	public List<GameCarts> inserGameCart(
			@RequestParam GameCarts gameCarts) {
		gcService.insert(gameCarts);
		return gcService.findByMemId(gameCarts.getMemId());
	}
	
}
