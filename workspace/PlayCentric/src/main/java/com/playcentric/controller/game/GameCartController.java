package com.playcentric.controller.game;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.secondary.GameCarts;
import com.playcentric.model.game.transaction.Payment;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.PaymentService;
import com.playcentric.service.game.GameCartService;
import com.playcentric.service.game.GameService;

@Controller
@RequestMapping("/personal/gamecart")
@SessionAttributes("loginMember")
public class GameCartController {
	
	@ModelAttribute("loginMember")
	public LoginMemDto getLoginMember() {
		return null;
	}
	
	@Autowired
	private GameCartService gcService;
	@Autowired
	private GameService gService;
	@Autowired
	private PaymentService pService;
	
	
	
	@GetMapping("/get")
	public String findMemGameCarts(@ModelAttribute("loginMember") LoginMemDto loginMember,Model model) {
		if (loginMember == null) {
			return "redirect:/member/homeShowErr/notLogin";
		}
		List<GameCarts> gamecarts = gcService.findByMemId(loginMember.getMemId());
		List<Game> games = new ArrayList<>();
		if (gamecarts.size() > 0) {
			for (GameCarts gamecart : gamecarts) {
				Game game = gService.findById(gamecart.getGameId());
				gService.setRateAndDiscountPrice(game);
				games.add(game);
			}
		}
		List<Payment> payments = pService.findAll();
		model.addAttribute("payments",payments);
		model.addAttribute("games",games);
		return "game/show-gamecart";
	}
	
	
}
