package com.playcentric.controller.prop.front.market;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.playcentric.model.prop.sellOrder.PropSellOrderDto;
import com.playcentric.model.prop.sellOrder.PropSellOrderForMarketDto;
import com.playcentric.service.prop.sellOrder.PropSellOrderService;

@Controller
public class MarketController {

	@Autowired
	private PropSellOrderService propSellOrderService;

	// 進入市場頁面
	@GetMapping("prop/front/market")
	public String showMarketPage() {
		return "prop/front/market";
	}

	// 根據 propId及排除當前loginMemId 找賣單後以價格為區間顯示
	@GetMapping("prop/front/market/orders")
	@ResponseBody
	public List<PropSellOrderForMarketDto> findByPropId(@RequestParam("propId") Integer propId,@RequestParam("loginMemId") Integer loginMemId) {
		return propSellOrderService.findByPropId(propId,loginMemId);
	}

	// 根據gameId及memId及Order=0(販賣中)找所有賣單
	@GetMapping("prop/front/market/selloders")
	@ResponseBody
	public List<PropSellOrderDto> findAllByGameIdAndMemIdAndOrderStatus(@RequestParam("memId") Integer memId,
			@RequestParam("gameId") Integer gameId) {
		return propSellOrderService.findAllByGameIdAndMemIdAndOrderStatus(memId, gameId);
	
	}
	//根據propId及status為0找尋(販售中)賣單的平均價格'
	@GetMapping("prop/front/market/averageAmountByPropId")
	@ResponseBody
	public Map calculateAverageAmountByPropId(@RequestParam("propId") Integer propId){
	    return propSellOrderService.calculateAverageAmountByPropId(propId);
	}
}
