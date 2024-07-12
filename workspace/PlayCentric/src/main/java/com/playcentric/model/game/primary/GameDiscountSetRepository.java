package com.playcentric.model.game.primary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;


public interface GameDiscountSetRepository extends JpaRepository<GameDiscountSet,Integer> {
	@Query("from GameDiscountSet where :date between startAt and endAt")
	public List<GameDiscountSet> findBetweenStartAndEnd(@Param("date") LocalDateTime date);
}
