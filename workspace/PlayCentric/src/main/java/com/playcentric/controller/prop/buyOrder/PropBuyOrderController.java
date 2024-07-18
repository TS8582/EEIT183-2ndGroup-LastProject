package com.playcentric.controller.prop.buyOrder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.controller.prop.front.propInventory.PropInventoryController;
import com.playcentric.model.ImageLib;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.prop.Props;
import com.playcentric.model.prop.buyOrder.PropBuyOrder;
import com.playcentric.model.prop.buyOrder.PropBuyOrderDto;
import com.playcentric.model.prop.sellOrder.PropSellOrder;
import com.playcentric.model.prop.sellOrder.PropSellOrderDto;
import com.playcentric.model.prop.sellOrder.PropSellOrderForMarketDto;
import com.playcentric.model.prop.type.PropType;
import com.playcentric.service.game.GameService;
import com.playcentric.service.prop.PropService;
import com.playcentric.service.prop.MemberPropInventoryService.MemberPropInventoryService;
import com.playcentric.service.prop.buyOrder.PropBuyOrderService;
import com.playcentric.service.prop.buyOrder.PropBuyOrderService2;
import com.playcentric.service.prop.sellOrder.PropSellOrderService;
import com.playcentric.service.prop.type.PropTypeService;
import com.playcentric.service.ImageLibService;

@Controller
@SessionAttributes(names = "games")
public class PropBuyOrderController {

	@Autowired
	PropBuyOrderService propBuyOrderService;
	
	@Autowired
	PropBuyOrderService2 propBuyOrderService2;

	@Autowired
	PropSellOrderService propSellOrderService;

	@Autowired
	MemberPropInventoryService memberPropInventoryService;

	// 進入成交紀錄頁面
	@GetMapping("/prop/propTradeRecord")
	public String showpropTradeRecordPage() {
		return "prop/propTradeRecord";
	}

	// 根據遊戲Id找所有買單
	@GetMapping("/prop/findAllpropBuyOrder")
	@ResponseBody
	public List<PropBuyOrderDto> finAllPropBuyOrder(@RequestParam("gameId") int gameId) {
		return propBuyOrderService.findPropBuyOrders(gameId);
	}

	// 根據OrderId找所有買單
//	@GetMapping("/prop/findAllpropBuyOrderByOrderId")
//	@ResponseBody
//	public List<PropBuyOrderDto> findAllpropBuyOrderByOrderId(@RequestParam("orderId") int orderId) {
//		return propBuyOrderService.findAllByOrderId(orderId);
//	}

	// 購買道具
	@PostMapping("/prop/front/buyProp")
	@ResponseBody
	// 1.找尋所有賣單由價格低到高排序後依購買quantity扣除賣單的quantity並修改賣單quantity為0的status，
	public String buyProp(@RequestParam("quantity") Integer quantity, @RequestParam("propId") Integer propId,
			@RequestParam("memId") Integer memId,@RequestParam("paymentId") Integer paymentId,@RequestParam("price") Integer price) {
		propSellOrderService.buyProp(quantity, propId);

		// 2.根據propId及memId雙主鍵增加買家的propQuantity
		memberPropInventoryService.findMemberPropByIdAndPlusQuantity(memId, propId, quantity);

		// 3.新增買單
        propBuyOrderService2.savePropBuyOrder(memId, quantity, paymentId, price);
		
        // 4.扣除點數
		return "購買完成";

	}

}
