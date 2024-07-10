package com.playcentric.model.forum;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.playcentric.model.game.primary.Game;


@Repository
public interface ForumRepository extends JpaRepository<Forum, Integer> {

	Optional<Forum> findByGame(Game game);
	
	List<Forum> findByTextsIntroContaining(String keyword);
}
