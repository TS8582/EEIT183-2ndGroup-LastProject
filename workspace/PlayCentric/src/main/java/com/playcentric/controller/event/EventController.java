package com.playcentric.controller.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playcentric.model.event.Event;
import com.playcentric.service.event.EventService;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    // 創建活動
    @PostMapping("/create")
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    // 根據ID查找活動
    @GetMapping("/get/{eventId}")
    public Event getEvent(@PathVariable Integer eventId) {
        return eventService.getEvent(eventId);
    }

    // 查找所有活動
    @GetMapping("/find")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // 更新活動
    @PutMapping("/update")
    public Event updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    // 刪除活動
    @DeleteMapping("/delete/{eventId}")
    public void deleteEvent(@PathVariable Integer eventId) {
        eventService.deleteEvent(eventId);
    }
}