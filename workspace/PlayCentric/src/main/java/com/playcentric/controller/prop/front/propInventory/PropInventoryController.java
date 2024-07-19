package com.playcentric.controller.prop.front.propInventory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.playcentric.model.prop.MemberPropInventory.MemberPropInventory;
import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryForFrontDto;
import com.playcentric.model.prop.sellOrder.PropSellOrder;
import com.playcentric.service.prop.MemberPropInventoryService.MemberPropInventoryService;
import com.playcentric.service.prop.sellOrder.PropSellOrderService;


@Controller
public class PropInventoryController {
	
	@Autowired
	MemberPropInventoryService memberPropInventoryService;
	
	@Autowired
	PropSellOrderService propSellOrderService;

	private Optional<MemberPropInventory> memberPropById;
	
	
	//根據會員id讀取倉庫資料
	@GetMapping("prop/memberPropsbyMemId")
	@ResponseBody
	public List<MemberPropInventoryForFrontDto> memberPropsbyMemId(int memId) {
		return memberPropInventoryService.memberPropsbyMemId(memId);		 
	}

	
	//販賣物品
	@PostMapping("prop/propSellorderSave")
	@ResponseBody
	public String savePropSellOrder(@RequestParam("amount") int amount,@RequestParam("propId") int propId,@RequestParam("quantity") int quantity,@RequestParam("memId") int memId  ) {
		
		LocalDateTime now = LocalDateTime.now();
		
		PropSellOrder propSellOrder = new PropSellOrder();
		propSellOrder.setAmount(amount);
		propSellOrder.setExpiryTime(now.plusDays(7));
//		propSellOrder.setMemberPropInventory(null);
//		propSellOrder.setOrderId(0); 自動生成
		propSellOrder.setOrderStatus((byte)0);
		propSellOrder.setPropId(propId);
		propSellOrder.setQuantity(quantity);
		propSellOrder.setSaleTime(now);
		propSellOrder.setSellerMemId(memId);
		propSellOrderService.SavePropSellOrder(propSellOrder);
		memberPropById = memberPropInventoryService.findMemberPropByIdAndUpdateQuantity(memId, propId,quantity);
		return "成功委託販賣!";
	}

	
}
