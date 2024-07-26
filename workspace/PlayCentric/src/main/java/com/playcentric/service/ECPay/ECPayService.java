package com.playcentric.service.ECPay;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.playcentric.config.NgrokConfig;
import com.playcentric.model.game.transaction.Recharge;
import com.playcentric.model.game.transaction.RechargeRepository;
import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.ecpayOperator.EcpayFunction;

@Service
@PropertySource("ecPay.properties")
public class ECPayService {

    @Autowired
    private NgrokConfig ngrokConfig;

    @Value("${recharge.return_url}")
    private String rechargeReturnUrl;

    @Value("${recharge.client_back_url}")
    private String rechargeClientBackUrl;

    @Value("${recharge.trade_desc}")
    private String rechargeTradeDesc;

    @Value("${recharge.item_name}")
    private String rechargeItemName;

    @Value("${ecpay.merchant_trade_date_format}")
    private String merchantTradeDateFormat;

    @Value("${ecpay.hash_key}")
    private String hashKey;

    @Value("${ecpay.hash_iv}")
    private String hashIV;

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private final AllInOne allInOne;

    public ECPayService() {
        // 初始化 AllInOne 物件
        this.allInOne = new AllInOne("");
    }
    
    public Recharge saveRecharge(Recharge recharge) {
		return rechargeRepository.save(recharge);
	}

    public String rechargePoints(Recharge recharge) {
        // String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0,
        // 20);
        AioCheckOutALL obj = new AioCheckOutALL();
        SimpleDateFormat sdf = new SimpleDateFormat(merchantTradeDateFormat);
        String currentDate = sdf.format(new Date());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(merchantTradeDateFormat);
        LocalDateTime rechargeAt = LocalDateTime.parse(currentDate, dtf);
        recharge.setRechargeAt(rechargeAt);
        recharge.setStatus((short)0);
        recharge = rechargeRepository.save(recharge);

        String tradeNo = "PlayCentric" + "Point" + recharge.getRechargeId();
        obj.setMerchantTradeNo(tradeNo);
        obj.setMerchantTradeDate(currentDate);
        obj.setTotalAmount(recharge.getAmount().toString());
        obj.setTradeDesc(rechargeTradeDesc);
        obj.setItemName(rechargeItemName);
        obj.setReturnURL(ngrokConfig.getUrl() + rechargeReturnUrl);
        obj.setClientBackURL(ngrokConfig.getUrl() + rechargeClientBackUrl);
        obj.setNeedExtraPaidInfo("N");

        String form = allInOne.aioCheckOut(obj, null);

        return form;
    }

    public boolean checkRechargeReturn(Map<String, String> params) {
        boolean result = false;
        String merchantTradeNo = params.get("MerchantTradeNo");
        Integer rechargeId = Integer.parseInt(merchantTradeNo.substring(merchantTradeNo.lastIndexOf("Point") + 5));
        System.err.println(rechargeId);
        Optional<Recharge> optional = rechargeRepository.findById(rechargeId);
        if (optional.isPresent()) {
            Recharge recharge = optional.get();
            recharge.setStatus((short)2);

            String tradeDate = params.get("TradeDate");
            System.err.println(tradeDate);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(merchantTradeDateFormat);
            LocalDateTime rechargeAt = LocalDateTime.parse(tradeDate, dtf);
            result = recharge.getRechargeAt().equals(rechargeAt);

            String tradeAmt = params.get("TradeAmt");
            System.err.println(tradeAmt);
            result = recharge.getAmount().equals(Integer.parseInt(tradeAmt));

            if (result) {
                Optional<Member> optionalMem = memberRepository.findById(recharge.getMemId());
                if ((result = optionalMem.isPresent())) {
                    recharge.setStatus((short)1);
                    Member member = optionalMem.get();
                    member.setPoints(member.getPoints()+recharge.getAmount());
                    result = memberRepository.save(member)!=null && rechargeRepository.save(recharge)!=null;
                }
            }
        }
        return result;
    }

    public boolean validateCheckMacValue(Map<String, String> params) throws Exception {
        String checkMacValue = EcpayFunction.genCheckMacValue(hashKey, hashIV, new Hashtable<>(params));
        String rtnCheckValue = params.get("CheckMacValue");
        if (checkMacValue.equals(rtnCheckValue)) {
            return true;
        }
        return false;
    }

    public String getRechargeResult(Integer memId){
		PageRequest pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "rechargeAt");
        Recharge recharge = rechargeRepository.findByMemId(memId, pageable);
        recharge.setStatus((short)1);

        recharge.setStatus(recharge.getStatus()==2? (short)2:(short)1);

        return recharge.getStatus()==1? "儲值成功!":"儲值失敗!";
    }

    public Page<Recharge> getMemRechargePage(Integer memId, Integer pageNum){
        PageRequest pageable = PageRequest.of(pageNum - 1, 10, Sort.Direction.DESC, "rechargeAt");
		return rechargeRepository.findByMemIdAndStatus(memId,(short)1, pageable);
    }
}
