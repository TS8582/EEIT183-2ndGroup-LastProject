package com.playcentric.controller.ECPay;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.service.ECPay.ECPayService;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class ECPayController {
    
    @Autowired
    private ECPayService ecPayService;

    @PostMapping("/ecpayCheckout")
    @ResponseBody
	public String startOrder(@RequestParam String rechargeAmount) {
        String aioCheckOutALLForm = ecPayService.ecPayDoOrder(rechargeAmount);

        System.err.println("儲值"+rechargeAmount+"元");
        
		return aioCheckOutALLForm;
	}
    
    @PostMapping("/ecPayReturn")
    @ResponseBody
    public String orderFinish(@RequestParam Map<String, String> params) {
        try {
            ecPayService.checkReturn(params);
            System.err.println("EC Pay 成功回傳");
            return "1|OK";
        } catch (Exception e) {
            e.printStackTrace();
            return "0|Error";
        }
    }

    @GetMapping("/ecPayOK")
    public String backFromEcPay(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("redirectMsg","儲值成功!");
        return "redirect:/member/memInfo";
    }
    
    
}
