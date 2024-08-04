package com.playcentric.model.game.primary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game,Integer> {

	//29李岳澤為了製作用遊戲名稱查詢單筆資料的功能所以在這裡加上一些程式
	Optional<Game> findByGameNameContaining(String gameName);
	
	Page<Game> findByIsShow(Boolean isShow,Pageable pgb);
	
	Page<Game> findByIsShowOrderByReleaseAtDesc(Boolean isShow,Pageable pgb);
	
	Page<Game> findByIsShowAndGameNameContainingOrderByReleaseAtDesc(Boolean isShow, String gameName, Pageable pgb);
	
	List<Game> findByIsShowAndGameNameContainingOrderByReleaseAtDesc(Boolean isShow, String gameName);
	
	List<Game> findByIsShowOrderByReleaseAtDesc(Boolean isShow);
	
	List<Game> findByIsShow(Boolean isShow);
	
	Page<Game> findByPriceBetween(Integer minPrice,Integer maxPrice,Pageable pgb);
	
	List<Game> findByPriceBetween(Integer minPrice,Integer maxPrice);
	
	@Query(value = "SELECT TOP 5 gameId from gameOrderDetails group by gameId order by count(gameId) desc",nativeQuery = true)
	List<Integer> findTop5();
	
	@Query(value = "select top 5 * from game order by releaseAt desc",nativeQuery = true)
	List<Game> findNew5();
}
