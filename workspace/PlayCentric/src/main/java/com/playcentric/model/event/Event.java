package com.playcentric.model.event;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.playcentric.model.ImageLib;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 活動實體類
 * 代表系統中的一個活動
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;
    
    private String eventName;
    private String eventDescription;
    private Integer eventType;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime eventStartTime;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime eventEndTime;
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime eventSignupDeadLine;
    
    // 設置默認值為 0（未開始）
    private Integer eventStatus = 0; 
    
    @ManyToOne
    @JoinColumn(name = "eventImage")
    private ImageLib eventImage;
    
    @Column(name = "reviewStatus")
    private Integer reviewStatus = 0; // 預設值為0,表示未審核
    
    /**
     * 計算活動當前狀態
     * @return 0: 未開始, 1: 進行中, 2: 已結束
     */
    public Integer calculateEventStatus() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(this.eventStartTime)) {
            return 0; // 未開始
        } else if (now.isAfter(this.eventEndTime)) {
            return 2; // 已結束
        } else {
            return 1; // 進行中
        }
    }

    /**
     * 安全地獲取狀態
     * @return 活動狀態，如果為null則返回0
     */
    public Integer getEventStatusSafe() {
        return eventStatus != null ? eventStatus : 0;
    }
}