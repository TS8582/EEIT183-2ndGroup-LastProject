package com.playcentric.controller.event;

import java.util.List;
import java.util.stream.Collectors;

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

import com.playcentric.model.event.EventSignup;
import com.playcentric.model.event.EventSignupDTO;
import com.playcentric.service.event.EventSignupService;

@RestController
@RequestMapping("/eventSignup")
public class EventSignupController {

    @Autowired
    private EventSignupService eventSignupService;

    @PostMapping("/create")
    public ResponseEntity<EventSignupDTO> createEventSignup(@RequestBody EventSignupDTO eventSignupDTO) {
        EventSignupDTO createdEventSignup = eventSignupService.createEventSignup(eventSignupDTO);
        return new ResponseEntity<>(createdEventSignup, HttpStatus.CREATED);
    }

    @GetMapping("/{signupId}")
    public ResponseEntity<EventSignupDTO> getEventSignup(@PathVariable Integer signupId) {
        EventSignupDTO eventSignupDTO = eventSignupService.getEventSignupDTO(signupId);
        return eventSignupDTO != null ? new ResponseEntity<>(eventSignupDTO, HttpStatus.OK)
                                      : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventSignupDTO>> getAllEventSignups() {
        List<EventSignupDTO> eventSignupDTOS = eventSignupService.getAllEventSignups();
        return new ResponseEntity<>(eventSignupDTOS, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<EventSignupDTO> updateEventSignup(@RequestBody EventSignupDTO eventSignupDTO) {
        EventSignupDTO updatedEventSignup = eventSignupService.updateEventSignup(eventSignupDTO);
        return updatedEventSignup != null ? new ResponseEntity<>(updatedEventSignup, HttpStatus.OK)
                                          : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{signupId}")
    public ResponseEntity<Void> deleteEventSignup(@PathVariable Integer signupId) {
        eventSignupService.deleteEventSignup(signupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}