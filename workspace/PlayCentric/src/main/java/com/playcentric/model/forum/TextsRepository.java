package com.playcentric.model.forum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TextsRepository extends JpaRepository<Texts, Integer> {

	@Query("FROM Texts t WHERE t.title LIKE %:keyword%")
	List<Texts> findAllByTitleContaining(@Param("keyword") String keyword);

	// 是否顯示
	List<Texts> findAllByHideTexts(boolean hideTexts);

	// 修改是否封鎖
	@Modifying
	@Query("UPDATE Texts t SET t.hideTexts = :hideTexts WHERE t.textsId = :textsId")
	void updateHideTextsById(@Param("textsId") Integer textsId, @Param("hideTexts") boolean hideTexts);
}
