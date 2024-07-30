package com.playcentric.controller.game;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.primary.GameDiscount;
import com.playcentric.model.game.primary.GameTypeLib;
import com.playcentric.model.game.secondary.GameCarts;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.service.game.GameCartService;
import com.playcentric.service.game.GameService;

@RestController
@SessionAttributes("loginMember")
public class GameRestController {
	
	@ModelAttribute("loginMember")
	public LoginMemDto createLoginMemDto() {
		return null;
	}
	
	@Autowired
	private GameService gService;
	@Autowired
	private GameCartService gcService;
	
	//所有遊戲
	@GetMapping("/game/getGamePage")
	public Page<Game> getGamePage(@RequestParam Integer pg,@ModelAttribute("loginMember") LoginMemDto loginMember) {
		Pageable pgb = PageRequest.of(pg, 9);
		Page<Game> findGames = gService.findByIsShowOrderByReleaseAtDesc(pgb);
		for (Game game : findGames) {
			gService.setRateAndDiscountPrice(game);
			if (loginMember != null) {
				gcService.setInCart(loginMember, game);
			}
		}
		
		return findGames;
	}
	
	//用分類找遊戲
	@GetMapping("/game/getGamePageByType")
	public Page<Game> getGamePageByType(@RequestParam Integer pg,
	                                    @RequestParam List<Integer> typeId,
	                                    @ModelAttribute("loginMember") LoginMemDto loginMember) {
	    Pageable pgb = PageRequest.of(pg, 9);
	    List<Game> gamePage = gService.findByIsShowOrderByReleaseAtDesc().stream()
	            .filter(game -> game.getGameTypeLibs().stream()
	                    .map(GameTypeLib::getGameTypeId)
	                    .collect(Collectors.toSet())
	                    .containsAll(typeId))
	            .collect(Collectors.toList());
	    
	    if (gamePage.isEmpty()) {
	        return new PageImpl<>(Collections.emptyList(), pgb, 0);
	    }
	    else {
			for (Game game2 : gamePage) {
				gService.setRateAndDiscountPrice(game2);
				if (loginMember != null) {
					gcService.setInCart(loginMember, game2);
				}
			}
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
	
	//價格找遊戲
	@GetMapping("/game/getGamePageByPrice")
	public Page<Game> getGamePageByPrice(
			@RequestParam Integer pg,
			@RequestParam Integer minPrice,
			@RequestParam Integer maxPrice,
			@ModelAttribute("loginMember") LoginMemDto loginMember
			) {
		Pageable pgb = PageRequest.of(pg, 9);
		List<Game> gamelist = gService.findByIsShowOrderByReleaseAtDesc().stream()
				.filter(game -> {
					Integer discountedPrice = game.getPrice();
					Integer myDiscountPrice = gService.setRateAndDiscountPrice(game);
					if (loginMember != null) {
						gcService.setInCart(loginMember, game);
					}
					if (myDiscountPrice != null) {
						discountedPrice = myDiscountPrice;
					}
	                return discountedPrice >= minPrice && discountedPrice <= maxPrice;
	            })
	            .collect(Collectors.toList());
		// 計算分頁索引
	    int start = (int) pgb.getOffset();
	    int end = Math.min(start + pgb.getPageSize(), gamelist.size());

	    if (start <= end) {
	        List<Game> subList = gamelist.subList(start, end);
	        return new PageImpl<>(subList, pgb, gamelist.size());
	    } else {
	        return new PageImpl<>(Collections.emptyList(), pgb, gamelist.size());
	    }
	}
	
	//價格+分類篩選遊戲
	@GetMapping("/game/getGamePageByPriceAndType")
	public Page<Game> getGamePageByPriceAndType(
			@RequestParam Integer pg,
			@RequestParam Integer minPrice,
			@RequestParam Integer maxPrice,
			@RequestParam List<Integer> typeId,
			@ModelAttribute("loginMember") LoginMemDto loginMember) {
	    Pageable pgb = PageRequest.of(pg, 9);
	    // 根據條件查詢遊戲
	    List<Game> gamePage = gService.findByIsShowOrderByReleaseAtDesc().stream()
	            .filter(game -> game.getGameTypeLibs().stream()
	                    .map(GameTypeLib::getGameTypeId)
	                    .collect(Collectors.toSet())
	                    .containsAll(typeId))
	            .filter(game -> {
	            	if (loginMember != null) {
	        			gcService.setInCart(loginMember, game);
	        		}
	            	Integer discountedPrice;
	            	discountedPrice = game.getPrice();
					gService.setRateAndDiscountPrice(game);
	                // 过滤折扣后价格在指定范围内的游戏
	                return discountedPrice >= minPrice && discountedPrice <= maxPrice;
	            })
	            .collect(Collectors.toList());
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

	//遊戲上下架
	@GetMapping("/game/isShow")
	public void postMethodName(@RequestParam Integer gameId) {
		Game game = gService.findById(gameId);
		if (game.getIsShow() == true) game.setIsShow(false);
		else game.setIsShow(true);
		gService.save(game);
	}
	
	@GetMapping("/personal/game/download")
	public ResponseEntity<Resource> downloadGame(Integer gameId) {
		Game game = gService.findById(gameId);
		if (game != null) {
			ByteArrayResource resource = new ByteArrayResource(game.getGameFile());
			return ResponseEntity.ok()
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"")
	                .body(resource);
		}
		return ResponseEntity.notFound().build();
	}
	
	
}
