package com.playcentric.model.prop.buyOrder2;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropBuyOrderRepository2 extends JpaRepository<PropBuyOrder2, Integer> {
	   @Query("SELECT o FROM PropBuyOrder2 o WHERE o.props.gameId = :gameId")
	    List<PropBuyOrder2> findAllByGameId(@Param("gameId") int gameId);
	   
	    @Query("SELECT o FROM PropBuyOrder2 o WHERE o.buyerMemId = :buyerMemId")
	    Page<PropBuyOrder2> findAllByMemId(@Param("buyerMemId") int buyerMemId, Pageable pageable);
	}
