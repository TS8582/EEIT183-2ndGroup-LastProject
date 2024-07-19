package com.playcentric.service.ECPay;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.playcentric.config.NgrokConfig;
import com.playcentric.model.game.transaction.Recharge;
import com.playcentric.model.game.transaction.RechargeRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
@PropertySource("ecPay.properties")
public class ECPayService {

    @Autowired
    private NgrokConfig ngrokConfig;

    @Value("${ecpay.recharge_return_url}")
    private String rechargeReturnUrl;

    @Value("${ecpay.recharge_client_back_url}")
    private String rechargeClientBackUrl;

    @Value("${ecpay.recharge_trade_desc}")
    private String rechargeTradeDesc;

    @Value("${ecpay.recharge_item_name}")
    private String rechargeItemName;

    @Value("${ecpay.merchant_trade_date_format}")
    private String merchantTradeDateFormat;

    @Value("${ecpay.hash_key}")
    private String hashKey;

    @Value("${ecpay.hash_iv}")
    private String hashIV;

    @Autowired
    private RechargeRepository rechargeRepository;

    private final AllInOne allInOne;

    public ECPayService() {
        // 初始化 AllInOne 物件
        this.allInOne = new AllInOne("");
    }

    public String rechargePoints(Recharge recharge) {
        // String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
        AioCheckOutALL obj = new AioCheckOutALL();
        SimpleDateFormat sdf = new SimpleDateFormat(merchantTradeDateFormat);
        Date nowDate = new Date();
        String currentDate = sdf.format(nowDate);

        LocalDateTime rechargeAt = nowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        recharge.setRechargeAt(rechargeAt);
        recharge = rechargeRepository.save(recharge);

        String tradeNo = "PlayCentric" + currentDate + "Points" + recharge.getRechargeId();

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

    public boolean checkReturn(Map<String, String> params) {
        return true;
    }
}
