package com.playcentric.controller.event;

import com.playcentric.model.event.Event;
import com.playcentric.model.ImageLib;
import com.playcentric.service.event.EventService;
import com.playcentric.service.ImageLibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private ImageLibService imageLibService;

    /**
     * 創建活動 (REST API)
     * @param event 活動實體
     * @return 新創建的活動或錯誤信息
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
     * 創建活動 (表單提交)
     * @param event 活動實體
     * @param photoFile 上傳的圖片文件
     * @param redirectAttributes 重定向屬性
     * @return 重定向到活動列表頁面
     */
    @PostMapping("/createPost")
    public String createEventPost(@ModelAttribute Event event,
                                  @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
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
     * @param event 活動實體
     * @param photoFile 上傳的圖片文件
     * @param redirectAttributes 重定向屬性
     * @return 重定向到活動列表頁面
     */
    @PostMapping("/updatePost")
    public String updateEventPost(@ModelAttribute Event event,
                                  @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
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
                // 如果沒有上傳新圖片，保留原有圖片
                event.setEventImage(original.getEventImage());
            }

            event.setEventType(original.getEventType());
            Event updatedEvent = eventService.updateEvent(event);
            logger.info("活動更新成功，活動ID: {}", updatedEvent.getEventId());
            redirectAttributes.addFlashAttribute("successMessage", "活動更新成功");
        } catch (Exception e) {
            logger.error("活動更新失敗", e);
            redirectAttributes.addFlashAttribute("errorMessage", "活動更新失敗: " + e.getMessage());
        }
        return "redirect:/events/listEvents";
    }

    /**
     * 根據ID查找活動 (REST API)
     * @param eventId 活動ID
     * @return 查找到的活動或404錯誤
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
     * @return 所有活動的列表
     */
    @GetMapping("/find")
    @ResponseBody
    public List<Event> getAllEvents() {
        logger.info("接收到獲取所有活動的請求");
        List<Event> events = eventService.getAllEvents();
        logger.info("成功獲取所有活動，共 {} 個活動", events.size());
        return events;
    }

    /**
     * 更新活動 (REST API)
     * @param event 活動實體
     * @return 更新後的活動或錯誤信息
     */
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> updateEvent(@RequestBody Event event) {
        logger.info("接收到更新活動的API請求，活動ID: {}", event.getEventId());
        try {
            Event updatedEvent = eventService.updateEvent(event);
            logger.info("活動更新成功，活動ID: {}", updatedEvent.getEventId());
            return ResponseEntity.ok(updatedEvent);
        } catch (IllegalArgumentException e) {
            logger.error("活動更新失敗", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 刪除活動
     * @param eventId 活動ID
     * @param redirectAttributes 重定向屬性，用於傳遞錯誤信息
     * @return 重定向到活動列表頁面
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

    /**
     * 顯示所有活動的列表頁面
     * @param model Spring MVC Model
     * @return 活動列表頁面
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
     * @param model Spring MVC Model
     * @return 公開活動列表的視圖名稱
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
     * @param eventId 活動ID
     * @param model Spring MVC Model
     * @return 活動詳細頁面或錯誤頁面
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
        model.addAttribute("event", event);
        logger.info("成功獲取並顯示活動詳情，活動ID: {}", eventId);
        return "event/event-detail";
    }
}