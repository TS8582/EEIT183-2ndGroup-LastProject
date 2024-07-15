package com.playcentric.model.playfellow;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PfGameRepository extends JpaRepository<PfGame, Integer> {

    List<PfGame> findByPlayFellowMemberPlayFellowId(Integer playFellowId); // 用伴遊Id去查pfgame
    
    @Query("SELECT pg FROM PfGame pg WHERE pg.game.id = :gameId")
    List<PfGame> findByGameId(@Param("gameId") Integer gameId);
    
    Optional<PfGame> findByPlayFellowMemberPlayFellowIdAndGameGameId(Integer playFellowId, Integer gameId);
    
//    @Query("SELECT pg FROM PfGame pg WHERE pg.game.gameId = :gameId")
//    List<PfGame> findAllByGameId(@Param("gameId") Integer gameId);
}
