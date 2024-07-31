package com.playcentric.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.secondary.OwnGameLib;
import com.playcentric.model.game.secondary.OwnGameLibRepository;
import com.playcentric.model.member.LoginMemDto;

@Service
public class OwnGameLibService {
	
	@Autowired
	private OwnGameLibRepository oglRepo;
	
	public OwnGameLib save(OwnGameLib ownGameLib) {
		return oglRepo.save(ownGameLib);
	}
	
	//用會員找擁有遊戲
	public List<OwnGameLib> findByMemId(Integer memId) {
		return oglRepo.findByMemId(memId);
	}
	
	//找會員擁有遊戲(按照購買時間降冪排序)
	public List<OwnGameLib> findByMemIdOrderByBuyAtDesc(Integer memId) {
		return oglRepo.findByMemIdOrderByBuyAtDesc(memId);
	}
	
	//設定擁有遊戲參數
	public void setHaveGame(LoginMemDto loginMember,Game game) {
		List<OwnGameLib> ownGameLibs = oglRepo.findByMemId(loginMember.getMemId());
		for (OwnGameLib ownGameLib : ownGameLibs) {
			if (ownGameLib.getGameId() == game.getGameId()) {
				game.setHaveGame(true);
			}
		}
	}
	
}
