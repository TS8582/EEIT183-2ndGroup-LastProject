package com.playcentric.controller.event;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.ImageLib;
import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventSignup;
import com.playcentric.service.ImageLibService;
import com.playcentric.service.event.EventService;
import com.playcentric.service.event.EventSignupService;

@Controller
@RequestMapping("/eventSignup")
public class EventSignupController {

    @Autowired
    private EventSignupService eventSignupService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ImageLibService imageLibService;
    
    @GetMapping("/manage")
    public String manageSignups(Model model) {
        List<EventSignup> signups = eventSignupService.getAllSignups();
        model.addAttribute("signups", signups);
        return "event/event-signup-management";
    }

    /**
     * 處理創建新報名的POST請求。
     * @param eventSignup 從表單提交的報名數據，使用@ModelAttribute綁定
     * @param model Spring MVC的Model對象，用於向視圖傳遞數據
     * @return 創建成功後跳轉
     */
    @PostMapping("/create")
    public String createSignup(@ModelAttribute EventSignup eventSignup, Model model) {
        EventSignup createdSignup = eventSignupService.createSignup(eventSignup);
        model.addAttribute("signup", createdSignup);
        return "signupCreated"; // 返回創建成功的視圖
    }

    /**
     * 處理獲取單個報名詳情的GET請求。
     * @param signupId 報名ID
     * @param model Spring MVC的Model對象
     * @return 顯示報名詳情
     */
    @GetMapping("/get/{signupId}")
    public String getSignup(@PathVariable Integer signupId, Model model) {
        EventSignup signup = eventSignupService.getSignupById(signupId);
        model.addAttribute("signup", signup);
        return "signupDetails"; // 返回報名詳情的視圖
    }

    /**
     * 處理獲取特定活動所有報名的GET請求。
     * @param eventId 活動ID
     * @param model Spring MVC的Model對象
     * @return 顯示活動所有報名
     */
    @GetMapping("/event/{eventId}")
    public String getSignupsByEvent(@PathVariable Integer eventId, Model model) {
        List<EventSignup> signups = eventSignupService.getSignupsByEventId(eventId);
        model.addAttribute("signups", signups);
        return "eventSignups"; // 返回活動報名列表的視圖
    }

    /**
     * 處理獲取特定會員所有報名的GET請求。
     * @param memberId 會員ID
     * @param model Spring MVC的Model對象
     * @return 顯示會員所有報名
     */
    @GetMapping("/member/{memberId}")
    public String getSignupsByMember(@PathVariable Integer memberId, Model model) {
        List<EventSignup> signups = eventSignupService.getSignupsByMemberId(memberId);
        model.addAttribute("signups", signups);
        return "memberSignups"; // 返回會員報名列表的視圖
    }

    /**
     * 處理顯示創建新報名表單的GET請求。
     * @param model Spring MVC的Model對象
     * @return 顯示報名表單
     */
    @GetMapping("/new")
    public String showSignupForm(Model model) {
        model.addAttribute("eventSignup", new EventSignup());
        return "signupForm"; // 返回創建報名的表單視圖
    }

    /**
     * API: 創建新的報名
     */
    @PostMapping("/api/create")
    @ResponseBody
    public ResponseEntity<EventSignup> apiCreateSignup(@RequestBody EventSignup eventSignup) {
        EventSignup createdSignup = eventSignupService.createSignup(eventSignup);
        return ResponseEntity.ok(createdSignup);
    }

    /**
     * API: 獲取報名詳情
     */
    @GetMapping("/api/{signupId}")
    @ResponseBody
    public ResponseEntity<EventSignup> apiGetSignup(@PathVariable Integer signupId) {
        EventSignup signup = eventSignupService.getSignupById(signupId);
        return ResponseEntity.ok(signup);
    }

    /**
     * API: 更新報名信息
     */
    @PutMapping("/api/{signupId}")
    @ResponseBody
    public ResponseEntity<EventSignup> apiUpdateSignup(@PathVariable Integer signupId, @RequestBody EventSignup eventSignup) {
        eventSignup.setSignupId(signupId);
        EventSignup updatedSignup = eventSignupService.updateSignup(eventSignup);
        return ResponseEntity.ok(updatedSignup);
    }

    /**
     * API: 刪除報名
     */
    @DeleteMapping("/api/{signupId}")
    @ResponseBody
    public ResponseEntity<Void> apiDeleteSignup(@PathVariable Integer signupId) {
        eventSignupService.deleteSignup(signupId);
        return ResponseEntity.ok().build();
    }

    /**
     * API: 獲取所有報名
     */
    @GetMapping("/api/find")
    @ResponseBody
    public ResponseEntity<List<EventSignup>> apiGetAllSignups() {
        List<EventSignup> signups = eventSignupService.getAllSignups();
        return ResponseEntity.ok(signups);
    }
    
    @PostMapping("/createimage")
    public String createSignup(@ModelAttribute EventSignup eventSignup, 
                               @RequestParam("photoFile") MultipartFile photoFile,
                               @RequestParam("eventId") Integer eventId,
                               Model model) {
        try {
            // 使用ImageLibService保存圖片
            ImageLib imageLib = new ImageLib();
            imageLib.setImageFile(photoFile.getBytes());
            ImageLib savedImage = imageLibService.saveImage(imageLib);

            // 設置 EventSignup 屬性
            Event event = eventService.getEvent(eventId);
            eventSignup.setEvent(event);
            eventSignup.setWorkImageId(savedImage.getImageId());
            eventSignup.setSignupTime(LocalDateTime.now());
            eventSignup.setWorkUploadTime(LocalDateTime.now());
            eventSignup.setVoteCount(0);
            eventSignup.setEventSignupStatus(1); // 假設 1 表示已報名狀態

            EventSignup createdSignup = eventSignupService.createSignup(eventSignup);
            model.addAttribute("signup", createdSignup);
            return "redirect:/events/public/detail/" + eventId;
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "文件上傳失敗");
            return "signup-form";
        }
    }
}