package com.playcentric.service.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    // ======== 活動管理相關方法 ========

    /**
     * 創建新活動
     */
    @Transactional
    public Event createEvent(Event event) {
        validateEvent(event);
        event.setEventStatus(event.calculateEventStatus());
        Event createdEvent = eventRepository.save(event);
        logger.info("活動創建成功，活動ID: {}", createdEvent.getEventId());
        return createdEvent;
    }

    /**
     * 根據ID查找活動，並更新狀態
     */
    @Transactional
    public Optional<Event> getEvent(Integer eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        eventOpt.ifPresent(this::updateEventStatus);
        return eventOpt;
    }

    /**
     * 獲取所有活動
     * @return 所有活動的列表
     */
    public List<Event> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        events.forEach(this::updateEventStatus);
        return events;
    }

    /**
     * 更新活動
     * @param event 要更新的活動
     * @param useAutoStatus 是否使用自動計算的狀態
     * @return 更新後的活動
     */
    @Transactional
    public Event updateEvent(Event event, boolean useAutoStatus) {
        try {
            // 獲取原有的活動資訊
            Event existingEvent = eventRepository.findById(event.getEventId())
                .orElseThrow(() -> new RuntimeException("找不到指定的活動"));

            // 更新基本資訊
            existingEvent.setEventName(event.getEventName());
            existingEvent.setEventDescription(event.getEventDescription());
            existingEvent.setEventStartTime(event.getEventStartTime());
            existingEvent.setEventEndTime(event.getEventEndTime());
            existingEvent.setEventSignupDeadLine(event.getEventSignupDeadLine());

            // 保留原有的 eventType
            // existingEvent.setEventType(event.getEventType()); // 此行被註釋掉，以保留原有的 eventType

            // 只在需要時更新圖片
            if (event.getEventImage() != null) {
                existingEvent.setEventImage(event.getEventImage());
            }

            // 根據 useAutoStatus 設置狀態
            if (useAutoStatus) {
                existingEvent.setEventStatus(existingEvent.calculateEventStatus());
            } else if (event.getEventStatus() != null) {
                existingEvent.setEventStatus(event.getEventStatus());
            }

            validateEvent(existingEvent);
            Event updatedEvent = eventRepository.save(existingEvent);
            logger.info("活動更新成功，活動ID: {}", updatedEvent.getEventId());
            return updatedEvent;
        } catch (Exception e) {
            logger.error("活動更新失敗", e);
            throw new RuntimeException("活動更新失敗: " + e.getMessage());
        }
    }

    /**
     * 刪除活動
     * @param eventId 要刪除的活動ID
     */
    @Transactional
    public void deleteEvent(Integer eventId) {
        eventRepository.deleteById(eventId);
        logger.info("活動刪除成功，活動ID: {}", eventId);
    }

    // ======== 活動查詢相關方法 ========

    /**
     * 獲取進行中的活動
     * @return 進行中活動的列表
     */
    public List<Event> getOngoingEvents() {
        List<Event> events = eventRepository.findByEventStatus(1);
        events.forEach(this::updateEventStatus);
        return events.stream()
            .filter(e -> e.getEventStatus() == 1)
            .toList();
    }

    /**
     * 獲取已結束的活動
     * @return 已結束活動的列表
     */
    public List<Event> getCompletedEvents() {
        List<Event> events = eventRepository.findByEventStatus(2);
        events.forEach(this::updateEventStatus);
        return events.stream()
            .filter(e -> e.getEventStatus() == 2)
            .toList();
    }

    // ======== 輔助方法 ========

    /**
     * 驗證活動數據
     * @param event 要驗證的活動
     * @throws IllegalArgumentException 如果活動數據無效
     */
    private void validateEvent(Event event) {
        if (event.getEventEndTime().isBefore(event.getEventStartTime())) {
            throw new IllegalArgumentException("結束時間不能早於開始時間");
        }
        if (event.getEventSignupDeadLine().isAfter(event.getEventEndTime())) {
            throw new IllegalArgumentException("報名截止時間不能晚於活動結束時間");
        }
    }

    /**
     * 更新活動狀態
     * @param event 要更新的活動
     */
    @Transactional
    public void updateEventStatus(Event event) {
        int calculatedStatus = event.calculateEventStatus();
        if (event.getEventStatus() != calculatedStatus) {
            event.setEventStatus(calculatedStatus);
            eventRepository.save(event);
            logger.info("更新活動狀態：活動ID {}，新狀態 {}", event.getEventId(), calculatedStatus);
        }
    }

    // ======== 定時任務 ========

    /**
     * 定時任務：更新所有活動狀態
     * 每小時執行一次
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updateAllEventStatuses() {
        List<Event> events = eventRepository.findAll();
        for (Event event : events) {
            updateEventStatus(event);
        }
        logger.info("完成所有活動狀態的定時更新");
    }

    /**
     * 獲取下一個即將結束的活動
     * @return 下一個即將結束的活動，如果沒有則返回空
     */
    public Optional<Event> getNextEndingEvent() {
        return eventRepository.findTopByEventStatusAndEventEndTimeAfterOrderByEventEndTimeAsc(1, LocalDateTime.now());
    }

    /**
     * 更新特定活動的狀態
     * @param eventId 要更新的活動ID
     */
    @Transactional
    public void updateSpecificEventStatus(Integer eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("找不到指定的活動"));
        updateEventStatus(event);
    }
}