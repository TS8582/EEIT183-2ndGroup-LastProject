package com.playcentric.model.forum;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Integer> {

	@Query("FROM Talk")
	Page<Talk> findLatest(Pageable pgb);
	
	// 查詢文章底下留言
		  @Query("FROM Talk WHERE texts.textsId = :textsId")
		    List<Talk> findTalkBytextsId(@Param("textsId") Integer textsId);
}
