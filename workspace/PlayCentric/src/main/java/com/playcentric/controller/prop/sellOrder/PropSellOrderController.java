package com.playcentric.controller.prop.sellOrder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.ImageLib;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.prop.Props;
import com.playcentric.model.prop.sellOrder.PropSellOrderDto;
import com.playcentric.model.prop.type.PropType;
import com.playcentric.service.game.GameService;
import com.playcentric.service.prop.PropService;
import com.playcentric.service.prop.sellOrder.PropSellOrderService;
import com.playcentric.service.prop.type.PropTypeService;
import com.playcentric.service.ImageLibService;

@Controller
@SessionAttributes(names = "games")
public class PropSellOrderController {

	@Autowired
	PropSellOrderService propSellOrderService;

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

}
