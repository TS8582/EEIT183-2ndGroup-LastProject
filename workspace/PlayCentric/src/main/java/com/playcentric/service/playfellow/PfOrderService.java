package com.playcentric.service.playfellow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.playfellow.PfOrder;
import com.playcentric.model.playfellow.PfOrderRepository;

@Service
public class PfOrderService {
	
	@Autowired
	PfOrderRepository pfOrderRepository;

	public List<PfOrder> getAllPfOrder() {
		List<PfOrder> pfOrders = pfOrderRepository.findAll();
		return pfOrders;
	}
}
