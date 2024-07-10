package com.playcentric.model.prop.buyOrder;

import java.time.LocalDateTime;
import java.util.Date;
import com.playcentric.model.game.transaction.Payment;
import com.playcentric.model.member.Member;
import com.playcentric.model.prop.sellOrder.PropSellOrder;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "propBuyOrder")
public class PropBuyOrder {

    @EmbeddedId
    private PropBuyOrderId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "orderId", referencedColumnName = "orderId", insertable = false, updatable = false)
    private PropSellOrder propSellOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("buyerMemId")
    @JoinColumn(name = "buyerMemId", referencedColumnName = "memId", insertable = false, updatable = false)
    private Member member;
    private int quantity;
    private LocalDateTime orderTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentId", referencedColumnName = "paymentId", insertable = false, updatable = false)
    private Payment payment;
    
    private int price;

}
