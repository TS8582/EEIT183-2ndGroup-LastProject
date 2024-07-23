package com.playcentric.controller.prop.front.info;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.playcentric.model.prop.buyOrder2.PropBuyOrder2;
import com.playcentric.service.prop.buyOrder.PropBuyOrderService2;




@Controller
public class PropInfoController {
	
	@Autowired
	PropBuyOrderService2 propBuyOrderService2;
	
	// 進入拍賣info頁面
	@GetMapping("prop/front/propInfoPage")
	public String showInfoPage() {
		return "prop/front/propInfoPage";
	}
	
	//回傳買單資訊
	@GetMapping("prop/front/propInfo")
	@ResponseBody
	public List<PropBuyOrder2> findAllSellPropsByMemId(int buyerMemId){
		return propBuyOrderService2.findAllByMemId(buyerMemId);
	}
}
