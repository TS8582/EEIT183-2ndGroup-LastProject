package com.playcentric.model.event;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventSignupRepository extends JpaRepository<EventSignup, Integer> {
    /**
     * 根據活動ID查找所有報名
     * @param eventId 活動ID
     * @return 該活動的所有報名列表
     */
    List<EventSignup> findByEvent_EventId(Integer eventId);

    /**
     * 根據會員ID查找所有報名
     * @param memberId 會員ID
     * @return 該會員的所有報名列表
     */
    List<EventSignup> findByMember_MemId(Integer memberId);
}