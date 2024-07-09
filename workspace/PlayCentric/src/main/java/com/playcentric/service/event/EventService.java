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

    // 創建活動
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // 根據ID查找活動
    public Event getEvent(Integer eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    // 查找所有活動
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // 更新活動
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    // 刪除活動
    public void deleteEvent(Integer eventId) {
        eventRepository.deleteById(eventId);
    }
}