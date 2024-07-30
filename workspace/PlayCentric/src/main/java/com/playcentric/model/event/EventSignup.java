package com.playcentric.model.event;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "eventSignup")
public class EventSignup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer signupId;
    
    @ManyToOne
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private Member member;
    
    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy年MM月dd日 HH:mm")
    // @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
//    @Temporal(TemporalType.TIMESTAMP) 
    private LocalDateTime signupTime;
    
    private Integer workType;
    private String workTitle;
    private String workDescription;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime workUploadTime;

    @JsonIgnore
    @Lob
    private byte[] workImage;
    private Integer voteCount;
    private Integer eventSignupStatus;
}
