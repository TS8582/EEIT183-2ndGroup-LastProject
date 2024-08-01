package com.playcentric.model.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 活動儲存庫介面
 * 提供對活動實體的數據庫操作
 */
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

    /**
     * 查找下一個即將結束的活動
     * @param status 活動狀態
     * @param time 當前時間
     * @return 下一個即將結束的活動
     */
    Optional<Event> findTopByEventStatusAndEventEndTimeAfterOrderByEventEndTimeAsc(Integer status, LocalDateTime time);
    
    /**
     * 根據狀態查找活動
     * @param status 活動狀態
     * @return 符合條件的活動列表
     */
    List<Event> findByEventStatus(Integer status);
    
    /**
     * 根據活動狀態和審核狀態查找活動
     * @param eventStatus 活動狀態
     * @param reviewStatus 審核狀態
     * @return 符合條件的活動列表
     */
    List<Event> findByEventStatusAndReviewStatus(Integer eventStatus, Integer reviewStatus);
}