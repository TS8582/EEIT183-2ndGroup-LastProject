package com.playcentric.model.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
    /**
     * 根據活動狀態查找活動，並按開始時間降序排序
     */
    List<Event> findByEventStatusOrderByEventStartTimeDesc(Integer status);

    /**
     * 根據活動狀態查找活動，並按結束時間降序排序
     */
    List<Event> findByEventStatusOrderByEventEndTimeDesc(Integer status);

    /**
     * 查找指定狀態且結束時間早於給定時間的所有活動
     */
    List<Event> findByEventStatusAndEventEndTimeBefore(Integer status, LocalDateTime endTime);

    /**
     * 查找下一個即將結束的活動
     */
    Optional<Event> findTopByEventStatusAndEventEndTimeAfterOrderByEventEndTimeAsc(Integer status, LocalDateTime time);
    
    /**
     * 根據狀態查找活動
     */
    List<Event> findByEventStatus(Integer status);
}