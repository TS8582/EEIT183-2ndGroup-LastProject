package com.playcentric.model.event;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;
    
    private String eventName;
    private String eventDescription;
    private Integer eventType;
    private LocalDateTime eventStartTime;
    private LocalDateTime eventEndTime;
    private LocalDateTime eventSignupDeadLine;
}