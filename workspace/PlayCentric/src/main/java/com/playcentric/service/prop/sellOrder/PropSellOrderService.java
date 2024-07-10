package com.playcentric.service.prop.sellOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.prop.sellOrder.PropSellOrder;
import com.playcentric.model.prop.sellOrder.PropSellOrderDto;
import com.playcentric.model.prop.sellOrder.PropSellOrderRepository;

import jakarta.persistence.criteria.Order;

import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryDto;
import com.playcentric.model.prop.buyOrder.PropBuyOrder;
import com.playcentric.model.prop.buyOrder.PropBuyOrderDto;

@Service
public class PropSellOrderService {

	@Autowired
	PropSellOrderRepository propSellOrderRepo;
	

	public List<PropSellOrderDto> findAllByGameId(int id) {
		List<PropSellOrder> propSellOrders = propSellOrderRepo.findAllByGameId(id);
		return propSellOrders.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	private PropSellOrderDto convertToDto(PropSellOrder order) {
		PropSellOrderDto propSellOrderDto = new PropSellOrderDto();
		propSellOrderDto.setAmount(order.getAmount());

		// 格式化時間
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		propSellOrderDto.setExpiryTime(order.getExpiryTime().format(formatter));
		propSellOrderDto.setSaleTime(order.getSaleTime().format(formatter));

		// 如果時間到期將訂單狀態改已下架
		if (LocalDateTime.now().isBefore(order.getExpiryTime())) {
			propSellOrderDto.setOrderStatus(order.getOrderStatus());
		} else {
			{
				propSellOrderDto.setOrderStatus((byte)2);
				order.setOrderStatus((byte) 2);
				propSellOrderRepo.save(order);			}
		}
		
		//如果數量是0將訂單狀態改已售完		
		if(order.getQuantity()==0) {
			order.setOrderStatus((byte)1);
			propSellOrderRepo.save(order);			
		}
		

		// 將 MemberPropInventory 轉換為 MemberPropInventoryDto
		MemberPropInventoryDto memberPropInventoryDto = new MemberPropInventoryDto(order.getMemberPropInventory());
		propSellOrderDto.setMemberPropInventoryDto(memberPropInventoryDto);
		propSellOrderDto.setOrderId(order.getOrderId());
		propSellOrderDto.setPropId(order.getOrderId());
		propSellOrderDto.setQuantity(order.getQuantity());
		propSellOrderDto.setSellerMemId(order.getSellerMemId());
		return propSellOrderDto;
	}
	// 變更賣單數量
    public void updateSellOrderQuantity(int orderId, int buyQuantity) {
        Optional<PropSellOrder> optionalOrder = propSellOrderRepo.findById(orderId);
        PropSellOrder order = optionalOrder.get();
        int newQuantity = order.getQuantity() - buyQuantity;
        order.setQuantity(newQuantity);
        propSellOrderRepo.save(order);
		}
    
    // 根據orderId找賣單
    public PropSellOrderDto findByOrderId(Integer orderId) {
        Optional<PropSellOrder> optionalPropSellOrder = propSellOrderRepo.findById(orderId);
        return optionalPropSellOrder.map(PropSellOrderDto::new).orElse(null);
    }
    

}
