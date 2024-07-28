package com.playcentric.model.game.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface GameOrderDetailsRepository extends JpaRepository<GameOrderDetails,GameOrderDetailsId> {
	
	List<GameOrderDetails> findByGameOrderId(Integer gameOrderId);
	
}
