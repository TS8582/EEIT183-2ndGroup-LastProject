package com.playcentric.service.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventRepository;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;

    // ======== 活動管理相關方法 ========

    /**
     * 創建新活動
     * @param event 要創建的活動
     * @return 創建後的活動
     * @throws IllegalArgumentException 如果活動數據無效
     */
    public Event createEvent(Event event) {
        validateEvent(event);
        return eventRepository.save(event);
    }

    /**
     * 根據ID查找活動，並在必要時更新活動狀態
     * @param eventId 活動ID
     * @return 包含活動的Optional，如果未找到則為空
     */
    public Optional<Event> getEvent(Integer eventId) {
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        eventOpt.ifPresent(this::updateEventStatusIfNeeded);
        return eventOpt;
    }

    /**
     * 獲取所有活動
     * @return 所有活動的列表
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * 更新活動
     * @param event 要更新的活動
     * @return 更新後的活動
     * @throws IllegalArgumentException 如果活動數據無效
     */
    public Event updateEvent(Event event) {
        validateEvent(event);
        return eventRepository.save(event);
    }

    /**
     * 刪除活動
     * @param eventId 要刪除的活動ID
     */
    public void deleteEvent(Integer eventId) {
        eventRepository.deleteById(eventId);
    }

    // ======== 活動查詢相關方法 ========

    /**
     * 獲取進行中的活動
     * @return 進行中活動的列表
     */
    public List<Event> getOngoingEvents() {
        return eventRepository.findByEventStatusOrderByEventStartTimeDesc(1);
    }

    /**
     * 獲取已結束的活動
     * @return 已結束活動的列表
     */
    public List<Event> getCompletedEvents() {
        return eventRepository.findByEventStatusOrderByEventEndTimeDesc(2);
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
     * 更新活動狀態（如果需要）
     * @param event 要檢查的活動
     */
    private void updateEventStatusIfNeeded(Event event) {
        if (event.getEventStatus() == 1 && event.getEventEndTime().isBefore(LocalDateTime.now())) {
            event.setEventStatus(2); // 2 表示已結束
            eventRepository.save(event);
        }
    }

    // ======== 定時任務 ========

    /**
     * 定時任務：自動更新已結束活動的狀態
     * 每小時執行一次
     */
    @Scheduled(cron = "0 0 * * * *")
    public void updateEventStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findByEventStatusAndEventEndTimeBefore(1, now);
        events.forEach(event -> {
            event.setEventStatus(2); // 2 表示已結束
            eventRepository.save(event);
        });
        System.out.println("Updated " + events.size() + " events to ended status.");
    }
}