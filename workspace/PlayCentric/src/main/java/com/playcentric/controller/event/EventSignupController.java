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

    // 創建活動報名
    @PostMapping("/create")
    public ResponseEntity<EventSignupDTO> createEventSignup(@RequestBody EventSignupDTO eventSignupDTO) {
        EventSignup eventSignup = eventSignupService.createEventSignup(eventSignupDTO);
        return new ResponseEntity<>(new EventSignupDTO(eventSignup), HttpStatus.CREATED);
    }

    // 根據ID查找活動報名
    @GetMapping("/{signupId}")
    public ResponseEntity<EventSignupDTO> getEventSignup(@PathVariable Integer signupId) {
        EventSignupDTO eventSignupDTO = eventSignupService.getEventSignupDTO(signupId);
        return eventSignupDTO != null ? ResponseEntity.ok(eventSignupDTO) : ResponseEntity.notFound().build();
    }

    // 查找所有活動報名
    @GetMapping("/all")
    public ResponseEntity<List<EventSignupDTO>> getAllEventSignups() {
        List<EventSignupDTO> eventSignupDTOList = eventSignupService.getAllEventSignups()
                .stream()
                .map(EventSignupDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventSignupDTOList);
    }

    // 更新活動報名
    @PutMapping("/update")
    public ResponseEntity<EventSignupDTO> updateEventSignup(@RequestBody EventSignupDTO eventSignupDTO) {
        EventSignup updatedEventSignup = eventSignupService.updateEventSignup(eventSignupDTO);
        return new ResponseEntity<>(new EventSignupDTO(updatedEventSignup), HttpStatus.OK);
    }

    // 刪除活動報名
    @DeleteMapping("/delete/{signupId}")
    public ResponseEntity<Void> deleteEventSignup(@PathVariable Integer signupId) {
        eventSignupService.deleteEventSignup(signupId);
        return ResponseEntity.noContent().build();
    }
}