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
	
	public void setHaveGame(LoginMemDto loginMember,Game game) {
		List<OwnGameLib> ownGameLibs = oglRepo.findByMemId(loginMember.getMemId());
		for (OwnGameLib ownGameLib : ownGameLibs) {
			if (ownGameLib.getGameId() == game.getGameId()) {
				game.setHaveGame(true);
			}
		}
	}
	
}
