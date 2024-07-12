package com.playcentric.model.forum;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.playcentric.model.game.primary.Game;


@Repository
public interface ForumRepository extends JpaRepository<Forum, Integer> {

//	Optional<Forum> findByGame(Game game);
	
	// 模糊查詢全部
		 @Query("from Forum where forumName like %:forumName%")
		    List<Forum> findAllByForumName(@Param(value = "forumName") String name);
}
