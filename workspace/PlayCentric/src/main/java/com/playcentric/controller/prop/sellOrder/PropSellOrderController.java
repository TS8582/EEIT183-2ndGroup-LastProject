package com.playcentric.controller.prop.sellOrder;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.playcentric.model.prop.MemberPropInventory.MemberPropInventory;
import com.playcentric.model.prop.sellOrder.PropSellOrder;
import com.playcentric.model.prop.sellOrder.PropSellOrderDto;
import com.playcentric.service.prop.MemberPropInventoryService.MemberPropInventoryService;
import com.playcentric.service.prop.sellOrder.PropSellOrderService;

@Controller
@SessionAttributes(names = "games")
public class PropSellOrderController {
	
	private PropSellOrderDto sellOrder;


	@Autowired
	PropSellOrderService propSellOrderService;

	
	@Autowired
	MemberPropInventoryService memberPropInventoryService;


	private int quantity;

	// 進入賣單頁面
	@GetMapping("/prop/propSellOrder")
	public String showPropSellOrderPage() {
		return "prop/propSellOrder";
	}

	// 根據遊戲Id找所有賣單
	@GetMapping("/prop/findAllpropSellOrder")
	@ResponseBody
	public List<PropSellOrderDto> findAllPropSellOrder(@RequestParam("gameId") int gameId) {
		return propSellOrderService.findAllByGameId(gameId);
	}

	// 根據orderId找賣單
	@GetMapping("/prop/findPropSellOrderByOrderId")
	@ResponseBody
	public PropSellOrderDto findSellOrderById(@RequestParam("orderId") int orderId) {
		return propSellOrderService.findByOrderId(orderId);
	}
	
	// 根據gameId及memId找賣單
	@GetMapping("/prop/findpropSellOrderBygameIdAndmemId")
	@ResponseBody
	public List<PropSellOrderDto> findpropSellOrderBygameIdAndmemId(@RequestParam("gameId") int gameId,@RequestParam("memId") int memId) {
		return null;
	}
	
	// 根據orderId退單
	// 1.找到賣單改下架 2.根據賣單道具id及數量新增至倉庫
	@PostMapping("prop/cancelSell")
	@ResponseBody
	public String cancelSell(int orderId) {
		 PropSellOrder order = propSellOrderService.changeStatusDelist(orderId);
		 int sellerMemId = order.getSellerMemId();
		 int propId = order.getPropId();
		 int quantity = order.getQuantity();
		 memberPropInventoryService.findMemberPropByIdAndPlusQuantity(sellerMemId, propId, quantity);
		return "取消拍賣OK!";
	}
	
}
