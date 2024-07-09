package com.playcentric.service.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.event.EventVote;
import com.playcentric.model.event.EventVoteRepository;

@Service
public class EventVoteService {
    @Autowired
    private EventVoteRepository eventVoteRepository;

    // 創建活動投票
    public EventVote createEventVote(EventVote eventVote) {
        return eventVoteRepository.save(eventVote);
    }

    // 根據ID查找活動投票
    public EventVote getEventVote(Integer voteId) {
        return eventVoteRepository.findById(voteId).orElse(null);
    }

    // 查找所有活動投票
    public List<EventVote> getAllEventVotes() {
        return eventVoteRepository.findAll();
    }

    // 刪除活動投票
    public void deleteEventVote(Integer voteId) {
        eventVoteRepository.deleteById(voteId);
    }
}