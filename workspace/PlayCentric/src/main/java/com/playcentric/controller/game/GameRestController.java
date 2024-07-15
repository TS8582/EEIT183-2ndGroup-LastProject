package com.playcentric.controller.game;

import org.springframework.web.bind.annotation.RestController;

import com.playcentric.model.game.primary.Game;
import com.playcentric.service.game.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class GameRestController {
	
	@Autowired
	private GameService gService;
	
	@GetMapping("/game/getpage")
	public Page<Game> getProducts(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 9); // 每頁顯示9個遊戲
        return gService.findShowInStore(pageable);
    }
	
	
	
}
