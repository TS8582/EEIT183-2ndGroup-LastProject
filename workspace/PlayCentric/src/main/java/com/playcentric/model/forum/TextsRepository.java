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
	@Query("FROM Texts WHERE title LIKE %:title% and hideTexts = false")
	List<Texts> findAllByTitle(@Param("title") String title);

	// 是否顯示
	@Query("FROM Texts WHERE hideTexts = false")
	List<Texts> findAllByHideTexts();

	// 分頁
	@Query("FROM Texts WHERE hideTexts = false")
	Page<Texts> findLatest(Pageable pgb);

	@Query("FROM Texts t WHERE t.textsId = :textsId and t.hideTexts = false")
	List<Texts> findByTextsId(@Param("textsId") Integer textsId);

	// 尋找主題的文章
	@Query("FROM Texts t WHERE t.forum.forumId = :forumId and t.hideTexts = false")
	List<Texts> findTextsByForumId(@Param("forumId") Integer forumId);

	// 分页查询某主题下的文章
	@Query("FROM Texts t WHERE t.forum.forumId = :forumId and t.hideTexts = false")
	Page<Texts> findByForumForumId(@Param("forumId") Integer forumId, Pageable pageable);

	@Query("from Texts where hideTexts = false")
	Page<Texts> findAllPageByTexts(Pageable pageable);

	// 判定資料庫是否有此文章
	Texts findByTitle(String title);
}
