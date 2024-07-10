package com.playcentric.model.prop.MemberPropInventory;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class MemberPropInventoryId implements Serializable {
	
    private int propId;
    private int memId;
    
	@Override
	public int hashCode() {
		return Objects.hash(memId, propId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberPropInventoryId other = (MemberPropInventoryId) obj;
		return memId == other.memId && propId == other.propId;
	}
    
    

}
