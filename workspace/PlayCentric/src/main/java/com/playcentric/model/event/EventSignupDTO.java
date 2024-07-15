//package com.playcentric.model.event;
//
//
//import java.time.LocalDateTime;
//
//import lombok.Data;
//
//@Data
//public class EventSignupDTO {
//    private Integer signupId;
//    private Integer memId;
//    private Integer eventId;
//    private LocalDateTime signupTime;
//    private Integer work;
//    private Integer workType;
//    private String workTitle;
//    private String workDescription;
//    private LocalDateTime workUploadTime;
//    private Integer voteCount;
//
//    public EventSignupDTO() {
//    }
//
//    public EventSignupDTO(EventSignup eventSignup) {
//        this.signupId = eventSignup.getSignupId();
//        this.memId = eventSignup.getMember().getMemId();
//        this.eventId = eventSignup.getEvent().getEventId();
//        this.signupTime = eventSignup.getSignupTime();
//        this.work = eventSignup.getWork();
//        this.workType = eventSignup.getWorkType();
//        this.workTitle = eventSignup.getWorkTitle();
//        this.workDescription = eventSignup.getWorkDescription();
//        this.workUploadTime = eventSignup.getWorkUploadTime();
//        this.voteCount = eventSignup.getVoteCount();
//    }
//}
