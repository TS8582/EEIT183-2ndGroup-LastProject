package com.playcentric.controller.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventSignup;
import com.playcentric.model.event.EventSignupDTO;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.service.event.EventService;
import com.playcentric.service.event.EventSignupService;
import com.playcentric.service.event.EventVoteService;
import com.playcentric.service.member.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/eventSignup")
public class EventSignupController {

    private static final Logger logger = LoggerFactory.getLogger(EventSignupController.class);

    @Autowired
    private EventSignupService eventSignupService;

    @Autowired
    private EventService eventService;

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private EventVoteService eventVoteService;

    /**
     * 顯示報名管理頁面
     * @param model Spring MVC Model
     * @return 報名管理頁面視圖
     */
    @GetMapping("/manage")
    public String manageSignups(Model model) {
        List<EventSignup> signups = eventSignupService.getAllSignups();
        model.addAttribute("signups", signups);
        return "event/event-signup-management";
    }

    /**
     * 處理創建新報名的POST請求
     * @param eventSignup 報名數據
     * @param photoFile 上傳的圖片文件
     * @param eventId 活動ID
     * @param session HttpSession
     * @param redirectAttributes 重定向屬性
     * @return 重定向到活動詳情頁面
     */
    @PostMapping("/create")
    public String createSignup(@ModelAttribute EventSignup eventSignup,
                               @RequestParam("photoFile") MultipartFile photoFile,
                               @RequestParam("eventId") Integer eventId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        logger.info("開始處理報名請求，活動ID: {}", eventId);
        try {
            LoginMemDto loginMember = (LoginMemDto) session.getAttribute("loginMember");
            if (loginMember == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "請先登錄");
                return "redirect:/member/login";
            }

            Member member = memberService.findById(loginMember.getMemId());
            Event event = eventService.getEvent(eventId)
                .orElseThrow(() -> new RuntimeException("活動不存在"));

            // 檢查用戶是否已經報名過這個活動
            if (eventSignupService.hasUserSignedUp(member.getMemId(), eventId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "您已經報名過這個活動了");
                return "redirect:/events/public/detail/" + eventId;
            }

            eventSignup.setMember(member);
            eventSignup.setEvent(event);
            eventSignup.setWorkImage(photoFile.getBytes());
            eventSignup.setWorkUploadTime(LocalDateTime.now());

            EventSignup createdSignup = eventSignupService.createSignup(eventSignup);
            logger.info("報名成功，報名ID: {}", createdSignup.getSignupId());
            redirectAttributes.addFlashAttribute("successMessage", "報名成功！");
        } catch (Exception e) {
            logger.error("報名過程中發生錯誤", e);
            redirectAttributes.addFlashAttribute("errorMessage", "報名失敗: " + e.getMessage());
        }
        return "redirect:/events/public/detail/" + eventId;
    }

    /**
     * 獲取報名詳情（REST API）
     * @param signupId 報名ID
     * @return 報名詳情或404錯誤
     */
    @GetMapping("/api/{signupId}")
    @ResponseBody
    public ResponseEntity<?> getSignup(@PathVariable Integer signupId) {
        logger.info("獲取報名詳情，報名ID: {}", signupId);
        return eventSignupService.getSignupById(signupId)
            .map(signup -> {
                EventSignupDTO dto = new EventSignupDTO();
                dto.setSignupId(signup.getSignupId());
                dto.setWorkTitle(signup.getWorkTitle());
                dto.setWorkDescription(signup.getWorkDescription());
                dto.setVoteCount(eventVoteService.getVoteCountForSignup(signup.getSignupId()));
                dto.setMember(signup.getMember());
                return ResponseEntity.ok(dto);
            })
            .orElseGet(() -> {
                logger.warn("未找到指定的報名記錄");
                return ResponseEntity.notFound().build();
            });
    }    

    /**
     * 更新報名信息（REST API）
     * @param signupId 報名ID
     * @param eventSignup 更新的報名數據
     * @return 更新後的報名信息或錯誤信息
     */
    @PutMapping("/api/{signupId}")
    @ResponseBody
    public ResponseEntity<?> updateSignup(@PathVariable Integer signupId, @RequestBody EventSignup eventSignup) {
        logger.info("開始更新報名信息，報名ID: {}", signupId);
        try {
            eventSignup.setSignupId(signupId);
            EventSignup updatedSignup = eventSignupService.updateSignup(eventSignup);
            logger.info("成功更新報名信息");
            return ResponseEntity.ok(updatedSignup);
        } catch (RuntimeException e) {
            logger.error("更新報名信息時發生錯誤", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 刪除報名（REST API）
     * @param signupId 報名ID
     * @return 成功響應或錯誤信息
     */
    @DeleteMapping("/api/{signupId}")
    @ResponseBody
    public ResponseEntity<?> deleteSignup(@PathVariable Integer signupId) {
        logger.info("開始刪除報名，報名ID: {}", signupId);
        try {
            eventSignupService.deleteSignup(signupId);
            logger.info("成功刪除報名");
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("刪除報名時發生錯誤", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 獲取特定活動的所有報名（REST API）
     * @param eventId 活動ID
     * @return 該活動的所有報名列表
     */
    @GetMapping("/api/event/{eventId}")
    @ResponseBody
    public ResponseEntity<List<EventSignupDTO>> getEventSignups(@PathVariable Integer eventId) {
        List<EventSignup> signups = eventSignupService.getSignupsByEventId(eventId);
        List<EventSignupDTO> dtos = signups.stream().map(signup -> {
            EventSignupDTO dto = new EventSignupDTO();
            dto.setSignupId(signup.getSignupId());
            dto.setWorkTitle(signup.getWorkTitle());
            dto.setWorkDescription(signup.getWorkDescription());
            dto.setVoteCount(eventVoteService.getVoteCountForSignup(signup.getSignupId()));
            dto.setMember(signup.getMember());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * 獲取報名圖片
     * @param signupId 報名ID
     * @return 報名圖片的ResponseEntity
     */
    @GetMapping("/image/{signupId}")
    public ResponseEntity<byte[]> getSignupImage(@PathVariable Integer signupId) {
        try {
            byte[] imageBytes = eventSignupService.getSignupImage(signupId);
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
        } catch (RuntimeException e) {
            logger.error("獲取報名圖片失敗", e);
            return ResponseEntity.notFound().build();
        }
    }
}