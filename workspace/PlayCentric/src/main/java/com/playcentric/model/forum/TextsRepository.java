package com.playcentric.model.forum;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TextsRepository extends JpaRepository<Texts, Integer> {

	// 模糊查詢全部
	@Query("FROM Texts WHERE title LIKE %:title%")
	List<Texts> findAllByTitle(@Param("title") String title);

	// 是否顯示
	List<Texts> findAllByHideTexts(boolean hideTexts);

	// 分頁
	@Query("from Texts")
	Page<Texts> findLatest(Pageable pgb);

	// 尋找主題的文章
	@Query("FROM Texts m WHERE m.forum.forumId = :forumId")
	List<Texts> findTextsByForumId(@Param("forumId") Integer forumId);

	// 分页查询某主题下的文章
	@Query("FROM Texts t WHERE t.forum.forumId = :forumId")
	List<Texts> findTextsByForumId2(Integer forumId);

	Page<Texts> findByForumForumId(Integer forumId, Pageable pageable);

}
