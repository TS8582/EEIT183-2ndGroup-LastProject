package com.playcentric.controller.prop.front.propsRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PropsRepositoryController {
    // 進入市場頁面
    @GetMapping("prop/front/market")
    public String showMarketPage() {
        return "prop/front/market";
    }

}
