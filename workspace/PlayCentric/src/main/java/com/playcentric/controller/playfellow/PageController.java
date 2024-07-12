package com.playcentric.controller.playfellow;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

	@GetMapping("/playFellow/test")
	public String playFellow() {
		return "playFellow/items";
	}
}
