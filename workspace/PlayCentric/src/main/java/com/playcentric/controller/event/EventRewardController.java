package com.playcentric.controller.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playcentric.model.event.EventReward;
import com.playcentric.service.event.EventRewardService;

@RestController
@RequestMapping("/eventReward")
public class EventRewardController {
    @Autowired
    private EventRewardService eventRewardService;

    // 創建活動獎勵
    @PostMapping("/create")
    public EventReward createEventReward(@RequestBody EventReward eventReward) {
        return eventRewardService.createEventReward(eventReward);
    }

    // 根據ID查找活動獎勵
    @GetMapping("/get/{rewardId}")
    public EventReward getEventReward(@PathVariable Integer rewardId) {
        return eventRewardService.getEventReward(rewardId);
    }

    // 查找所有活動獎勵
    @GetMapping("/find")
    public List<EventReward> getAllEventRewards() {
        return eventRewardService.getAllEventRewards();
    }

    // 刪除活動獎勵
    @DeleteMapping("/delete/{rewardId}")
    public void deleteEventReward(@PathVariable Integer rewardId) {
        eventRewardService.deleteEventReward(rewardId);
    }
}
