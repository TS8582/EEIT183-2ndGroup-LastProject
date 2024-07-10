package com.playcentric.model.prop.type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropTypeRepository extends JpaRepository<PropType, Integer> {
    List<PropType> findAllByGameId(int gameId);

}
