package com.playcentric.model.game.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentDao {
	
	private Integer paymentId;
	private String paymentName;

	public PaymentDao(Payment payment) {
	    if (payment != null) {
	        this.paymentId = payment.getPaymentId();
	        this.paymentName = payment.getPaymentName();
	    }
	}
}
