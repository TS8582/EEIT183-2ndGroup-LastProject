package com.playcentric.model.game.primary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game,Integer> {

	//29李岳澤為了製作用遊戲名稱查詢單筆資料的功能所以在這裡加上一些程式
	Optional<Game> findByGameNameContaining(String gameName);

}
