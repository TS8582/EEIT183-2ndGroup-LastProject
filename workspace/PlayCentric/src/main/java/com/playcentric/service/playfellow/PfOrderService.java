package com.playcentric.service.playfellow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.playcentric.model.member.Member;
import com.playcentric.model.playfellow.PfOrder;
import com.playcentric.model.playfellow.PfOrderRepository;

@Service
@SessionAttributes(names = { "loginMember" })
public class PfOrderService {
	
	@Autowired
	PfOrderRepository pfOrderRepository;

	public List<PfOrder> getAllPfOrder() {
		List<PfOrder> pfOrders = pfOrderRepository.findAll();
		return pfOrders;
	}
	
	public PfOrder savePfOrder(PfOrder pfOrder) {
		return pfOrderRepository.save(pfOrder);
	}
	
	public List<PfOrder> findByMember(Member member){
		return pfOrderRepository.findByMember(member);
	}
	
	
	public List<PfOrder> findpfMemberOfpfOrderByMember(Integer memId) {
		return pfOrderRepository.findOrdersByMember(memId);
	}
	
	
}
