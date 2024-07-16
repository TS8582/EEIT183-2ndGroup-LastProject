package com.playcentric.model.event;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventSignupRepository extends JpaRepository<EventSignup, Integer> {
    List<EventSignup> findByEvent_EventId(Integer eventId);
    List<EventSignup> findByMember_MemId(Integer memberId);
}