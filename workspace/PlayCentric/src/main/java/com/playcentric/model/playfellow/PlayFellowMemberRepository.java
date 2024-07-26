package com.playcentric.model.playfellow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.playcentric.model.member.Member;

public interface PlayFellowMemberRepository extends JpaRepository<PlayFellowMember, Integer> {
	
	@Query("SELECT COUNT(p) > 0 FROM PlayFellowMember p WHERE p.pfnickname = :pfnickname")
	boolean existsByPfnickname(String pfnickname);//檢查暱稱是否重複
	
	//審核 抓status
	List<PlayFellowMember> findByPfstatus(Byte pfstatus); 
	
    List<PlayFellowMember> findAllByOrderByPfcreatedTimeDesc();
    
    
    //sessioN會員去抓伴遊的資料
    PlayFellowMember findByMember(Member member);

	
	

}
