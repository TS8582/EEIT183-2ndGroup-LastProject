package com.playcentric.service.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.event.EventRepository;
import com.playcentric.model.event.EventSignupRepository;
import com.playcentric.model.event.EventVote;
import com.playcentric.model.event.EventVoteDTO;
import com.playcentric.model.event.EventVoteRepository;
import com.playcentric.model.member.MemberRepository;

@Service
public class EventVoteService {
    @Autowired
    private EventVoteRepository eventVoteRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EventSignupRepository eventSignupRepository;

    public EventVoteDTO createEventVote(EventVoteDTO eventVoteDTO) {
        EventVote eventVote = new EventVote();
        eventVote.setVoteId(eventVoteDTO.getVoteId());
        eventVote.setEvent(eventRepository.findById(eventVoteDTO.getEventId()).orElse(null));
        eventVote.setMember(memberRepository.findById(eventVoteDTO.getMemId()).orElse(null));
        eventVote.setEventSignup(eventSignupRepository.findById(eventVoteDTO.getSignupId()).orElse(null));

        EventVote savedEventVote = eventVoteRepository.save(eventVote);

        return new EventVoteDTO(savedEventVote.getVoteId(),
                                savedEventVote.getEvent().getEventId(),
                                savedEventVote.getMember().getMemId(),
                                savedEventVote.getEventSignup().getSignupId());
    }

    public EventVoteDTO getEventVoteById(Integer id) {
        Optional<EventVote> eventVoteOptional = eventVoteRepository.findById(id);
        if (eventVoteOptional.isPresent()) {
            EventVote eventVote = eventVoteOptional.get();
            return new EventVoteDTO(eventVote.getVoteId(),
                                    eventVote.getEvent().getEventId(),
                                    eventVote.getMember().getMemId(),
                                    eventVote.getEventSignup().getSignupId());
        }
        return null;
    }

    public List<EventVoteDTO> getAllEventVotes() {
        List<EventVote> eventVotes = eventVoteRepository.findAll();
        List<EventVoteDTO> eventVoteDTOs = new ArrayList<>();
        for (EventVote eventVote : eventVotes) {
            EventVoteDTO dto = new EventVoteDTO(eventVote.getVoteId(),
                                                eventVote.getEvent().getEventId(),
                                                eventVote.getMember().getMemId(),
                                                eventVote.getEventSignup().getSignupId());
            eventVoteDTOs.add(dto);
        }
        return eventVoteDTOs;
    }

    public EventVoteDTO updateEventVote(Integer id, EventVoteDTO eventVoteDTO) {
        Optional<EventVote> eventVoteOptional = eventVoteRepository.findById(id);
        if (eventVoteOptional.isPresent()) {
            EventVote eventVote = eventVoteOptional.get();
            eventVote.setVoteId(eventVoteDTO.getVoteId());
            eventVote.setEvent(eventRepository.findById(eventVoteDTO.getEventId()).orElse(null));
            eventVote.setMember(memberRepository.findById(eventVoteDTO.getMemId()).orElse(null));
            eventVote.setEventSignup(eventSignupRepository.findById(eventVoteDTO.getSignupId()).orElse(null));

            EventVote updatedEventVote = eventVoteRepository.save(eventVote);

            return new EventVoteDTO(updatedEventVote.getVoteId(),
                                    updatedEventVote.getEvent().getEventId(),
                                    updatedEventVote.getMember().getMemId(),
                                    updatedEventVote.getEventSignup().getSignupId());
        }
        return null;
    }

    
    public boolean deleteEventVote(Integer id) {
        Optional<EventVote> eventVoteOptional = eventVoteRepository.findById(id);
        if (eventVoteOptional.isPresent()) {
            eventVoteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
