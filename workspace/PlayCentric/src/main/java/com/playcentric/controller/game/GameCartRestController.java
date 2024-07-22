package com.playcentric.controller.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.playcentric.model.game.secondary.GameCarts;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.game.GameCartService;
import com.playcentric.service.game.GameService;
import com.playcentric.service.member.MemberService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/gamecart")
@SessionAttributes("loginMember")
public class GameCartRestController {
	
	
	@ModelAttribute("loginMember")
	public LoginMemDto createLoginMemDto() {
		return null;
	}
	
	@Autowired
	private GameCartService gcService;
	@Autowired
	private GameService gService;
	@Autowired
	private MemberService mService;
	
	
	@GetMapping("/insert")
	public void inserGameCart(
			@RequestParam Integer gameId,
			@RequestParam Integer memId) {
		GameCarts gameCarts = new GameCarts();
		gameCarts.setGameId(gameId);
		gameCarts.setMemId(memId);
		gameCarts.setGame(gService.findById(gameId));
		gameCarts.setMember(mService.findById(memId));
		gcService.insert(gameCarts);
	}
	
	@GetMapping("/remove")
	public void removeGameCart(
			@RequestParam Integer gameId,
			@ModelAttribute("loginMember") LoginMemDto loginMember){
		gcService.remove(gameId,loginMember.getMemId());
	}
	
	
}
