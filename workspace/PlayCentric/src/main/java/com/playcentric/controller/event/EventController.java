package com.playcentric.controller.event;

import java.util.List;
import java.util.stream.Collectors;

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
    /**
     * 創建活動 (REST API)
     * @param event 活動實體
     * @return 新創建的活動
     */
    @PostMapping("/create")
    @ResponseBody
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    /**
     * 創建活動 (表單提交)
     * @param event 活動實體
     * @return 重定向到活動列表頁面
     */
    @PostMapping("/createPost")
    public String createEventPost(@ModelAttribute Event event) {
        event.setEventType(1);
        eventService.createEvent(event);
        return "redirect:/events/listEvents";
    }

    /**
     * 更新活動 (表單提交)
     * @param event 活動實體
     * @return 重定向到活動列表頁面
     */
    @PostMapping("/updatePost")
    public String updateEventPost(@ModelAttribute Event event) {
        Event original = eventService.getEvent(event.getEventId());
        // 保留活動類型
        event.setEventType(original.getEventType());
        // 更新活動狀態
        eventService.updateEvent(event);
        return "redirect:/events/listEvents";
    }

    /**
     * 根據ID查找活動
     * @param eventId 活動ID
     * @return 查找到的活動
     */
    @GetMapping("/get/{eventId}")
    @ResponseBody
    public Event getEvent(@PathVariable Integer eventId) {
        return eventService.getEvent(eventId);
    }

    /**
     * 查找所有活動
     * @return 所有活動的列表
     */
    @GetMapping("/find")
    @ResponseBody
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    /**
     * 更新活動 (REST API)
     * @param event 活動實體
     * @return 更新後的活動
     */
    @PostMapping("/update")
    @ResponseBody
    public Event updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    /**
     * 刪除活動
     * @param eventId 活動ID
     * @param redirectAttributes 重定向屬性，用於傳遞錯誤信息
     * @return 重定向到活動列表頁面
     */
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

    /**
     * 顯示所有活動的列表頁面
     * @param model 模型，用於傳遞活動列表數據
     * @return 活動列表頁面
     */
    @GetMapping("/listEvents")
    public String events(Model model) {
        List<Event> allEvents = eventService.getAllEvents();
        model.addAttribute("allEvents", allEvents);
        return "event/events";
    }

    /**
     * 顯示所有公開活動的列表
     * @param model 模型，用於傳遞活動列表數據
     * @return 公開活動列表的視圖名稱
     */
    @GetMapping("/public/list")
    public String publicEventList(Model model) {
        List<Event> allEvents = eventService.getAllEvents();
        List<Event> ongoingEvents = allEvents.stream()
                .filter(e -> e.getEventStatus() == 1)
                .collect(Collectors.toList());
        List<Event> completedEvents = allEvents.stream()
                .filter(e -> e.getEventStatus() == 2)
                .collect(Collectors.toList());
        
        model.addAttribute("ongoingEvents", ongoingEvents);
        model.addAttribute("completedEvents", completedEvents);
        return "event/event-list";
    }

    /**
     * 顯示指定ID的公開活動詳細資訊
     * @param eventId 活動ID
     * @param model 模型，用於傳遞活動詳細資訊
     * @return 如果活動存在，返回活動詳細頁面；如果活動不存在，重新定向到活動列表
     */
    @GetMapping("/public/detail/{eventId}")
    public String publicEventDetail(@PathVariable Integer eventId, Model model) {
        Event event = eventService.getEvent(eventId);
        if (event == null) {
            model.addAttribute("errorMessage", "找不到指定的活動");
            return "event/event-not-found"; // 創建一個新的錯誤頁面
        }
        model.addAttribute("event", event);
        return "event/event-detail";
    }
}
