package com.playcentric.model.prop.sellOrder;

import java.time.format.DateTimeFormatter;

import com.playcentric.model.prop.MemberPropInventory.MemberPropInventoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PropSellOrderDto {
    private int orderId;
    private int propId;
    private int sellerMemId;
    private MemberPropInventoryDto memberPropInventoryDto;
    private int amount;
    private int quantity;
    private String saleTime;
    private String expiryTime;
    private byte orderStatus;

    public PropSellOrderDto(PropSellOrder propSellOrder) {
        this.orderId = propSellOrder.getOrderId();
        this.propId = propSellOrder.getPropId();
        this.sellerMemId = propSellOrder.getSellerMemId();
        this.memberPropInventoryDto = new MemberPropInventoryDto(propSellOrder.getMemberPropInventory());
        this.amount = propSellOrder.getAmount();
        this.quantity = propSellOrder.getQuantity();
        this.saleTime = propSellOrder.getSaleTime().format(DateTimeFormatter.ISO_DATE_TIME);
        this.expiryTime = propSellOrder.getExpiryTime().format(DateTimeFormatter.ISO_DATE_TIME);
        this.orderStatus = propSellOrder.getOrderStatus();
    }
}
