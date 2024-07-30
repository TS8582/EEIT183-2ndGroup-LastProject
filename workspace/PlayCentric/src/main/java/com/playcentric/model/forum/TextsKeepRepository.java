package com.playcentric.model.forum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.playcentric.model.member.Member;




public interface TextsKeepRepository extends JpaRepository<TextsKeep, TextsKeepId> {
    
    List<TextsKeep> findByTexts(Texts texts);

    List<TextsKeep> findByMember(Member member);
}
