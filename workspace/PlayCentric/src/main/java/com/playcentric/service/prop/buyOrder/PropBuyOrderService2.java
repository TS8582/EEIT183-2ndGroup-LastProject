package com.playcentric.service.prop.buyOrder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.playcentric.model.prop.Props;
import com.playcentric.model.prop.PropsRepository;
import com.playcentric.model.prop.buyOrder2.PropBuyOrder2;
import com.playcentric.model.prop.buyOrder2.PropBuyOrderRepository2;

@Service
public class PropBuyOrderService2 {
    @Autowired
    private PropBuyOrderRepository2 propBuyOrderRepo;
    @Autowired
    private PropsRepository propsRepo;
	private Optional<Props> optional;

//    新增買單
    public String savePropBuyOrder(Integer buyerMemId, Integer quantity, Integer paymentId, Integer price,Integer propId) {
        PropBuyOrder2 propBuyOrder = new PropBuyOrder2();
        propBuyOrder.setBuyerMemId(buyerMemId);
        propBuyOrder.setQuantity(quantity);
        LocalDateTime orderTime = LocalDateTime.now();
        propBuyOrder.setOrderTime(orderTime);
        propBuyOrder.setPaymentId(paymentId);
        propBuyOrder.setPrice(price);
        optional = propsRepo.findById(propId);        
        propBuyOrder.setProps(optional.get());
        propBuyOrderRepo.save(propBuyOrder);
        return "買單儲存成功";
    }
//    查詢所有買單
	public List<PropBuyOrder2> findPropBuyOrders(int gameId) {
		List<PropBuyOrder2> buyOrders = propBuyOrderRepo.findAllByGameId(gameId);
		return buyOrders;
	}
	
//	根據memId查詢所有買單
    public Page<PropBuyOrder2> findOrdersByMemId(int memId,Pageable  pageable) {
        return propBuyOrderRepo.findAllByMemId(memId, pageable);
    }
}
