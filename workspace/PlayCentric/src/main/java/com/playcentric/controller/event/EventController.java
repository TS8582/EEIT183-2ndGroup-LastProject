package com.playcentric.controller.event;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.model.event.Event;
import com.playcentric.service.event.EventService;

@Controller
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    // 創建活動
    @PostMapping("/create")
    @ResponseBody
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }
    
    @PostMapping("/createPost")
    public String createEventPost(@ModelAttribute Event event) {
    	event.setEventType(1);
    	eventService.createEvent(event);
    	
    	return "redirect:/events/listEvents";
    }
    
    @PostMapping("/updatePost")
	public String updateEventPost(@ModelAttribute Event event) {
    	
    	Event original = eventService.getEvent(event.getEventId());
		
		event.setEventType(original.getEventType());

		eventService.updateEvent(event);
		
		return "redirect:/events/listEvents";
	}

    // 根據ID查找活動
    @GetMapping("/get/{eventId}")
    @ResponseBody
    public Event getEvent(@PathVariable Integer eventId) {
        return eventService.getEvent(eventId);
    }

    // 查找所有活動
    @GetMapping("/find")
    @ResponseBody
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // 更新活動
    @PostMapping("/update")
    @ResponseBody
    public Event updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    // 刪除活動
    @PostMapping("/delete")
    public String deleteEvent(@RequestParam Integer eventId, RedirectAttributes redirectAttributes) {
    	try {
    		eventService.deleteEvent(eventId);
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errMsg", "已有報名，無法刪除!");
		}
		return "redirect:/events/listEvents";
    }
    
    @GetMapping("/listEvents")
    public String events(Model model) {
    	List<Event> allEvents = eventService.getAllEvents();
    	model.addAttribute("allEvents", allEvents);
        return "event/events";
    }
}