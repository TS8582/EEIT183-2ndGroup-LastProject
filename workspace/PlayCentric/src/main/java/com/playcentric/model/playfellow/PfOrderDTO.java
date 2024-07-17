package com.playcentric.model.playfellow;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PfOrderDTO {

	private Integer pfGameId;
	private Integer memId;
	private String transactionID;
	private Integer paymentStatus;
	private Integer quantity;// 每單數量
	private Integer totalAmount;// 總金額

}
