package com.playcentric.controller.event;

import com.playcentric.model.event.EventVote;
import com.playcentric.service.event.EventVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/eventVotes")
public class EventVoteController {

    @Autowired
    private EventVoteService eventVoteService;

    /**
     * 顯示投票列表頁面
     */
    @GetMapping("/list")
    public String listVotes(Model model) {
        List<EventVote> votes = eventVoteService.getAllVotes();
        model.addAttribute("votes", votes);
        return "event/vote-list";
    }

    /**
     * 顯示創建投票的表單
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("eventVote", new EventVote());
        return "event/vote-form";
    }

    /**
     * 處理創建投票的表單提交（返回視圖）
     */
    @PostMapping("/create")
    public String createVoteForm(@RequestParam Integer memberId, @RequestParam Integer signupId, Model model) {
        try {
            EventVote vote = eventVoteService.createVote(memberId, signupId);
            return "redirect:/eventVotes/list";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "event/vote-form";
        }
    }

    /**
     * 顯示特定投票的詳情
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
     */
    @GetMapping("/edit/{voteId}")
    public String showEditForm(@PathVariable Integer voteId, Model model) {
        EventVote vote = eventVoteService.getVote(voteId);
        model.addAttribute("vote", vote);
        return "event/vote-form";
    }

    /**
     * 處理更新投票的表單提交
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
     */
    @GetMapping("/signup/{signupId}")
    public String getVotesBySignup(@PathVariable Integer signupId, Model model) {
        List<EventVote> votes = eventVoteService.getVotesBySignupId(signupId);
        model.addAttribute("votes", votes);
        return "event/vote-list";
    }

    // API 部分

    /**
     * API: 創建新的投票
     */
    @PostMapping("/api/create")
    @ResponseBody
    public ResponseEntity<?> apiCreateVote(@RequestParam Integer memberId, @RequestParam Integer signupId) {
        try {
            EventVote vote = eventVoteService.createVote(memberId, signupId);
            return ResponseEntity.ok(Map.of("success", true, "message", "投票成功", "vote", vote));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * API: 獲取特定投票記錄
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
     */
    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<List<EventVote>> apiGetAllVotes() {
        List<EventVote> votes = eventVoteService.getAllVotes();
        return ResponseEntity.ok(votes);
    }

    /**
     * API: 獲取特定報名的所有投票
     */
    @GetMapping("/api/signup/{signupId}")
    @ResponseBody
    public ResponseEntity<List<EventVote>> apiGetVotesBySignupId(@PathVariable Integer signupId) {
        List<EventVote> votes = eventVoteService.getVotesBySignupId(signupId);
        return ResponseEntity.ok(votes);
    }
    
    /**
     * API: 獲取特定活動的所有投票
     */
    @GetMapping("/api/event/{eventId}")
    @ResponseBody
    public ResponseEntity<List<EventVote>> getVotesByEvent(@PathVariable Integer eventId) {
        List<EventVote> votes = eventVoteService.getVotesByEventId(eventId);
        return ResponseEntity.ok(votes);
    }

    /**
     * API: 獲取特定用戶在特定活動中的投票次數
     */
    @GetMapping("/api/count/{memId}/{eventId}")
    @ResponseBody
    public ResponseEntity<Long> getVoteCount(@PathVariable Integer memId, @PathVariable Integer eventId) {
        long count = eventVoteService.getVoteCountByMemberAndEvent(memId, eventId);
        return ResponseEntity.ok(count);
    }

    /**
     * API: 獲取特定活動的投票結果統計
     */
    @GetMapping("/api/results/{eventId}")
    @ResponseBody
    public ResponseEntity<Map<Integer, Long>> getVoteResults(@PathVariable Integer eventId) {
        Map<Integer, Long> results = eventVoteService.getVoteResultsByEventId(eventId);
        return ResponseEntity.ok(results);
    }
}