package com.playcentric.controller.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playcentric.model.event.EventVoteDTO;
import com.playcentric.service.event.EventVoteService;

@RestController
@RequestMapping("/eventVotes")
public class EventVoteController {
    @Autowired
    private EventVoteService eventVoteService;

    @PostMapping("/create")
    public ResponseEntity<EventVoteDTO> createEventVote(@RequestBody EventVoteDTO eventVoteDTO) {
        EventVoteDTO createdEventVote = eventVoteService.createEventVote(eventVoteDTO);
        if (createdEventVote != null) {
            return new ResponseEntity<>(createdEventVote, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<EventVoteDTO> getEventVoteById(@PathVariable Integer id) {
        EventVoteDTO eventVoteDTO = eventVoteService.getEventVoteById(id);
        if (eventVoteDTO != null) {
            return new ResponseEntity<>(eventVoteDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find")
    public ResponseEntity<List<EventVoteDTO>> getAllEventVotes() {
        List<EventVoteDTO> eventVoteDTOs = eventVoteService.getAllEventVotes();
        return new ResponseEntity<>(eventVoteDTOs, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EventVoteDTO> updateEventVote(@PathVariable Integer id, @RequestBody EventVoteDTO eventVoteDTO) {
        EventVoteDTO updatedEventVote = eventVoteService.updateEventVote(id, eventVoteDTO);
        if (updatedEventVote != null) {
            return new ResponseEntity<>(updatedEventVote, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEventVote(@PathVariable Integer id) {
        boolean deleted = eventVoteService.deleteEventVote(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

