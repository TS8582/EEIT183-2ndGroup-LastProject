package com.playcentric.model.event;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.playcentric.model.member.Member;

/**
 * 活動報名儲存庫介面
 * 提供對活動報名實體的數據庫操作
 */
public interface EventSignupRepository extends JpaRepository<EventSignup, Integer> {
    /**
     * 根據活動ID查找所有報名
     * @param eventId 活動ID
     * @return 該活動的所有報名列表
     */
    List<EventSignup> findByEvent_EventId(Integer eventId);

    /**
     * 根據會員ID查找所有報名
     * @param memberId 會員ID
     * @return 該會員的所有報名列表
     */
    List<EventSignup> findByMember_MemId(Integer memberId);

    /**
     * 檢查特定會員是否已經報名特定活動
     * @param memId 會員ID
     * @param eventId 活動ID
     * @return 如果已經報名則返回true，否則返回false
     */
    boolean existsByMember_MemIdAndEvent_EventId(Integer memId, Integer eventId);

    /**
     * 分頁查詢特定會員的報名記錄
     * @param member 會員
     * @param pageable 分頁參數
     * @return 該會員的報名記錄分頁
     */
    Page<EventSignup> findByMember(Member member, Pageable pageable);
    
    /**
     * 檢查是否存在與指定活動ID相關的報名記錄
     * @param eventId 活動ID
     * @return 如果存在相關報名記錄則返回true，否則返回false
     */
    boolean existsByEvent_EventId(Integer eventId);
}