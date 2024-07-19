package com.playcentric.controller.ECPay;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.model.game.transaction.Recharge;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.service.ECPay.ECPayService;
import com.playcentric.service.member.MemberService;



@Controller
@SessionAttributes(names = { "loginMember" })
public class ECPayController {
    
    @Autowired
    private ECPayService ecPayService;

    @Autowired
    private MemberService memberService;

    @PostMapping("/ecpayCheckout")
    @ResponseBody
	public String startOrder(@RequestParam Integer rechargeAmount, Model model) {
        LoginMemDto loginMember = (LoginMemDto)model.getAttribute("loginMember");
        if(loginMember==null){
            return "請重新登入";
        }

        Recharge recharge = new Recharge();
        Member member = memberService.findById(loginMember.getMemId());
        recharge.setMember(member);
        recharge.setAmount(rechargeAmount);

        String aioCheckOutALLForm = ecPayService.rechargePoints(recharge);

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
