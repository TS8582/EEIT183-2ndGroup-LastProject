package com.playcentric.controller.game;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.secondary.GameReviews;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.game.GameReviewsService;
import com.playcentric.service.game.GameService;
import com.playcentric.service.member.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/personal/api/gameReviews")
@SessionAttributes("loginMember")
public class GameReviewsRestController {
	
	@Autowired
	private GameReviewsService grService;
	@Autowired
	private GameService gService;
	@Autowired
	private MemberService mService;
	
	@ModelAttribute("loginMember")
	public LoginMemDto getLoginMember() {
		return null;
	}
	
	@PostMapping("/add")
	public GameReviews postMethodName(
			@ModelAttribute("loginMember") LoginMemDto loginMember,
			@RequestBody GameReviews gameReviews
			) {
		if (loginMember != null) {
			Game game = gService.findById(gameReviews.getGameId());
			game.setTotalReviews(game.getTotalReviews() + 1);
			game.setTotalScore(game.getTotalScore() + gameReviews.getReviewsScore());
			gService.save(game);
			gameReviews.setMemId(loginMember.getMemId());
			gameReviews.setGame(game);
			gameReviews.setMember(mService.findById(loginMember.getMemId()));
			return grService.save(gameReviews);
		}
		return null;
	}
	
	
}
