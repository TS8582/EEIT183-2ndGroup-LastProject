package com.playcentric.model.playfellow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.playcentric.model.member.Member;


public interface PfOrderRepository extends JpaRepository<PfOrder, Integer> {

	List<PfOrder> findByMember(Member member);

}
