package com.playcentric.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.game.primary.GameDiscountSet;
import com.playcentric.model.game.primary.GameDiscountSetRepository;

@Service
public class GameDiscountSetService {
	
	@Autowired
	private GameDiscountSetRepository rep;
	
	//新增優惠活動
	public GameDiscountSet insert(GameDiscountSet discountSet) {
		return rep.save(discountSet);
	}
	
	//獲取所有優惠活動
	public List<GameDiscountSet> findAll() {
		return rep.findAll();
	}
	
	//以id找優惠活動
	public GameDiscountSet findById(Integer id) {
		return rep.findById(id).get();
	}
	
}
