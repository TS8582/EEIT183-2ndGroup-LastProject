package com.playcentric.model.event;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.playcentric.model.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
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
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 若要在 thymeleaf 強制使用本格式，需雙層大括號
	@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime signupTime;
    
    @Transient
    private String workPhotoUrl;
    
    private Integer workType;
    private String workTitle;
    private String workDescription;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime workUploadTime;
    
    private Integer voteCount;
}
