package com.playcentric.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.game.secondary.GameCarts;
import com.playcentric.model.game.secondary.GameCartsRepository;

@Service
public class GameCartService {
	
	@Autowired
	private GameCartsRepository rep;
	
	//加入購物車
	public GameCarts insert(GameCarts gameCarts) {
		return rep.save(gameCarts);
	}
	
	//從購物車內移除
	public void remove(Integer gameId,Integer memId) {
		rep.deleteByGameIdAndMemId(gameId, memId);
	}
	
	//查詢會員的購物車
	public List<GameCarts> findByMemId(Integer memId) {
		return rep.findByMemId(memId);
	}
	
}
