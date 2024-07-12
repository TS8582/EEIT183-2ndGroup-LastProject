package com.playcentric.model.playfellow;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PfGameRepository extends JpaRepository<PfGame, Integer> {

    List<PfGame> findByPlayFellowMemberPlayFellowId(Integer playFellowId);//用伴遊Id去查pfgame
    
    Optional<PfGame> findByPlayFellowMemberPlayFellowIdAndGameGameId(Integer playFellowId, Integer gameId);

}
