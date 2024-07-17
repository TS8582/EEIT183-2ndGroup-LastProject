package com.playcentric.service.prop.sellOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.naming.java.javaURLContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.prop.sellOrder.PropSellOrder;
import com.playcentric.model.prop.sellOrder.PropSellOrderDto;
import com.playcentric.model.prop.sellOrder.PropSellOrderForMarketDto;
import com.playcentric.model.prop.sellOrder.PropSellOrderRepository;

import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryDto;

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
				propSellOrderDto.setOrderStatus((byte) 2);
				order.setOrderStatus((byte) 2);
				propSellOrderRepo.save(order);
			}
		}

		// 如果數量是0將訂單狀態改已售完
		if (order.getQuantity() == 0) {
			order.setOrderStatus((byte) 1);
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

	// 新增賣單
	public String SavePropSellOrder(PropSellOrder propSellOrder) {
		propSellOrderRepo.save(propSellOrder);
		return "賣單新增成功";
	}

	 // 根據 propId 找賣單並回傳賣單的每個價格區間和數量
    public List<PropSellOrderForMarketDto> findByPropId(Integer propId) {
        // 從 propSellOrderRepo 中查找所有指定 propId 的 PropSellOrder 對象
        List<PropSellOrder> orders = propSellOrderRepo.findAllByPropId(propId);

        // 排序
        orders.sort(Comparator.comparingDouble(PropSellOrder::getAmount).thenComparing(PropSellOrder::getSaleTime));

        // 將 PropSellOrder 列表轉換為 PropSellOrderForMarketDto 列表
        List<PropSellOrderForMarketDto> dtos = new ArrayList<>();
        if (orders.isEmpty()) {
            return dtos; // 如果沒有訂單，返回空列表
        }

        double currentAmount = orders.get(0).getAmount();
        int totalQuantity = 0;
        int i = 0;

        // 遍歷所有訂單，累計每個價格區間的 quantity
        while (i < orders.size()) {
            totalQuantity += orders.get(i).getQuantity();
            if (i + 1 < orders.size() && orders.get(i + 1).getAmount() != currentAmount) {
                dtos.add(new PropSellOrderForMarketDto(orders.get(i).getPropId(), currentAmount, totalQuantity));
                totalQuantity = 0;
                currentAmount = orders.get(i + 1).getAmount();
            }
            i++;
        }

        // 添加最後一個價格區間的數量
        if (totalQuantity > 0) {
            dtos.add(new PropSellOrderForMarketDto(orders.get(i - 1).getPropId(), currentAmount, totalQuantity));
        }

        return dtos;
    }

}
