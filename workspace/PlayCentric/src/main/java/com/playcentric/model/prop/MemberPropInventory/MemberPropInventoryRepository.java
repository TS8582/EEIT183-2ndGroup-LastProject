package com.playcentric.model.prop.MemberPropInventory;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.playcentric.model.member.Member;
import com.playcentric.model.prop.Props;
public interface MemberPropInventoryRepository extends JpaRepository<MemberPropInventory, MemberPropInventoryId > {
    List<MemberPropInventory> findByIdMemId(int memId);
}
