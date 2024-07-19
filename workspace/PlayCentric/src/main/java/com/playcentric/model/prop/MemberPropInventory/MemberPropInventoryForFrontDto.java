package com.playcentric.model.prop.MemberPropInventory;

import com.playcentric.model.prop.Props;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MemberPropInventoryForFrontDto {
    private Props prop;
    private int memId;
    private int propQuantity;
}
