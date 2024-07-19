package com.playcentric.service.event;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.playcentric.model.event.Event;
import com.playcentric.model.event.EventSignup;
import com.playcentric.model.event.EventSignupRepository;
import com.playcentric.model.event.EventVote;
import com.playcentric.model.event.EventVoteRepository;
import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberRepository;

@Service
public class EventVoteService {

    @Autowired
    private EventVoteRepository eventVoteRepository;

    @Autowired
    private EventSignupRepository eventSignupRepository;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 創建新的投票
     * @param memberId 投票的會員ID
     * @param signupId 被投票的報名ID
     * @return 創建的投票記錄
     * @throws RuntimeException 如果投票不符合規則（例如重複投票、超過投票次數限制等）
     */
    @Transactional
    public EventVote createVote(Integer memberId, Integer signupId) {
        EventSignup eventSignup = eventSignupRepository.findById(signupId)
                .orElseThrow(() -> new RuntimeException("找不到指定的報名記錄"));

        Event event = eventSignup.getEvent();

        if (event.getEventStatus() != 1) {
            throw new RuntimeException("當前不是投票階段");
        }

        // 檢查是否重複投票
        boolean hasVoted = eventVoteRepository.findAll().stream()
                .anyMatch(vote -> vote.getMember().getMemId().equals(memberId) 
                        && vote.getEventSignup().getSignupId().equals(signupId));
        if (hasVoted) {
            throw new RuntimeException("您已經為此作品投過票了");
        }

        // 檢查投票次數是否達到上限
        long voteCount = eventVoteRepository.findAll().stream()
                .filter(vote -> vote.getMember().getMemId().equals(memberId) 
                        && vote.getEvent().getEventId().equals(event.getEventId()))
                .count();
        if (voteCount >= 5) {
            throw new RuntimeException("您在此活動中的投票次數已達上限");
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("找不到指定的會員"));

        EventVote eventVote = new EventVote();
        eventVote.setMember(member);
        eventVote.setEventSignup(eventSignup);
        eventVote.setEvent(event);
        eventVote.setEventVoteStatus(1);

        EventVote savedVote = eventVoteRepository.save(eventVote);

        eventSignup.setVoteCount(eventSignup.getVoteCount() + 1);
        eventSignupRepository.save(eventSignup);

        return savedVote;
    }

    /**
     * 讀取特定的投票記錄
     * @param voteId 投票ID
     * @return 投票記錄
     * @throws RuntimeException 如果找不到指定的投票記錄
     */
    public EventVote getVote(Integer voteId) {
        return eventVoteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("找不到指定的投票記錄"));
    }

    /**
     * 更新投票記錄
     * @param eventVote 更新後的投票記錄
     * @return 更新後的投票記錄
     * @throws RuntimeException 如果找不到指定的投票記錄
     */
    @Transactional
    public EventVote updateVote(Integer voteId, Integer eventVoteStatus) {
        EventVote existingVote = eventVoteRepository.findById(voteId)
            .orElseThrow(() -> new RuntimeException("找不到指定的投票記錄"));
        
        existingVote.setEventVoteStatus(eventVoteStatus);
        
        return eventVoteRepository.save(existingVote);
    }

    /**
     * 刪除投票記錄
     * @param voteId 要刪除的投票ID
     * @throws RuntimeException 如果找不到指定的投票記錄
     */
    @Transactional
    public void deleteVote(Integer voteId) {
        EventVote vote = eventVoteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("找不到指定的投票記錄"));

        EventSignup eventSignup = vote.getEventSignup();
        eventSignup.setVoteCount(eventSignup.getVoteCount() - 1);
        eventSignupRepository.save(eventSignup);

        eventVoteRepository.deleteById(voteId);
    }

    /**
     * 獲取特定報名的所有投票
     * @param signupId 報名ID
     * @return 投票列表
     */
    public List<EventVote> getVotesBySignupId(Integer signupId) {
        return eventVoteRepository.findAll().stream()
                .filter(vote -> vote.getEventSignup().getSignupId().equals(signupId))
                .collect(Collectors.toList());
    }

    /**
     * 獲取所有投票
     * @return 所有投票的列表
     */
    public List<EventVote> getAllVotes() {
        return eventVoteRepository.findAll();
    }
}