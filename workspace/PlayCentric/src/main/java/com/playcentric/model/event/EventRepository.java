package com.playcentric.model.event;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

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
}