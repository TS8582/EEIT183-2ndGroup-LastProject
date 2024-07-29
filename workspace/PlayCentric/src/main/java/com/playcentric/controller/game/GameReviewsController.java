package com.playcentric.controller.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.secondary.GameReviews;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.game.GameReviewsService;
import com.playcentric.service.game.GameService;


@Controller
@SessionAttributes("loginMember")
public class GameReviewsController {
	
	@ModelAttribute("loginMember")
	public LoginMemDto createLoginMemDto() {
		return null;
	}
	
	@Autowired
	private GameReviewsService grService;
	
	@Autowired
	private GameService gService;
	
	@GetMapping("/gameReviews/show")
	public String showGameReviews(@RequestParam Integer gameId,Model model,
			@ModelAttribute("loginMember") LoginMemDto loginMember) {
		Game game = gService.findById(gameId);
		List<GameReviews> reviews = grService.findByGameId(gameId);
		if (loginMember != null) {
			List<GameReviews> memberReviews = grService.findByGameIdAndMemId(gameId, loginMember.getMemId());
			model.addAttribute("memberReviews",memberReviews);
		}
		model.addAttribute("reviews",reviews);
		model.addAttribute("game",game);
		return "game/show-game-reviews";
	}
	
	
}
