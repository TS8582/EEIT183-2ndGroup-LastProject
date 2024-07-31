package com.playcentric.model.game.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface OwnGameLibRepository extends JpaRepository<OwnGameLib,GameAndMemId> {
	
	List<OwnGameLib> findByMemId(Integer memId);
	
	List<OwnGameLib> findByMemIdOrderByBuyAtDesc(Integer memId);
	
}
