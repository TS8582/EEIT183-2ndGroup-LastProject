package com.playcentric.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.game.transaction.Payment;
import com.playcentric.model.game.transaction.PaymentRepository;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentRepository pRepo;
	
	public List<Payment> findAll() {
		return pRepo.findAll();
	}
	
	public Payment findById(Integer paymentId) {
		return pRepo.findById(paymentId).get();
	}
	
}
