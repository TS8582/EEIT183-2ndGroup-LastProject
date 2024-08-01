package com.playcentric.service.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventSignup;
import com.playcentric.model.event.EventSignupRepository;
import com.playcentric.model.event.EventVote;
import com.playcentric.model.event.EventVoteRepository;
import com.playcentric.model.member.MemberRepository;

/**
 * 活動投票服務類
 * 處理與活動投票相關的業務邏輯
 */
@Service
public class EventVoteService {

    @Autowired
    private EventVoteRepository eventVoteRepository;

    @Autowired
    private EventSignupRepository eventSignupRepository;

    @Autowired
    private MemberRepository memberRepository;

    // ======== 投票管理相關方法 ========

    /**
     * 創建新的投票，包含投票限制和時間限制檢查
     * @param memberId 會員ID
     * @param signupId 報名ID
     * @return 創建的投票記錄
     * @throws RuntimeException 如果不滿足投票條件
     */
    @Transactional
    public EventVote createVote(Integer memberId, Integer signupId) {
        EventSignup eventSignup = eventSignupRepository.findById(signupId)
            .orElseThrow(() -> new RuntimeException("找不到指定的報名記錄"));

        Event event = eventSignup.getEvent();

        // 檢查活動是否在投票階段
        if (event.getEventStatus() != 1) {
            throw new RuntimeException("當前不是投票階段");
        }

        // 檢查是否在投票時間範圍內（報名截止後至活動結束前）
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(event.getEventSignupDeadLine()) || now.isAfter(event.getEventEndTime())) {
            throw new RuntimeException("不在投票時間範圍內");
        }

        // 檢查用戶是否已經投過票給這個作品
        boolean hasVoted = eventVoteRepository.existsByMember_MemIdAndEventSignup_SignupId(memberId, signupId);
        if (hasVoted) {
            throw new RuntimeException("您已經為此作品投過票了");
        }

        // 檢查用戶在此活動中的投票次數是否達到上限（假設上限為5次）
        long voteCount = eventVoteRepository.countByMember_MemIdAndEvent_EventId(memberId, event.getEventId());
        if (voteCount >= 5) {
            throw new RuntimeException("您在此活動中的投票次數已達上限");
        }

        // 創建新的投票記錄
        EventVote eventVote = new EventVote();
        eventVote.setMember(memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("找不到指定的會員")));
        eventVote.setEventSignup(eventSignup);
        eventVote.setEvent(event);
        eventVote.setEventVoteStatus(1); // 假設1表示有效投票
        eventVote.setVoteTime(now); // 設置投票時間

        return eventVoteRepository.save(eventVote);
    }

    /**
     * 更新投票記錄
     * @param voteId 投票ID
     * @param eventVoteStatus 新的投票狀態
     * @return 更新後的投票記錄
     * @throws RuntimeException 如果找不到指定的投票記錄
     */
    @Transactional
    public EventVote updateVote(Integer voteId, Integer eventVoteStatus) {
        EventVote existingVote = eventVoteRepository.findById(voteId)
            .orElseThrow(() -> new RuntimeException("找不到指定的投票記錄"));
        
        existingVote.setEventVoteStatus(eventVoteStatus);
        
        return eventVoteRepository.save(existingVote);
    }

    /**
     * 刪除投票記錄
     * @param voteId 要刪除的投票ID
     * @throws RuntimeException 如果找不到指定的投票記錄
     */
    @Transactional
    public void deleteVote(Integer voteId) {
        EventVote vote = eventVoteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("找不到指定的投票記錄"));

        EventSignup eventSignup = vote.getEventSignup();
        eventSignup.setVoteCount(eventSignup.getVoteCount() - 1);
        eventSignupRepository.save(eventSignup);

        eventVoteRepository.deleteById(voteId);
    }

    // ======== 投票查詢相關方法 ========

    /**
     * 讀取特定的投票記錄
     * @param voteId 投票ID
     * @return 投票記錄
     * @throws RuntimeException 如果找不到指定的投票記錄
     */
    public EventVote getVote(Integer voteId) {
        return eventVoteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("找不到指定的投票記錄"));
    }

    /**
     * 獲取特定報名的所有投票
     * @param signupId 報名ID
     * @return 投票列表
     */
    public List<EventVote> getVotesBySignupId(Integer signupId) {
        return eventVoteRepository.findAll().stream()
                .filter(vote -> vote.getEventSignup().getSignupId().equals(signupId))
                .collect(Collectors.toList());
    }

    /**
     * 獲取所有投票
     * @return 所有投票的列表
     */
    public List<EventVote> getAllVotes() {
        return eventVoteRepository.findAll();
    }
    
    /**
     * 獲取特定活動的所有投票
     * @param eventId 活動ID
     * @return 該活動的所有投票列表
     */
    public List<EventVote> getVotesByEventId(Integer eventId) {
        return eventVoteRepository.findByEvent_EventId(eventId);
    }

    /**
     * 獲取特定用戶在特定活動中的投票次數
     * @param memId 會員ID
     * @param eventId 活動ID
     * @return 該用戶在該活動中的投票次數
     */
    public long getVoteCountByMemberAndEvent(Integer memId, Integer eventId) {
        return eventVoteRepository.countByMember_MemIdAndEvent_EventId(memId, eventId);
    }

    /**
     * 獲取特定活動的投票結果統計
     * @param eventId 活動ID
     * @return 一個Map，key是報名ID，value是得票數
     */
    public Map<Integer, Long> getVoteResultsByEventId(Integer eventId) {
        List<EventVote> votes = getVotesByEventId(eventId);
        return votes.stream()
                .collect(Collectors.groupingBy(
                        vote -> vote.getEventSignup().getSignupId(),
                        Collectors.counting()
                ));
    }
    
    /**
     * 獲取特定報名的投票數量
     * @param signupId 報名ID
     * @return 投票數量
     */
    public long getVoteCountForSignup(Integer signupId) {
        return eventVoteRepository.countByEventSignup_SignupIdAndEventVoteStatus(signupId,1);
    }
}