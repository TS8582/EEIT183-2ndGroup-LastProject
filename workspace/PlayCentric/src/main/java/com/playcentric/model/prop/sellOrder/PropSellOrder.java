package com.playcentric.model.prop.sellOrder;

import java.time.LocalDateTime;

import com.playcentric.model.prop.MemberPropInventory.MemberPropInventory;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "propSellOrder")
public class PropSellOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private int propId;
    private int sellerMemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "propId", referencedColumnName = "propId", insertable = false, updatable = false),
        @JoinColumn(name = "sellerMemId", referencedColumnName = "memId", insertable = false, updatable = false)
    })
    private MemberPropInventory memberPropInventory;

    private int amount;
    private int quantity;
    private LocalDateTime saleTime;
    private LocalDateTime expiryTime;
    private byte orderStatus;
}
