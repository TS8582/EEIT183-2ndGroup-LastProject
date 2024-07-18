package com.playcentric.service.prop.buyOrder;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.playcentric.model.prop.buyOrder2.PropBuyOrder2;
import com.playcentric.model.prop.buyOrder2.PropBuyOrderRepository2;

@Service
public class PropBuyOrderService2 {
    @Autowired
    private PropBuyOrderRepository2 propBuyOrderRepo;
    
    public String savePropBuyOrder(Integer buyerMemId, Integer quantity, Integer paymentId, Integer price) {
        PropBuyOrder2 propBuyOrder = new PropBuyOrder2();
        propBuyOrder.setBuyerMemId(buyerMemId);
        propBuyOrder.setQuantity(quantity);
        LocalDateTime orderTime = LocalDateTime.now();
        propBuyOrder.setOrderTime(orderTime);
        propBuyOrder.setPaymentId(paymentId);
        propBuyOrder.setPrice(price);
        propBuyOrderRepo.save(propBuyOrder);
        
        return "買單儲存成功";
    }
}
