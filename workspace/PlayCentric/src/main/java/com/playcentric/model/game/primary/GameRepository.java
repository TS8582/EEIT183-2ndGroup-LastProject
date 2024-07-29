package com.playcentric.model.game.primary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game,Integer> {

	//29李岳澤為了製作用遊戲名稱查詢單筆資料的功能所以在這裡加上一些程式
	Optional<Game> findByGameNameContaining(String gameName);
	
	Page<Game> findByIsShow(Boolean isShow,Pageable pgb);
	
	Page<Game> findByIsShowOrderByReleaseAtDesc(Boolean isShow,Pageable pgb);
	
	List<Game> findByIsShowOrderByReleaseAtDesc(Boolean isShow);
	
	List<Game> findByIsShow(Boolean isShow);
	
	Page<Game> findByPriceBetween(Integer minPrice,Integer maxPrice,Pageable pgb);
	
	List<Game> findByPriceBetween(Integer minPrice,Integer maxPrice);
}
