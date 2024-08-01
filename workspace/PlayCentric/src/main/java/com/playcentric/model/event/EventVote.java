package com.playcentric.model.event;

import java.time.LocalDateTime;

import com.playcentric.model.member.Member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 活動投票實體類
 * 代表用戶對某個活動報名的投票
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "eventVote")
public class EventVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteId;

    @ManyToOne
    @JoinColumn(name = "signupId")
    private EventSignup eventSignup;

    @ManyToOne
    @JoinColumn(name = "memId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;

    private Integer eventVoteStatus;
    private LocalDateTime voteTime;
}