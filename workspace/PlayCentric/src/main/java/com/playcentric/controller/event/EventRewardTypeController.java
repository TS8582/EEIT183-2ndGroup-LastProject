package com.playcentric.controller.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.playcentric.model.event.EventRewardType;
import com.playcentric.service.event.EventRewardTypeService;

@Controller
@RequestMapping("/eventRewardType")
public class EventRewardTypeController {
    @Autowired
    private EventRewardTypeService eventRewardTypeService;

    // 創建活動獎勵類型
    @PostMapping("/create")
    @ResponseBody
    public EventRewardType createEventRewardType(@RequestBody EventRewardType eventRewardType) {
        return eventRewardTypeService.createEventRewardType(eventRewardType);
    }

    // 根據ID查找活動獎勵類型
    @GetMapping("/get/{rewardTypeId}")
    @ResponseBody
    public EventRewardType getEventRewardType(@PathVariable Integer rewardTypeId) {
        return eventRewardTypeService.getEventRewardType(rewardTypeId);
    }

    // 查找所有活動獎勵類型
    @GetMapping("/find")
    @ResponseBody
    public List<EventRewardType> getAllEventRewardTypes() {
        return eventRewardTypeService.getAllEventRewardTypes();
    }
    
    // 更新活動獎勵類型
    @PostMapping("/update/{rewardTypeId}")
    @ResponseBody
    public EventRewardType updateEventRewardType(@PathVariable Integer rewardTypeId, @RequestBody EventRewardType eventRewardType) {
        eventRewardType.setRewardTypeId(rewardTypeId);
        return eventRewardTypeService.updateEventRewardType(eventRewardType);
    }
    
    // 刪除活動獎勵類型
    @PostMapping("/delete/{rewardTypeId}")
    @ResponseBody
    public void deleteEventRewardType(@PathVariable Integer rewardTypeId) {
        eventRewardTypeService.deleteEventRewardType(rewardTypeId);
    }
}