package com.playcentric.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.game.primary.GameTypeLib;
import com.playcentric.model.game.primary.GameTypeLibRepository;

@Service
public class GameTypeService {
	
	@Autowired
	private GameTypeLibRepository rep;
	
	//獲取所有遊戲分類
	public List<GameTypeLib> findAll() {
		return rep.findAll();
	}
	
	//以id尋找單個分類
	public GameTypeLib findById(Integer id) {
		return rep.findById(id).get();
	}
		
}
