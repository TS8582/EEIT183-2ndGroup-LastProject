package com.playcentric.model.game.secondary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameCartsRepository extends JpaRepository<GameCarts,GameAndMemId> {
	
	void deleteByGameIdAndMemId(Integer gameId, Integer memId);
	
	List<GameCarts> findByMemId(Integer memId);
}
