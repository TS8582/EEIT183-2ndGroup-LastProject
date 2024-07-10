package com.playcentric.model.prop.MemberPropInventory;

import com.playcentric.model.member.Member;
import com.playcentric.model.prop.Props;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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
@Table(name = "memberPropInventory")
public class MemberPropInventory {
	
    @EmbeddedId
    private MemberPropInventoryId id;

//    FK props
    @ManyToOne
    @MapsId("propId")
    @JoinColumn(name = "propId")
    private Props props;
	
//    FK Member
    @ManyToOne
    @MapsId("memId")
    @JoinColumn(name = "memId")
    private Member member;
    
    
    private int propQuantity;

	public MemberPropInventory(MemberPropInventoryId id, Props props, int propQuantity) {
		super();
		this.id = id;
		this.props = props;
		this.propQuantity = propQuantity;
	}
    
    
    
    
}
