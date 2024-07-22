package com.playcentric.model.prop.sellOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PropSellOrderForMarketDto {
    private int propId;
    private double amount;
    private int quantity;

}
