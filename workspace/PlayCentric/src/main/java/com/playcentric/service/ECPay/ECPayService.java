package com.playcentric.service.ECPay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.playcentric.config.NgrokConfig;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
@PropertySource("ecPay.properties")
public class ECPayService {

    @Autowired
    private NgrokConfig ngrokConfig;

    @Value("${ecpay.return_url}")
    private String returnUrl;

    @Value("${ecpay.client_back_url}")
    private String clientBackUrl;

    @Value("${ecpay.trade_desc}")
    private String tradeDesc;

    @Value("${ecpay.item_name}")
    private String itemName;

    @Value("${ecpay.merchant_trade_date_format}")
    private String merchantTradeDateFormat;

    @Value("${ecpay.hash_key}")
    private static String hashKey;

    @Value("${ecpay.hash_iv}")
    private static String hashIV;

    private final AllInOne allInOne;

    public ECPayService() {
        // 初始化 AllInOne 物件
        this.allInOne = new AllInOne("");
    }

    public String ecpayCheckout(String totalAmount) {
        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo(uuId);

        // 動態生成 MerchantTradeDate
        SimpleDateFormat sdf = new SimpleDateFormat(merchantTradeDateFormat);
        String currentDate = sdf.format(new Date());
        obj.setMerchantTradeDate(currentDate);

        obj.setTotalAmount(totalAmount);
        obj.setTradeDesc(tradeDesc);
        obj.setItemName(itemName);
        obj.setReturnURL(ngrokConfig.getUrl()+returnUrl);
        obj.setClientBackURL(ngrokConfig.getUrl()+clientBackUrl);
        obj.setNeedExtraPaidInfo("N");

        String form = allInOne.aioCheckOut(obj, null);

        return form;
    }

    public boolean checkReturn(Map<String, String> params){
        return true;
    }
}
