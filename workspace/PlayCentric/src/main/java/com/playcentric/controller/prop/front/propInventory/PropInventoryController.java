package com.playcentric.controller.prop.front.propInventory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PropInventoryController {
	//讀取會員倉庫資料
	@GetMapping("prop/propInventorybyMemId")
	public String showMarketPage() {
		return "prop/front/propInventory";
	}

}
