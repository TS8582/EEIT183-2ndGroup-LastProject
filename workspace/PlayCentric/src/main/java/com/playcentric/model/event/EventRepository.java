package com.playcentric.model.event;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
    /**
     * 根據活動狀態查找活動，並按開始時間降序排序
     * @param status 活動狀態
     * @return 符合條件的活動列表
     */
    List<Event> findByEventStatusOrderByEventStartTimeDesc(Integer status);

    /**
     * 根據活動狀態查找活動，並按結束時間降序排序
     * @param status 活動狀態
     * @return 符合條件的活動列表
     */
    List<Event> findByEventStatusOrderByEventEndTimeDesc(Integer status);

    /**
     * 查找指定狀態且結束時間早於給定時間的所有活動
     * @param status 活動狀態
     * @param endTime 結束時間
     * @return 符合條件的活動列表
     */
    List<Event> findByEventStatusAndEventEndTimeBefore(Integer status, LocalDateTime endTime);
}