package com.playcentric.model.event;

import com.playcentric.model.member.Member;

import lombok.Data;

@Data
public class EventSignupDTO {
    private Integer signupId;
    private String workTitle;
    private String workDescription;
    private Long voteCount;
    private String workImageBase64;
    private Member member;
    private Integer eventSignupStatus;
    
    /**
     * 設置投票數量
     * @param voteCount 投票數量
     */
    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }
    
}