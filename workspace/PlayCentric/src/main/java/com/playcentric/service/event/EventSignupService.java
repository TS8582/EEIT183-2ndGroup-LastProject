package com.playcentric.service.event;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventRepository;
import com.playcentric.model.event.EventSignup;
import com.playcentric.model.event.EventSignupDTO;
import com.playcentric.model.event.EventSignupRepository;
import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberRepository;

@Service
public class EventSignupService {

    @Autowired
    private EventSignupRepository eventSignupRepository;

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    public EventSignupDTO createEventSignup(EventSignupDTO eventSignupDTO) {
        EventSignup eventSignup = new EventSignup();
        
        // Load Event object based on eventId
        Event event = eventRepository.findById(eventSignupDTO.getEventId())
                                      .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        
        // Load Member object based on memId
        Member member = memberRepository.findById(eventSignupDTO.getMemId())
                                        .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        
        eventSignup.setEvent(event);
        eventSignup.setMember(member);
        
        eventSignup.setSignupTime(eventSignupDTO.getSignupTime());
        eventSignup.setVoteCount(eventSignupDTO.getVoteCount());
        eventSignup.setWork(eventSignupDTO.getWork());
        eventSignup.setWorkDescription(eventSignupDTO.getWorkDescription());
        eventSignup.setWorkTitle(eventSignupDTO.getWorkTitle());
        eventSignup.setWorkType(eventSignupDTO.getWorkType());
        eventSignup.setWorkUploadTime(eventSignupDTO.getWorkUploadTime());
        
        eventSignupRepository.save(eventSignup);
        
        return eventSignupDTO;
    }

    public EventSignupDTO getEventSignupDTO(Integer signupId) {
        EventSignup eventSignup = eventSignupRepository.findById(signupId).orElse(null);
        return eventSignup != null ? new EventSignupDTO(eventSignup) : null;
    }

    public List<EventSignupDTO> getAllEventSignups() {
        return eventSignupRepository.findAll().stream()
                .map(EventSignupDTO::new)
                .collect(Collectors.toList());
    }

    public EventSignupDTO updateEventSignup(EventSignupDTO eventSignupDTO) {
        EventSignup eventSignup = eventSignupRepository.findById(eventSignupDTO.getSignupId()).orElse(null);
        if (eventSignup != null) {
            eventSignup.setMember(eventSignup.getMember());
            eventSignup.setEvent(eventSignup.getEvent());
            eventSignup.setSignupTime(eventSignupDTO.getSignupTime());
            eventSignup.setWork(eventSignupDTO.getWork());
            eventSignup.setWorkType(eventSignupDTO.getWorkType());
            eventSignup.setWorkTitle(eventSignupDTO.getWorkTitle());
            eventSignup.setWorkDescription(eventSignupDTO.getWorkDescription());
            eventSignup.setWorkUploadTime(eventSignupDTO.getWorkUploadTime());
            eventSignup.setVoteCount(eventSignupDTO.getVoteCount());
            EventSignup updatedEventSignup = eventSignupRepository.save(eventSignup);
            return new EventSignupDTO(updatedEventSignup);
        }
        return null;
    }

    public void deleteEventSignup(Integer signupId) {
        eventSignupRepository.deleteById(signupId);
    }
}