package com.playcentric.service.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.event.EventReward;
import com.playcentric.model.event.EventRewardRepository;

@Service
public class EventRewardService {
    @Autowired
    private EventRewardRepository eventRewardRepository;

    // 創建活動獎勵
    public EventReward createEventReward(EventReward eventReward) {
        return eventRewardRepository.save(eventReward);
    }

    // 根據ID查找活動獎勵
    public EventReward getEventReward(Integer rewardId) {
        return eventRewardRepository.findById(rewardId).orElse(null);
    }

    // 查找所有活動獎勵
    public List<EventReward> getAllEventRewards() {
        return eventRewardRepository.findAll();
    }

    // 刪除活動獎勵
    public void deleteEventReward(Integer rewardId) {
        eventRewardRepository.deleteById(rewardId);
    }
}