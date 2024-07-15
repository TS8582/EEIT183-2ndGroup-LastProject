package com.playcentric.model.game.primary;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameDiscountRepository extends JpaRepository<GameDiscount,GameDiscountId>{
	
	@Query("from GameDiscount gd join GameDiscountSet gds on gd.gameDiscountId = gds.gameDiscountId where (:d1 between gds.startAt and gds.endAt) and gd.gameId = :id")
	GameDiscount findNowDiscount(@Param("d1") LocalDateTime now,@Param("id") Integer gameId);
	
	
}
