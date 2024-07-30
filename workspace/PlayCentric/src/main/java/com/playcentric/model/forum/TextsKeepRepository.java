package com.playcentric.model.forum;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.playcentric.model.member.Member;




public interface TextsKeepRepository extends JpaRepository<TextsKeep, TextsKeepId> {
    
    List<TextsKeep> findByTexts(Texts texts);

    Page<TextsKeep> findByMember(Member member, Pageable pageable);
}
