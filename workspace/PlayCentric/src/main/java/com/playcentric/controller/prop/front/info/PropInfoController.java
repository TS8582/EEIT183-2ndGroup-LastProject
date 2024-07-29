package com.playcentric.controller.prop.front.info;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
    // 回傳買單資訊
    @GetMapping("prop/front/propInfo")
    @ResponseBody
    public Page<PropBuyOrder2> findAllBuyPropsByMemId(
            @RequestParam("memId") int memId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return propBuyOrderService2.findOrdersByMemId(memId, pageable);
    }
}
