package com.playcentric.model.event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventVoteRepository extends JpaRepository<EventVote, Integer> {
    
}