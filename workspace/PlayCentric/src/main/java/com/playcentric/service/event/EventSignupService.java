package com.playcentric.service.event;

import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventSignup;
import com.playcentric.model.event.EventSignupRepository;
import com.playcentric.model.event.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class EventSignupService {

    private static final Logger logger = LoggerFactory.getLogger(EventSignupService.class);

    @Autowired
    private EventSignupRepository eventSignupRepository;

    @Autowired
    private EventRepository eventRepository;

    /**
     * 創建新的報名
     * @param eventSignup 新的報名數據
     * @return 創建成功的報名數據
     * @throws RuntimeException 如果活動不存在或報名已截止
     */
    public EventSignup createSignup(EventSignup eventSignup) {
        logger.info("開始創建新的報名");
        Event event = eventRepository.findById(eventSignup.getEvent().getEventId())
            .orElseThrow(() -> {
                logger.error("活動不存在，活動ID: {}", eventSignup.getEvent().getEventId());
                return new RuntimeException("活動不存在");
            });

        if (event.getEventSignupDeadLine().isBefore(LocalDateTime.now())) {
            logger.error("報名已截止，活動ID: {}", event.getEventId());
            throw new RuntimeException("報名已截止");
        }

        eventSignup.setSignupTime(LocalDateTime.now());
        eventSignup.setVoteCount(0);
        eventSignup.setEventSignupStatus(1);
        EventSignup savedSignup = eventSignupRepository.save(eventSignup);
        logger.info("成功創建新的報名，報名ID: {}", savedSignup.getSignupId());
        return savedSignup;
    }

    /**
     * 根據ID獲取報名信息
     * @param signupId 報名ID
     * @return 包含報名信息的Optional
     */
    public Optional<EventSignup> getSignupById(Integer signupId) {
        logger.info("獲取報名信息，報名ID: {}", signupId);
        return eventSignupRepository.findById(signupId);
    }

    /**
     * 獲取所有報名
     * @return 所有報名的列表
     */
    public List<EventSignup> getAllSignups() {
        logger.info("獲取所有報名");
        return eventSignupRepository.findAll();
    }

    /**
     * 根據活動ID獲取報名
     * @param eventId 活動ID
     * @return 指定活動的所有報名
     */
    public List<EventSignup> getSignupsByEventId(Integer eventId) {
        logger.info("獲取活動的所有報名，活動ID: {}", eventId);
        return eventSignupRepository.findByEvent_EventId(eventId);
    }

    /**
     * 根據會員ID獲取報名
     * @param memberId 會員ID
     * @return 指定會員的所有報名
     */
    public List<EventSignup> getSignupsByMemberId(Integer memberId) {
        logger.info("獲取會員的所有報名，會員ID: {}", memberId);
        return eventSignupRepository.findByMember_MemId(memberId);
    }
    
    /**
     * 根據報名ID獲取報名圖片
     * @param signupId 報名ID
     * @return 報名圖片的字節數組
     * @throws RuntimeException 如果找不到指定的報名記錄
     */
    public byte[] getSignupImage(Integer signupId) {
        EventSignup signup = eventSignupRepository.findById(signupId)
            .orElseThrow(() -> new RuntimeException("報名記錄不存在"));
        return signup.getWorkImage();
    }

    /**
     * 更新報名信息
     * @param eventSignup 更新的報名數據
     * @return 更新後的報名數據
     * @throws RuntimeException 如果報名記錄不存在
     */
    public EventSignup updateSignup(EventSignup eventSignup) {
        logger.info("開始更新報名信息，報名ID: {}", eventSignup.getSignupId());
        if (!eventSignupRepository.existsById(eventSignup.getSignupId())) {
            logger.error("報名記錄不存在，報名ID: {}", eventSignup.getSignupId());
            throw new RuntimeException("報名記錄不存在");
        }
        EventSignup updatedSignup = eventSignupRepository.save(eventSignup);
        logger.info("成功更新報名信息，報名ID: {}", updatedSignup.getSignupId());
        return updatedSignup;
    }

    /**
     * 刪除報名
     * @param signupId 報名ID
     * @throws RuntimeException 如果報名記錄不存在
     */
    public void deleteSignup(Integer signupId) {
        logger.info("開始刪除報名，報名ID: {}", signupId);
        if (!eventSignupRepository.existsById(signupId)) {
            logger.error("報名記錄不存在，報名ID: {}", signupId);
            throw new RuntimeException("報名記錄不存在");
        }
        eventSignupRepository.deleteById(signupId);
        logger.info("成功刪除報名，報名ID: {}", signupId);
    }
    
    /**
     * 檢查用戶是否已經報名過特定活動
     * @param memId 會員ID
     * @param eventId 活動ID
     * @return 如果已經報名則返回true，否則返回false
     */
    public boolean hasUserSignedUp(Integer memId, Integer eventId) {
        return eventSignupRepository.existsByMember_MemIdAndEvent_EventId(memId, eventId);
    }
}