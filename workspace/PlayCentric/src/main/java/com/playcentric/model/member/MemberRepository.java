package com.playcentric.model.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;





public interface MemberRepository extends JpaRepository<Member, Integer> {

	Member findByAccount(String account);
	
	Member findByEmail(String email);

	Member findByGoogeId(String googeId);

	Page<Member> findByStatus(Short status, Pageable pageable);
}
