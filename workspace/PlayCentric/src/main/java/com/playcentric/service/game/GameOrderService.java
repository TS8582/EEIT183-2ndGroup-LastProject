package com.playcentric.service.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.game.transaction.GameOrder;
import com.playcentric.model.game.transaction.GameOrderDetails;
import com.playcentric.model.game.transaction.GameOrderDetailsRepository;
import com.playcentric.model.game.transaction.GameOrderRepository;

@Service
public class GameOrderService {
	
	@Autowired
	private GameOrderRepository oRepo;
	@Autowired
	private GameOrderDetailsRepository odRepo;
	
	public GameOrder save(GameOrder gameOrder) {
		return oRepo.save(gameOrder);
	}
	
	public GameOrderDetails saveDetails(GameOrderDetails orderDetails) {
		return odRepo.save(orderDetails);
	}
	
	public GameOrder findById(Integer gameOrderId) {
		return oRepo.findById(gameOrderId).get();
	}
	
}
