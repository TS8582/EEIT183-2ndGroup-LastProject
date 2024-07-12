package com.playcentric.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageLibRepository extends JpaRepository<ImageLib, Integer> {

//	// 討論區使用
//	List<ImageLib> findByTextsId(Integer textsId);

}
