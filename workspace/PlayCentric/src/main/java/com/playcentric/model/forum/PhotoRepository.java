package com.playcentric.model.forum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<ForumPhoto, Integer> {
	
	List<ForumPhoto> findByTextsId(Integer textsId);

}
