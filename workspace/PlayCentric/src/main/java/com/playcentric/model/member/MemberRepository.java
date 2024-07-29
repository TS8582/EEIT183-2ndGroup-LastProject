package com.playcentric.model.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;







public interface MemberRepository extends JpaRepository<Member, Integer> {

	Member findByAccount(String account);
	
	Member findByAccountAndStatus(String account,Short status);

	Member findByLoginToken(String loginToken);

	Member findByPasswordToken(String passwordToken);

	Member findByEmailVerifyToken(String emailVerifyToken);
	
	Member findByEmail(String email);

	Member findByGoogeId(String googeId);

	Member findByAccountOrEmail(String account, String email);

	Page<Member> findByStatus(Short status, Pageable pageable);

	@Query("from Member where status = 0 and (lower(account) like :keyword or lower(nickname) like :keyword or lower(memName) like :keyword or lower(email) like :emailKey)")
	Page<Member> findByKeyword(@Param("keyword") String keyword,@Param("emailKey") String emailKey, Pageable pageable);
	
}
