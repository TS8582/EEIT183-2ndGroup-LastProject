package com.playcentric.model.event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventVoteRepository extends JpaRepository<EventVote, Integer> {
    List<EventVote> findByEventSignup_SignupId(Integer signupId);
    boolean existsByMember_MemIdAndEventSignup_SignupId(Integer memberId, Integer signupId);
}