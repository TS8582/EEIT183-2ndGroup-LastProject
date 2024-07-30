package com.playcentric.model.game.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GameReviewsRepository extends JpaRepository<GameReviews,Integer> {
	
	@Query(value = "SELECT TOP 5 * FROM gameReviews Where gameId = :gameId ORDER BY reviewsAt DESC",nativeQuery = true)
	List<GameReviews> findByGameIdTop5(@Param("gameId")Integer gameId);
	
	List<GameReviews> findByGameIdOrderByReviewsAtDesc(Integer gameId);
	
	List<GameReviews> findByGameIdAndMemId(Integer gameId, Integer memId);
	
}
