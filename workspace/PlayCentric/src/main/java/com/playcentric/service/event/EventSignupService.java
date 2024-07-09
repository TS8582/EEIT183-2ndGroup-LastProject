package com.playcentric.service.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventSignup;
import com.playcentric.model.event.EventSignupDTO;
import com.playcentric.model.event.EventSignupRepository;
import com.playcentric.model.member.Member;

@Service
public class EventSignupService {

    @Autowired
    private EventSignupRepository eventSignupRepository;

    // 創建活動報名
    public EventSignup createEventSignup(EventSignupDTO eventSignupDTO) {
        EventSignup eventSignup = new EventSignup();
        eventSignup.setMember(new Member()); // 假設有適當的構造函數
        eventSignup.setEvent(new Event()); // 假設有適當的構造函數
        eventSignup.setSignupTime(eventSignupDTO.getSignupTime());
        eventSignup.setWork(eventSignupDTO.getWork());
        eventSignup.setWorkType(eventSignupDTO.getWorkType());
        eventSignup.setWorkTitle(eventSignupDTO.getWorkTitle());
        eventSignup.setWorkDescription(eventSignupDTO.getWorkDescription());
        eventSignup.setWorkUploadTime(eventSignupDTO.getWorkUploadTime());
        eventSignup.setVoteCount(eventSignupDTO.getVoteCount());
        return eventSignupRepository.save(eventSignup);
    }

    // 根據ID查找活動報名
    public EventSignupDTO getEventSignupDTO(Integer signupId) {
        EventSignup eventSignup = eventSignupRepository.findById(signupId).orElse(null);
        return eventSignup != null ? new EventSignupDTO(eventSignup) : null;
    }

    // 查找所有活動報名
    public List<EventSignup> getAllEventSignups() {
        return eventSignupRepository.findAll();
    }

    // 更新活動報名
    public EventSignup updateEventSignup(EventSignupDTO eventSignupDTO) {
        EventSignup eventSignup = eventSignupRepository.findById(eventSignupDTO.getSignupId()).orElse(null);
        if (eventSignup != null) {
            eventSignup.setMember(new Member()); // 假設有適當的構造函數
            eventSignup.setEvent(new Event()); // 假設有適當的構造函數
            eventSignup.setSignupTime(eventSignupDTO.getSignupTime());
            eventSignup.setWork(eventSignupDTO.getWork());
            eventSignup.setWorkType(eventSignupDTO.getWorkType());
            eventSignup.setWorkTitle(eventSignupDTO.getWorkTitle());
            eventSignup.setWorkDescription(eventSignupDTO.getWorkDescription());
            eventSignup.setWorkUploadTime(eventSignupDTO.getWorkUploadTime());
            eventSignup.setVoteCount(eventSignupDTO.getVoteCount());
            return eventSignupRepository.save(eventSignup);
        }
        return null;
    }

    // 刪除活動報名
    public void deleteEventSignup(Integer signupId) {
        eventSignupRepository.deleteById(signupId);
    }
}