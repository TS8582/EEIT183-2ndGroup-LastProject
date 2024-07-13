package com.playcentric.controller.prop.front.propInventory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PropInventoryController {
	// 進入倉庫頁面
	@GetMapping("prop/front/propInventory")
	public String showMarketPage() {
		return "prop/front/propInventory";
	}

}
