package com.playcentric.controller.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.playcentric.model.event.EventVote;
import com.playcentric.service.event.EventVoteService;

@Controller
@RequestMapping("/eventVote")
public class EventVoteController {
    @Autowired
    private EventVoteService eventVoteService;

    // 創建活動投票
    @PostMapping("/create")
    public EventVote createEventVote(@RequestBody EventVote eventVote) {
        return eventVoteService.createEventVote(eventVote);
    }

    // 根據ID查找活動投票
    @GetMapping("/get/{voteId}")
    public EventVote getEventVote(@PathVariable Integer voteId) {
        return eventVoteService.getEventVote(voteId);
    }

    // 查找所有活動投票
    @GetMapping("/find")
    public List<EventVote> getAllEventVotes() {
        return eventVoteService.getAllEventVotes();
    }

    // 刪除活動投票
    @DeleteMapping("/delete/{voteId}")
    public void deleteEventVote(@PathVariable Integer voteId) {
        eventVoteService.deleteEventVote(voteId);
    }
}