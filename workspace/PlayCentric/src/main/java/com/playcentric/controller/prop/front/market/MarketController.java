package com.playcentric.controller.prop.front.market;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class MarketController {

	// 進入市場頁面
	@GetMapping("prop/front/market")
	public String showMarketPage() {
		return "prop/front/market";
	}
}
