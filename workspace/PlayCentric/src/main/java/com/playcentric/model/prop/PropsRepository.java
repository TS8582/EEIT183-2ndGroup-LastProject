package com.playcentric.model.prop;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropsRepository extends JpaRepository<Props, Integer> {
    @Query("SELECT p FROM Props p WHERE p.game.gameId = :gameId")
    List<Props> findPropsByGameId(@Param("gameId") int gameId);

    
    
}
