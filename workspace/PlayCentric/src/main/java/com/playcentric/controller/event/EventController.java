package com.playcentric.controller.event;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.model.ImageLib;
import com.playcentric.model.event.Event;
import com.playcentric.service.ImageLibService;
import com.playcentric.service.event.EventService;

@Controller
@RequestMapping("/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private ImageLibService imageLibService;

    // ======== 網頁視圖相關方法 ========

    /**
     * 顯示所有活動的列表頁面
     */
    @GetMapping("/listEvents")
    public String events(Model model) {
        logger.info("接收到顯示活動列表的請求");
        List<Event> allEvents = eventService.getAllEvents();
        model.addAttribute("allEvents", allEvents);
        logger.info("成功獲取活動列表，共 {} 個活動", allEvents.size());
        return "event/events";
    }

    /**
     * 顯示所有公開活動的列表
     */
    @GetMapping("/public/list")
    public String publicEventList(Model model) {
        logger.info("接收到顯示公開活動列表的請求");
        List<Event> ongoingEvents = eventService.getOngoingEvents();
        List<Event> completedEvents = eventService.getCompletedEvents();
        model.addAttribute("ongoingEvents", ongoingEvents);
        model.addAttribute("completedEvents", completedEvents);
        logger.info("成功獲取公開活動列表，進行中: {}，已完成: {}", ongoingEvents.size(), completedEvents.size());
        return "event/event-list";
    }

    /**
     * 顯示指定ID的公開活動詳細資訊
     */
    @GetMapping("/public/detail/{eventId}")
    public String publicEventDetail(@PathVariable Integer eventId, Model model) {
        logger.info("接收到顯示活動詳情的請求，活動ID: {}", eventId);
        Event event = eventService.getEvent(eventId).orElse(null);
        if (event == null) {
            logger.warn("未找到指定的活動，活動ID: {}", eventId);
            model.addAttribute("errorMessage", "找不到指定的活動");
            return "event/event-not-found";
        }
        
        eventService.updateSpecificEventStatus(eventId);
        
        model.addAttribute("event", event);
        logger.info("成功獲取並顯示活動詳情，活動ID: {}", eventId);
        return "event/event-detail";
    }

    /**
     * 創建活動 (表單提交)
     */
    @PostMapping("/createPost")
    public String createEventPost(@ModelAttribute Event event,
                                  @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                                  @RequestParam(value = "useAutoStatus", defaultValue = "true") boolean useAutoStatus,
                                  RedirectAttributes redirectAttributes) {
        logger.info("開始處理創建活動的表單提交");
        try {
            // 處理圖片上傳
            if (photoFile != null && !photoFile.isEmpty()) {
                ImageLib imageLib = new ImageLib();
                imageLib.setImageFile(photoFile.getBytes());
                imageLib = imageLibService.saveImage(imageLib);
                event.setEventImage(imageLib);
                logger.info("活動圖片上傳成功，圖片ID: {}", imageLib.getImageId());
            }

            event.setEventType(1); // 設置默認事件類型
            if (useAutoStatus) {
                event.setEventStatus(event.calculateEventStatus());
            }
            Event createdEvent = eventService.createEvent(event);
            logger.info("活動創建成功，活動ID: {}", createdEvent.getEventId());
            redirectAttributes.addFlashAttribute("successMessage", "活動創建成功");
        } catch (Exception e) {
            logger.error("活動創建失敗", e);
            redirectAttributes.addFlashAttribute("errorMessage", "活動創建失敗: " + e.getMessage());
        }
        return "redirect:/events/listEvents";
    }

    /**
     * 更新活動 (表單提交)
     */
    @PostMapping("/updatePost")
    public String updateEventPost(@ModelAttribute Event event,
                                  @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                                  @RequestParam(value = "useAutoStatus", defaultValue = "true") boolean useAutoStatus,
                                  RedirectAttributes redirectAttributes) {
        logger.info("開始處理更新活動的表單提交，活動ID: {}", event.getEventId());
        try {
            Event original = eventService.getEvent(event.getEventId())
                .orElseThrow(() -> new RuntimeException("找不到指定的活動"));
            
            // 處理圖片更新
            if (photoFile != null && !photoFile.isEmpty()) {
                ImageLib imageLib = new ImageLib();
                imageLib.setImageFile(photoFile.getBytes());
                imageLib = imageLibService.saveImage(imageLib);
                event.setEventImage(imageLib);
                logger.info("活動圖片更新成功，新圖片ID: {}", imageLib.getImageId());
            } else {
                event.setEventImage(original.getEventImage());
            }

            event.setEventType(original.getEventType());
            
            // 確保 eventStatus 不為 null
            if (event.getEventStatus() == null) {
                event.setEventStatus(useAutoStatus ? event.calculateEventStatus() : 0);
            }
            
            Event updatedEvent = eventService.updateEvent(event, useAutoStatus);
            logger.info("活動更新成功，活動ID: {}", updatedEvent.getEventId());
            redirectAttributes.addFlashAttribute("successMessage", "活動更新成功");
        } catch (Exception e) {
            logger.error("活動更新失敗", e);
            redirectAttributes.addFlashAttribute("errorMessage", "活動更新失敗: " + e.getMessage());
        }
        return "redirect:/events/listEvents";
    }

    /**
     * 刪除活動
     */
    @PostMapping("/delete")
    public String deleteEvent(@RequestParam Integer eventId, RedirectAttributes redirectAttributes) {
        logger.info("接收到刪除活動的請求，活動ID: {}", eventId);
        try {
            eventService.deleteEvent(eventId);
            logger.info("活動刪除成功，活動ID: {}", eventId);
            redirectAttributes.addFlashAttribute("successMessage", "活動刪除成功");
        } catch (Exception e) {
            logger.error("活動刪除失敗", e);
            redirectAttributes.addFlashAttribute("errorMessage", "已有報名，無法刪除!");
        }
        return "redirect:/events/listEvents";
    }

    // ======== API 相關方法 ========

    /**
     * 創建活動 (REST API)
     */
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        logger.info("接收到創建活動的API請求");
        try {
            Event createdEvent = eventService.createEvent(event);
            logger.info("活動創建成功，活動ID: {}", createdEvent.getEventId());
            return ResponseEntity.ok(createdEvent);
        } catch (IllegalArgumentException e) {
            logger.error("活動創建失敗", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根據ID查找活動 (REST API)
     */
    @GetMapping("/get/{eventId}")
    @ResponseBody
    public ResponseEntity<?> getEvent(@PathVariable Integer eventId) {
        logger.info("接收到獲取活動詳情的請求，活動ID: {}", eventId);
        return eventService.getEvent(eventId)
            .map(event -> {
                logger.info("成功獲取活動詳情，活動ID: {}", eventId);
                return ResponseEntity.ok(event);
            })
            .orElseGet(() -> {
                logger.warn("未找到指定的活動，活動ID: {}", eventId);
                return ResponseEntity.notFound().build();
            });
    }

    /**
     * 查找所有活動 (REST API)
     */
    @GetMapping("/find")
    @ResponseBody
    public List<Event> getAllEvents() {
        logger.info("接收到獲取所有活動的請求");
        List<Event> events = eventService.getAllEvents();
        logger.info("成功獲取所有活動，共 {} 個活動", events.size());
        return events;
    }

    @PostMapping("/updatePostAjax")
    @ResponseBody
    public ResponseEntity<?> updateEventPostAjax(@ModelAttribute Event event,
                                                 @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                                                 @RequestParam(value = "useAutoStatus", defaultValue = "true") boolean useAutoStatus) {
        try {
            Event original = eventService.getEvent(event.getEventId())
                .orElseThrow(() -> new RuntimeException("找不到指定的活動"));
            
            // 保留原始的 eventType
            event.setEventType(original.getEventType());
            
            // 处理图片更新
            if (photoFile != null && !photoFile.isEmpty()) {
                ImageLib imageLib = new ImageLib();
                imageLib.setImageFile(photoFile.getBytes());
                imageLib = imageLibService.saveImage(imageLib);
                event.setEventImage(imageLib);
            } else {
                event.setEventImage(original.getEventImage());
            }
            
            // 确保 eventStatus 不为 null
            if (event.getEventStatus() == null) {
                event.setEventStatus(useAutoStatus ? event.calculateEventStatus() : original.getEventStatus());
            }
            
            Event updatedEvent = eventService.updateEvent(event, useAutoStatus);
            logger.info("活動更新成功（AJAX），活動ID: {}", updatedEvent.getEventId());
            return ResponseEntity.ok(Map.of("success", true, "message", "活動更新成功"));
        } catch (Exception e) {
            logger.error("活動更新失敗（AJAX）", e);
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "活動更新失敗: " + e.getMessage()));
        }
    }
    
    /**
     * 獲取下一個即將結束的活動 (REST API)
     */
    @GetMapping("/api/next-ending")
    @ResponseBody
    public ResponseEntity<?> getNextEndingEvent() {
        return eventService.getNextEndingEvent()
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}