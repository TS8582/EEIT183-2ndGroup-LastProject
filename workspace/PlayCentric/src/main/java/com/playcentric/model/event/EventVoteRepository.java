package com.playcentric.model.event;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventVoteRepository extends JpaRepository<EventVote, Integer> {
    /**
     * 根據活動ID查找所有投票
     * @param eventId 活動ID
     * @return 該活動的所有投票列表
     */
    List<EventVote> findByEvent_EventId(Integer eventId);

    /**
     * 計算特定會員在特定活動中的投票次數
     * @param memId 會員ID
     * @param eventId 活動ID
     * @return 投票次數
     */
    long countByMember_MemIdAndEvent_EventId(Integer memId, Integer eventId);

    /**
     * 檢查特定會員是否已經為特定報名投票
     * @param memId 會員ID
     * @param signupId 報名ID
     * @return 如果已經投票則返回true，否則返回false
     */
    boolean existsByMember_MemIdAndEventSignup_SignupId(Integer memId, Integer signupId);

    /**
     * 計算特定報名的投票數量
     * @param signupId 報名ID
     * @return 投票數量
     */
    long countByEventSignup_SignupId(Integer signupId);
}