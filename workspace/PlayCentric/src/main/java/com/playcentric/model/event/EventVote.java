//package com.playcentric.model.event;
//
//import com.playcentric.model.member.Member;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "eventVote")
//public class EventVote {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer voteId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "signupId")
//    private EventSignup eventSignup;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "memId")
//    private Member member;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "eventId")
//    private Event event;
//}