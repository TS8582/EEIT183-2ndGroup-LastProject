package com.playcentric.model.game.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface GameOrderRepository extends JpaRepository<GameOrder,Integer> {
	
	List<GameOrder> findByMemIdAndStatus(Integer memId, Integer status);
	
}
