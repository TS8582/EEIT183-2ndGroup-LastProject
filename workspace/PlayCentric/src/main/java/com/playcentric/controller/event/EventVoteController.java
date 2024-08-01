package com.playcentric.controller.event;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.playcentric.model.event.EventVote;
import com.playcentric.service.event.EventVoteService;

/**
 * 活動投票控制器
 * 處理與活動投票相關的請求
 */
@Controller
@RequestMapping("/eventVotes")
public class EventVoteController {

    private static final Logger logger = LoggerFactory.getLogger(EventVoteController.class);

    @Autowired
    private EventVoteService eventVoteService;

    // ======== 網頁視圖相關方法 ========

    /**
     * 顯示投票列表頁面
     * @param model Spring MVC Model
     * @return 投票列表頁面視圖名稱
     */
    @GetMapping("/list")
    public String listVotes(Model model) {
        List<EventVote> votes = eventVoteService.getAllVotes();
        model.addAttribute("votes", votes);
        return "event/vote-list";
    }

    /**
     * 顯示創建投票的表單
     * @param model Spring MVC Model
     * @return 投票表單頁面視圖名稱
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("eventVote", new EventVote());
        return "event/vote-form";
    }

    /**
     * 處理創建投票的表單提交
     * @param memberId 會員ID
     * @param signupId 報名ID
     * @param model Spring MVC Model
     * @return 重定向到投票列表或返回表單（如果出錯）
     */
    @PostMapping("/create")
    public String createVoteForm(@RequestParam Integer memberId, @RequestParam Integer signupId, Model model) {
        try {
            eventVoteService.createVote(memberId, signupId);
            return "redirect:/eventVotes/list";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "event/vote-form";
        }
    }

    /**
     * 顯示特定投票的詳情
     * @param voteId 投票ID
     * @param model Spring MVC Model
     * @return 投票詳情頁面視圖名稱或錯誤頁面
     */
    @GetMapping("/{voteId}")
    public String getVote(@PathVariable Integer voteId, Model model) {
        try {
            EventVote vote = eventVoteService.getVote(voteId);
            model.addAttribute("vote", vote);
            return "event/vote-detail";
        } catch (RuntimeException e) {
            model.addAttribute("error", "找不到指定的投票記錄");
            return "error";
        }
    }
    
    /**
     * 顯示編輯投票的表單
     * @param voteId 投票ID
     * @param model Spring MVC Model
     * @return 投票表單頁面視圖名稱
     */
    @GetMapping("/edit/{voteId}")
    public String showEditForm(@PathVariable Integer voteId, Model model) {
        EventVote vote = eventVoteService.getVote(voteId);
        model.addAttribute("vote", vote);
        return "event/vote-form";
    }

    /**
     * 處理更新投票的表單提交
     * @param voteId 投票ID
     * @param eventVoteStatus 新的投票狀態
     * @param model Spring MVC Model
     * @return 重定向到投票詳情或返回表單（如果出錯）
     */
    @PostMapping("/update")
    public String updateVote(@RequestParam Integer voteId, @RequestParam Integer eventVoteStatus, Model model) {
        try {
            EventVote updatedVote = eventVoteService.updateVote(voteId, eventVoteStatus);
            return "redirect:/eventVotes/" + updatedVote.getVoteId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "event/vote-form";
        }
    }

    /**
     * 處理刪除投票的請求
     * @param voteId 投票ID
     * @param model Spring MVC Model
     * @return 重定向到投票列表或投票詳情（如果出錯）
     */
    @PostMapping("/delete/{voteId}")
    public String deleteVote(@PathVariable Integer voteId, Model model) {
        try {
            eventVoteService.deleteVote(voteId);
            return "redirect:/eventVotes/list";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/eventVotes/" + voteId;
        }
    }

    /**
     * 顯示特定報名的所有投票
     * @param signupId 報名ID
     * @param model Spring MVC Model
     * @return 投票列表頁面視圖名稱
     */
    @GetMapping("/signup/{signupId}")
    public String getVotesBySignup(@PathVariable Integer signupId, Model model) {
        List<EventVote> votes = eventVoteService.getVotesBySignupId(signupId);
        model.addAttribute("votes", votes);
        return "event/vote-list";
    }

    /**
     * 顯示投票管理頁面
     * @param model Spring MVC Model
     * @return 投票管理頁面視圖名稱
     */
    @GetMapping("/manage")
    public String manageVotes(Model model) {
        return "event/event-vote-management";
    }

    // ======== API 相關方法 ========

    /**
     * API: 創建新的投票
     * @param memberId 會員ID
     * @param signupId 報名ID
     * @return 創建結果的 ResponseEntity
     */
    @PostMapping("/api/create")
    @ResponseBody
    public ResponseEntity<?> createVote(@RequestParam Integer memberId, @RequestParam Integer signupId) {
        try {
            EventVote vote = eventVoteService.createVote(memberId, signupId);
            long updatedVoteCount = eventVoteService.getVoteCountForSignup(signupId);
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "投票成功", 
                "vote", vote,
                "updatedVoteCount", updatedVoteCount
            ));
        } catch (RuntimeException e) {
            logger.warn("投票失敗: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("投票過程中發生未預期的錯誤", e);
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "投票時發生系統錯誤，請稍後再試"));
        }
    }

    /**
     * API: 獲取特定投票記錄
     * @param voteId 投票ID
     * @return 投票記錄的 ResponseEntity
     */
    @GetMapping("/api/{voteId}")
    @ResponseBody
    public ResponseEntity<?> apiGetVote(@PathVariable Integer voteId) {
        try {
            EventVote vote = eventVoteService.getVote(voteId);
            return ResponseEntity.ok(vote);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * API: 更新投票記錄
     * @param voteId 投票ID
     * @param payload 包含新狀態的 Map
     * @return 更新結果的 ResponseEntity
     */
    @PutMapping("/api/{voteId}")
    @ResponseBody
    public ResponseEntity<?> apiUpdateVote(@PathVariable Integer voteId, @RequestBody Map<String, Integer> payload) {
        try {
            Integer eventVoteStatus = payload.get("eventVoteStatus");
            if (eventVoteStatus == null) {
                return ResponseEntity.badRequest().body("eventVoteStatus 是必需的");
            }
            EventVote updatedVote = eventVoteService.updateVote(voteId, eventVoteStatus);
            return ResponseEntity.ok(updatedVote);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API: 刪除投票記錄
     * @param voteId 投票ID
     * @return 刪除結果的 ResponseEntity
     */
    @DeleteMapping("/api/{voteId}")
    @ResponseBody
    public ResponseEntity<?> apiDeleteVote(@PathVariable Integer voteId) {
        try {
            eventVoteService.deleteVote(voteId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * API: 獲取所有投票
     * @return 所有投票的列表
     */
    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<List<EventVote>> apiGetAllVotes() {
        List<EventVote> votes = eventVoteService.getAllVotes();
        return ResponseEntity.ok(votes);
    }

    /**
     * API: 獲取特定報名的所有投票
     * @param signupId 報名ID
     * @return 特定報名的所有投票
     */
    @GetMapping("/api/signup/{signupId}")
    @ResponseBody
    public ResponseEntity<List<EventVote>> apiGetVotesBySignupId(@PathVariable Integer signupId) {
        List<EventVote> votes = eventVoteService.getVotesBySignupId(signupId);
        return ResponseEntity.ok(votes);
    }
    
    /**
     * API: 獲取特定活動的所有投票
     * @param eventId 活動ID
     * @return 特定活動的所有投票
     */
    @GetMapping("/api/event/{eventId}")
    @ResponseBody
    public ResponseEntity<List<EventVote>> getVotesByEvent(@PathVariable Integer eventId) {
        List<EventVote> votes = eventVoteService.getVotesByEventId(eventId);
        return ResponseEntity.ok(votes);
    }

    /**
     * API: 獲取特定用戶在特定活動中的投票次數
     * @param memId 會員ID
     * @param eventId 活動ID
     * @return 投票次數
     */
    @GetMapping("/api/count/{memId}/{eventId}")
    @ResponseBody
    public ResponseEntity<Long> getVoteCount(@PathVariable Integer memId, @PathVariable Integer eventId) {
        long count = eventVoteService.getVoteCountByMemberAndEvent(memId, eventId);
        return ResponseEntity.ok(count);
    }

    /**
     * API: 獲取特定活動的投票結果統計
     * @param eventId 活動ID
     * @return 投票結果統計
     */
    @GetMapping("/api/results/{eventId}")
    @ResponseBody
    public ResponseEntity<Map<Integer, Long>> getVoteResults(@PathVariable Integer eventId) {
        Map<Integer, Long> results = eventVoteService.getVoteResultsByEventId(eventId);
        return ResponseEntity.ok(results);
    }

    /**
     * API: 獲取投票統計信息
     * @return 投票統計信息的 ResponseEntity
     */
    @GetMapping("/api/statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getVoteStatistics() {
        List<EventVote> allVotes = eventVoteService.getAllVotes();
        
        long totalVotes = allVotes.size();
        long validVotes = allVotes.stream().filter(v -> v.getEventVoteStatus() == 1).count();
        long pendingVotes = totalVotes - validVotes;
        long activeUsers = allVotes.stream().map(v -> v.getMember().getMemId()).distinct().count();
        double avgVotesPerUser = activeUsers > 0 ? (double) totalVotes / activeUsers : 0;

        // 計算投票趨勢（最近7天）
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        Map<LocalDateTime, Long> voteTrendMap = allVotes.stream()
            .filter(v -> v.getVoteTime().isAfter(sevenDaysAgo))
            .collect(Collectors.groupingBy(
                EventVote::getVoteTime,
                Collectors.counting()
            ));

        List<Map<String, Object>> voteTrend = voteTrendMap.entrySet().stream()
            .map(entry -> {
                Map<String, Object> trendItem = new HashMap<>();
                trendItem.put("date", entry.getKey());
                trendItem.put("count", entry.getValue());
                return trendItem;
            })
            .collect(Collectors.toList());

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalVotes", totalVotes);
        statistics.put("validVotes", validVotes);
        statistics.put("pendingVotes", pendingVotes);
        statistics.put("activeUsers", activeUsers);
        statistics.put("avgVotesPerUser", avgVotesPerUser);
        statistics.put("voteTrend", voteTrend);

        return ResponseEntity.ok(statistics);
    }
}