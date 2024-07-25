package com.playcentric.model.event;

import com.playcentric.model.member.Member;

import lombok.Data;

@Data
public class EventSignupDTO {
    private Integer signupId;
    private String workTitle;
    private String workDescription;
    private Long voteCount;  // 改為 Long 類型
    private String workImageBase64;
    private Member member;
    
    //添加接受 long 參數的 setVoteCount 方法
    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }
}