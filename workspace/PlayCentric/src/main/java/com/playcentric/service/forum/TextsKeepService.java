package com.playcentric.service.forum;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.forum.Texts;
import com.playcentric.model.forum.TextsKeep;
import com.playcentric.model.forum.TextsKeepId;
import com.playcentric.model.forum.TextsKeepRepository;
import com.playcentric.model.forum.TextsRepository;
import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberRepository;

@Service
public class TextsKeepService {

    @Autowired
    private TextsKeepRepository textsKeepRepository;

    @Autowired
    private TextsRepository textsRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    public String keepText(TextsKeepId id){
        Optional<TextsKeep> optional = textsKeepRepository.findById(id);
        if (optional.isPresent()) {
            textsKeepRepository.delete(optional.get());
            return "取消收藏!";
        }
        try {
            Member member = memberRepository.findById(id.getMemId()).get();
            Texts texts = textsRepository.findById(id.getTextsId()).get();
            TextsKeep textsKeep = new TextsKeep();
            textsKeep.setTextsKeepId(id);
            textsKeep.setMember(member);
            textsKeep.setTexts(texts);
            textsKeepRepository.save(textsKeep);
            return "收藏成功!";
        } catch (Exception e) {
            e.printStackTrace();
            return "收藏失敗!";
        }
    }

    public boolean checkTextKept(TextsKeepId id){
        Optional<TextsKeep> optional = textsKeepRepository.findById(id);
        return optional.isPresent();
    }

    public Integer getKeepNum(Integer textsId){
        Texts texts = textsRepository.findById(textsId).get();
        return textsKeepRepository.findByTexts(texts).size();
    }

    public List<TextsKeep> getMemKeepTexts(Integer memId){
        Member member = memberRepository.findById(memId).get();
        return textsKeepRepository.findByMember(member);
    }
}
