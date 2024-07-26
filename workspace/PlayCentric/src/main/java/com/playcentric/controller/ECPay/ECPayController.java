package com.playcentric.controller.ECPay;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.model.game.transaction.Recharge;
import com.playcentric.model.game.transaction.RechargeRepository;
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
    private MemberService mService;
    @Autowired
    private RechargeRepository rechargeRepository;

    @PostMapping("/member/personal/api/newRecharge")
    @ResponseBody
    public String startOrder(@RequestParam Integer rechargeAmount, Model model) {
        LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
        if (loginMember == null) {
            return "請重新登入";
        }

        Recharge recharge = new Recharge();
        recharge.setMemId(loginMember.getMemId());
        recharge.setPaymentId(2);
        recharge.setAmount(rechargeAmount);
        Member member = mService.findById(loginMember.getMemId());
        recharge.setMember(member);
        recharge.setMemId(member.getMemId());
        
        ecPayService.saveRecharge(recharge);
        String aioCheckOutALLForm = ecPayService.rechargePoints(recharge);

        System.err.println("儲值" + rechargeAmount + "元");

        return aioCheckOutALLForm;
    }

    @PostMapping("/recharge/ecPayReturn")
    @ResponseBody
    public String rechargeReturn(@RequestParam Map<String, String> params) {
        try {
            System.err.println("EC Pay 成功回傳");
            // 確認加密
            if (ecPayService.validateCheckMacValue(params)) {
                // 確認訂單完成並存在
                String rtnCode = params.get("RtnCode");
                if ("1".equals(rtnCode) && ecPayService.checkRechargeReturn(params)) {
                    System.err.println("儲值成功!");
                    return "1|OK"; // 回應給 ECPay 表示成功接收
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("儲值失敗!");
        return "0|Error";
    }

    @GetMapping("/personal/ecPayOK")
    public String backFromEcPay(RedirectAttributes redirectAttributes, Model model) {
        LoginMemDto loginMember = (LoginMemDto)model.getAttribute("loginMember");
        PageRequest pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "rechargeAt");
        Recharge recharge = rechargeRepository.findByMemId(loginMember.getMemId(), pageable);
        String rechargeResult = "儲值完成!";
        if (loginMember!=null) {
            rechargeResult = ecPayService.getRechargeResult(loginMember.getMemId());
            Member member = mService.findById(loginMember.getMemId());
            member.setPoints(member.getPoints() + recharge.getAmount());
            mService.save(member);
        }
        redirectAttributes.addFlashAttribute("redirectMsg", rechargeResult);
        return "redirect:/member/personal/Info";
    }

    @GetMapping("/personal/api/rechargeHis")
    @ResponseBody
    public Page<Recharge> getMethodName(@RequestParam Integer pageNum,@ModelAttribute("loginMember") LoginMemDto loginMember) {
        return ecPayService.getMemRechargePage(loginMember.getMemId(), pageNum);
    }
    
}
