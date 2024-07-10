package com.playcentric.model.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface MemberRepository extends JpaRepository<Member, Integer> {

	Member findByAccount(String account);
	
	Member findByEmail(String email);

	Member findByGoogeId(String googeId);

	Page<Member> findByStatus(Short status, Pageable pageable);

	@Query("from Member where status = 0 and (account like :keyword or nickname like :keyword or memName like :keyword)")
	Page<Member> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

	Page<Member> findByStatusAndAccountContainingOrNicknameContainingOrMemNameContainingOrEmailContaining(Short status, String account, String nickname, String memName, String email, Pageable pageable);

	
}
