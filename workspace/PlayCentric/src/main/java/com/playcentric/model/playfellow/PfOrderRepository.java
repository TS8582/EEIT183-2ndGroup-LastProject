package com.playcentric.model.playfellow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.playcentric.model.member.Member;

public interface PfOrderRepository extends JpaRepository<PfOrder, Integer> {

	List<PfOrder> findByMember(Member member);

	@Query(value = "SELECT o.* FROM pfOrder o " + 
	"JOIN pfGame g ON o.pfGameId = g.pfGameId "	+ 
			"JOIN playFellowMember pfm ON g.playFellowId = pfm.playFellowId "	+ 
	"JOIN members m ON pfm.memId = m.memId " + 
			"WHERE pfm.memId = :memId", 
			nativeQuery = true)
	List<PfOrder> findOrdersByMember(@Param("memId") Integer memId);

	
	
	
	

}


