package com.playcentric.service.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.event.EventRewardType;
import com.playcentric.model.event.EventRewardTypeRepository;

@Service
public class EventRewardTypeService {
    @Autowired
    private EventRewardTypeRepository eventRewardTypeRepository;

    // 創建活動獎勵類型
    public EventRewardType createEventRewardType(EventRewardType eventRewardType) {
        return eventRewardTypeRepository.save(eventRewardType);
    }

    // 根據ID查找活動獎勵類型
    public EventRewardType getEventRewardType(Integer rewardTypeId) {
        return eventRewardTypeRepository.findById(rewardTypeId).orElse(null);
    }

    // 查找所有活動獎勵類型
    public List<EventRewardType> getAllEventRewardTypes() {
        return eventRewardTypeRepository.findAll();
    }

    // 刪除活動獎勵類型
    public void deleteEventRewardType(Integer rewardTypeId) {
        eventRewardTypeRepository.deleteById(rewardTypeId);
    }
    
    // 更新活動獎勵類型
    public EventRewardType updateEventRewardType(EventRewardType eventRewardType) {
        // 檢查是否存在該獎勵類型
        EventRewardType existingType = eventRewardTypeRepository.findById(eventRewardType.getRewardTypeId()).orElse(null);
        if (existingType == null) {
            return null; // 如果不存在，返回 null 或者進行錯誤處理
        }
        // 更新獎勵類型的屬性
        existingType.setRewardTypeName(eventRewardType.getRewardTypeName());
        // 儲存並返回更新後的獎勵類型
        return eventRewardTypeRepository.save(existingType);
    }
}