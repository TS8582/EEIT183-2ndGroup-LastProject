package com.playcentric.controller.prop.buyOrder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.playcentric.model.member.Member;
import com.playcentric.model.prop.buyOrder2.PropBuyOrder2;
import com.playcentric.model.prop.sellOrder.PropSellOrder;
import com.playcentric.service.member.MemberService;
import com.playcentric.service.prop.MemberPropInventoryService.MemberPropInventoryService;
import com.playcentric.service.prop.buyOrder.PropBuyOrderService2;
import com.playcentric.service.prop.sellOrder.PropSellOrderService;

@Controller
@SessionAttributes(names = "games")
public class PropBuyOrderController {
//
//	@Autowired
//	PropBuyOrderService propBuyOrderService;
	
	@Autowired
	PropBuyOrderService2 propBuyOrderService2;

	@Autowired
	PropSellOrderService propSellOrderService;

	@Autowired
	MemberPropInventoryService memberPropInventoryService;
	
	@Autowired
	MemberService memberService;
	

	// 進入成交紀錄頁面
	@GetMapping("/prop/propTradeRecord")
	public String showpropTradeRecordPage() {
		return "prop/propTradeRecord";
	}

	// 根據遊戲Id找所有買單
//	@GetMapping("/prop/findAllpropBuyOrder")
//	@ResponseBody
//	public List<PropBuyOrderDto> finAllPropBuyOrder(@RequestParam("gameId") int gameId) {
//		return propBuyOrderService.findPropBuyOrders(gameId);
//	}
	
	// 根據遊戲Id找所有買單
	@GetMapping("/prop/findAllpropBuyOrder")
	@ResponseBody
	public List<PropBuyOrder2> findAllPropBuyOrder(@RequestParam("gameId") int gameId) {
		return propBuyOrderService2.findPropBuyOrders(gameId);
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
	public String buyProp(@RequestParam("quantity") Integer quantity, @RequestParam("propId") Integer propId,
            @RequestParam("memId") Integer memId, @RequestParam("paymentId") Integer paymentId,
			@RequestParam("price") Integer price, @RequestParam("selectedGameId") Integer selectedGameId) {

// 1-1 儲存未被買之前的所有賣單的 quantity
List<PropSellOrder> sellOrders = propSellOrderService.findAllByGameIdAndStatus0(selectedGameId);

// 過濾掉 sellerMemId 等於 loginMemId 的賣單
sellOrders = sellOrders.stream()
                 .filter(order -> order.getSellerMemId() != memId)
                 .collect(Collectors.toList());

Map<Integer, Integer> initialQuantities = sellOrders.stream()
  .collect(Collectors.toMap(PropSellOrder::getOrderId, PropSellOrder::getQuantity));

// 2. 找尋所有賣單由價格低到高排序後依購買 quantity 扣除賣單的 quantity 並修改賣單 quantity 為 0 的 status
propSellOrderService.buyProp(quantity, propId, memId);

// 1-2. 檢查全部賣單若 quantity 有變動 memberPoint += amount * 變動 quantity
for (PropSellOrder sellOrder : sellOrders) {
int initialQuantity = initialQuantities.get(sellOrder.getOrderId());
int finalQuantity = sellOrder.getQuantity();
int quantityDifference = initialQuantity - finalQuantity;
if (quantityDifference > 0) {
  Member seller = memberService.findById(sellOrder.getSellerMemId());
  int pointsToAdd = sellOrder.getAmount() * quantityDifference;
  seller.setPoints(seller.getPoints() + pointsToAdd);
  memberService.save(seller);
}
}

// 3. 根據 propId 及 memId 雙主鍵增加買家倉庫的 propQuantity
memberPropInventoryService.findMemberPropByIdAndPlusQuantity(memId, propId, quantity);

// 4. 新增買單
propBuyOrderService2.savePropBuyOrder(memId, quantity, paymentId, price, propId);

// 5. 扣除買家點數
Member buyer = memberService.findById(memId);
buyer.setPoints(buyer.getPoints() - price);
memberService.save(buyer);
memberService.setLoginDto(buyer);

return "購買完成";
}
	//根據memId找memName
	@GetMapping("/prop/front/buyProp/findAllByMemId")
	@ResponseBody
	public Member findAllByMemId(@RequestParam("memId") Integer memId) {
		return memberService.findById(memId);
	}
}
