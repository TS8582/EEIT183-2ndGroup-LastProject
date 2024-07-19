package com.playcentric.controller.prop.front.market;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    
    // 根據 propId 找賣單後顯示最便宜 20 筆
    @GetMapping("prop/front/market/orders")
    @ResponseBody
    public List<PropSellOrderForMarketDto> findByPropId(@RequestParam("propId") Integer propId) {
        return propSellOrderService.findByPropId(propId);
    }
}
