package com.playcentric.service.event;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.event.EventSignup;
import com.playcentric.model.event.EventSignupRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EventSignupService {

    @Autowired
    private EventSignupRepository eventSignupRepository;

    /**
     * 創建新的報名
     * @param eventSignup 新的報名數據
     * @return 創建成功的報名數據
     */
    public EventSignup createSignup(EventSignup eventSignup) {
        eventSignup.setSignupTime(LocalDateTime.now()); // 設置當前時間為報名時間
        eventSignup.setVoteCount(0); // 初始化投票數
        eventSignup.setEventSignupStatus(1); // 假設1代表已報名狀態
        return eventSignupRepository.save(eventSignup); // 保存報名到資料庫
    }

    /**
     * 獲取指定ID的報名詳情
     * @param signupId 報名ID
     * @return 指定ID的報名數據
     */
    public EventSignup getSignupById(Integer signupId) {
        return eventSignupRepository.findById(signupId)
        		// 若不存在，拋出異常
                .orElseThrow(() -> new RuntimeException("報名記錄不存在")); 
    }

    /**
     * 獲取所有報名的列表
     * @return 所有報名的列表
     */
    public List<EventSignup> getAllSignups() {
    	// 獲取所有報名
        return eventSignupRepository.findAll(); 
    }

    /**
     * 根據活動ID獲取報名
     * @param eventId 活動ID
     * @return 指定活動的所有報名
     */
    public List<EventSignup> getSignupsByEventId(Integer eventId) {
        return eventSignupRepository.findByEvent_EventId(eventId);
    }

    /**
     * 根據會員ID獲取報名
     * @param memberId 會員ID
     * @return 指定會員的所有報名
     */
    public List<EventSignup> getSignupsByMemberId(Integer memberId) {
        return eventSignupRepository.findByMember_MemId(memberId);
    }

    /**
     * 更新報名信息
     * @param eventSignup 更新的報名數據
     * @return 更新後的報名數據
     */
    public EventSignup updateSignup(EventSignup eventSignup) {
        if (!eventSignupRepository.existsById(eventSignup.getSignupId())) {
        	// 若不存在，拋出異常
            throw new RuntimeException("報名記錄不存在"); 
        }
        // 保存更新到資料庫
        return eventSignupRepository.save(eventSignup); 
    }

    /**
     * 刪除報名
     * @param signupId 報名ID
     */
    public void deleteSignup(Integer signupId) {
    	// 根據ID刪除報名
        eventSignupRepository.deleteById(signupId); 
    }
}