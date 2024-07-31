package com.playcentric.model.forum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;



public interface PhotoRepository extends JpaRepository<ForumPhoto, Integer> {
	
	List<ForumPhoto> findByTexts(Texts texts);

	@Transactional
	void deleteByTexts(Texts texts);

}
