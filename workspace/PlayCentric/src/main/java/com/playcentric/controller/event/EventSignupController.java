package com.playcentric.controller.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.playcentric.model.event.EventSignupDTO;
import com.playcentric.service.event.EventSignupService;

@Controller
@RequestMapping("/eventSignup")
public class EventSignupController {

    @Autowired
    private EventSignupService eventSignupService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<EventSignupDTO> createEventSignup(@RequestBody EventSignupDTO eventSignupDTO) {
        EventSignupDTO createdEventSignup = eventSignupService.createEventSignup(eventSignupDTO);
        return new ResponseEntity<>(createdEventSignup, HttpStatus.CREATED);
    }

    @GetMapping("/{signupId}")
    @ResponseBody
    public ResponseEntity<EventSignupDTO> getEventSignup(@PathVariable Integer signupId) {
        EventSignupDTO eventSignupDTO = eventSignupService.getEventSignupDTO(signupId);
        return eventSignupDTO != null ? new ResponseEntity<>(eventSignupDTO, HttpStatus.OK)
                                      : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<EventSignupDTO>> getAllEventSignups() {
        List<EventSignupDTO> eventSignupDTOS = eventSignupService.getAllEventSignups();
        return new ResponseEntity<>(eventSignupDTOS, HttpStatus.OK);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<EventSignupDTO> updateEventSignup(@RequestBody EventSignupDTO eventSignupDTO) {
        EventSignupDTO updatedEventSignup = eventSignupService.updateEventSignup(eventSignupDTO);
        return updatedEventSignup != null ? new ResponseEntity<>(updatedEventSignup, HttpStatus.OK)
                                          : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/delete/{signupId}")
    @ResponseBody
    public ResponseEntity<Void> deleteEventSignup(@PathVariable Integer signupId) {
        eventSignupService.deleteEventSignup(signupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}