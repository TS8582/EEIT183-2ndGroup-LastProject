package com.playcentric.model.prop.MemberPropInventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class MemberPropInventoryDto {
    private int propId;
    private int memId;
    private int propQuantity;
    
    public MemberPropInventoryDto(MemberPropInventory memberPropInventory) {
        this.propId = memberPropInventory.getProps().getPropId();
        this.memId = memberPropInventory.getMember().getMemId();
        this.propQuantity = memberPropInventory.getPropQuantity();
    }
}
