package com.playcentric.service.event;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventRepository;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

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
     * 根據ID查找活動
     * @param eventId 活動ID
     * @return 包含活動的Optional，如果未找到則為空
     */
    public Optional<Event> getEvent(Integer eventId) {
        return eventRepository.findById(eventId);
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
}