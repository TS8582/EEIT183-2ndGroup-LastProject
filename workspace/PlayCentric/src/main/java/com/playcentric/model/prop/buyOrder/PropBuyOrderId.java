package com.playcentric.model.prop.buyOrder;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class PropBuyOrderId implements Serializable{
	
    private int orderId;
    private int buyerMemId;
	@Override
	public int hashCode() {
		return Objects.hash(buyerMemId, orderId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropBuyOrderId other = (PropBuyOrderId) obj;
		return buyerMemId == other.buyerMemId && orderId == other.orderId;
	}
    
}
