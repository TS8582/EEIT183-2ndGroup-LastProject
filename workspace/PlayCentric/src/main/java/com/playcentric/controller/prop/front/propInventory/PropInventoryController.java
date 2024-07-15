package com.playcentric.controller.prop.front.propInventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryForFrontDto;
import com.playcentric.service.prop.MemberPropInventoryService.MemberPropInventoryService;

import jakarta.websocket.Session;



@Controller
public class PropInventoryController {
	
	@Autowired
	MemberPropInventoryService memberPropInventoryService;
	
	
	
	//跟會員id讀取倉庫資料
	@GetMapping("prop/memberPropsbyMemId")
	@ResponseBody
	public List<MemberPropInventoryForFrontDto> memberPropsbyMemId(int memId) {
		return memberPropInventoryService.memberPropsbyMemId(memId);		 
	}

}
