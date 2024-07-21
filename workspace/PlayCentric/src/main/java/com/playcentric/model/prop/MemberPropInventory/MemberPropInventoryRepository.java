package com.playcentric.model.prop.MemberPropInventory;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import com.playcentric.model.member.Member;
import com.playcentric.model.prop.Props;

import jakarta.persistence.QueryHint;
public interface MemberPropInventoryRepository extends JpaRepository<MemberPropInventory, MemberPropInventoryId > {
	List<MemberPropInventory> findByIdMemId(int memId);
    Optional<MemberPropInventory> findByIdMemIdAndIdPropId(int memId, int propId);

}
