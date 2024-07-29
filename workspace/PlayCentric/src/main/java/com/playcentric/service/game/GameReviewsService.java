package com.playcentric.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.game.secondary.GameReviews;
import com.playcentric.model.game.secondary.GameReviewsRepository;

@Service
public class GameReviewsService {
	
	@Autowired
	private GameReviewsRepository grRepo;
	
	public List<GameReviews> findByGameIdTop5(Integer gameId) {
		return grRepo.findByGameIdTop5(gameId);
	}
	
	public List<GameReviews> findByGameId(Integer gameId) {
		return grRepo.findByGameIdOrderByReviewsAtDesc(gameId);
	}
	
	public GameReviews save(GameReviews gameReviews) {
		return grRepo.save(gameReviews);
	}
	
	public List<GameReviews> findByGameIdAndMemId(Integer gameId, Integer memId) {
		return grRepo.findByGameIdAndMemId(gameId, memId);
	}
	
}
