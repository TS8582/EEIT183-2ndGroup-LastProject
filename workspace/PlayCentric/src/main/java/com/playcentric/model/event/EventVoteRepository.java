package com.playcentric.model.event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventVoteRepository extends JpaRepository<EventVote, Integer> {
    List<EventVote> findByEvent_EventId(Integer eventId);
    long countByMember_MemIdAndEvent_EventId(Integer memId, Integer eventId);
    boolean existsByMember_MemIdAndEventSignup_SignupId(Integer memId, Integer signupId);
    long countByEventSignup_SignupId(Integer signupId);
}