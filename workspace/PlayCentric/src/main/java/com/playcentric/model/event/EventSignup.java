package com.playcentric.model.event;

import java.time.LocalDateTime;

import com.playcentric.model.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "eventSignup")
public class EventSignup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer signupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private Event event;

    private LocalDateTime signupTime;
    private Integer work;
    private Integer workType;
    private String workTitle;
    private String workDescription;
    private LocalDateTime workUploadTime;
    private Integer voteCount;
}
