package com.playcentric.service.prop.buyOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.playcentric.model.member.Member;
import com.playcentric.model.prop.buyOrder.PropBuyOrder;
import com.playcentric.model.prop.buyOrder.PropBuyOrderDto;
import com.playcentric.model.prop.buyOrder.PropBuyOrderRepository;
import com.playcentric.model.prop.sellOrder.PropSellOrder;
import com.playcentric.model.prop.sellOrder.PropSellOrderRepository;
import com.playcentric.service.prop.sellOrder.PropSellOrderService;

@Service
public class PropBuyOrderService {

	@Autowired
	private PropSellOrderRepository propSellOrderRepo;

	@Autowired
	private PropBuyOrderRepository propBuyOrderRepo;

	@Autowired
	private PropSellOrderService propSellOrderService;

	public List<PropBuyOrderDto> findPropBuyOrders(int gameId) {
		List<PropBuyOrder> propBuyOrders = propBuyOrderRepo.findAllByGameId(gameId);
		return propBuyOrders.stream().map(PropBuyOrderDto::new).collect(Collectors.toList());
	}

//	public List<PropBuyOrderDto> findAllByOrderId(int orderId) {
//		List<PropBuyOrder> propBuyOrders = propBuyOrderRepo.findAllByOrderId(orderId);
//		return propBuyOrders.stream().map(PropBuyOrderDto::new).collect(Collectors.toList());
//	}

	// 原有的購買方法保留
//    public void buyProp(int propSellOrderId, int buyQuantity, Member member, LocalDateTime orderTime, int price) {
//        // 獲取銷售訂單
//        PropSellOrder propSellOrder = propSellOrderRepo.findById(propSellOrderId)
//            .orElseThrow(() -> new RuntimeException("Sell order not found"));
//
//        // 創建購買訂單
//        PropBuyOrder propBuyOrder = new PropBuyOrder();
//        propBuyOrder.setId(new PropBuyOrderId(propSellOrderId, member.getMemId())); // 設置複合主鍵
//        propBuyOrder.setMember(member);
//        propBuyOrder.setOrderTime(LocalDateTime.now()); // 設置當前時間
//        propBuyOrder.setQuantity(buyQuantity);
//        propBuyOrder.setPrice(price);
//        propBuyOrder.setPropSellOrder(propSellOrder);
//        // 設置支付信息，假設有支付信息設置方法
//        // propBuyOrder.setPayment(payment);
//
//        // 保存購買訂單
//        propBuyOrderRepo.save(propBuyOrder);
//
//        // 調用賣單service修改賣單的數量
//        propSellOrderService.updateSellOrderQuantity(propSellOrderId, buyQuantity);
//
//        //調用倉庫service修改倉庫的數量
//        
//    }
}
