//package com.playcentric.model.event;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
//@Table(name = "eventReward")
//public class EventReward {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer rewardId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "eventId")
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    private Event event;
//
//    private String rewardName;
//    private String rewardDescription;
//    private Integer rewardType;
//    private Integer rewardQuantity;
//}
