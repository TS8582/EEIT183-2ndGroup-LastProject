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
	
	public List<GameReviews> findByGameIdTop1(Integer gameId) {
		return grRepo.findByGameIdTop1(gameId);
	}
	
	public List<GameReviews> findByGameId(Integer gameId) {
		return grRepo.findByGameId(gameId);
	}
	
	public GameReviews save(GameReviews gameReviews) {
		return grRepo.save(gameReviews);
	}
	
}
