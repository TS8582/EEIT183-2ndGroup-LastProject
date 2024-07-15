//package com.playcentric.controller.event;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.playcentric.model.event.EventReward;
//import com.playcentric.service.event.EventRewardService;
//
//@Controller
//@RequestMapping("/eventReward")
//public class EventRewardController {
//    @Autowired
//    private EventRewardService eventRewardService;
//
//    // 創建活動獎勵
//    @PostMapping("/create")
//    @ResponseBody
//    public EventReward createEventReward(@RequestBody EventReward eventReward) {
//        return eventRewardService.createEventReward(eventReward);
//    }
//
//    // 根據ID查找活動獎勵
//    @GetMapping("/get/{rewardId}")
//    @ResponseBody
//    public EventReward getEventReward(@PathVariable Integer rewardId) {
//        return eventRewardService.getEventReward(rewardId);
//    }
//
//    // 查找所有活動獎勵
//    @GetMapping("/find")
//    @ResponseBody
//    public List<EventReward> getAllEventRewards() {
//        return eventRewardService.getAllEventRewards();
//    }
//
//    // 刪除活動獎勵
//    @PostMapping("/delete/{rewardId}")
//    @ResponseBody
//    public void deleteEventReward(@PathVariable Integer rewardId) {
//        eventRewardService.deleteEventReward(rewardId);
//    }
//}
