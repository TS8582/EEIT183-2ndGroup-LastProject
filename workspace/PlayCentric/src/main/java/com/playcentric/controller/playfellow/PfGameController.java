package com.playcentric.controller.playfellow;

import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.primary.GameRepository;
import com.playcentric.model.playfellow.PfGame;
import com.playcentric.model.playfellow.PfGameDTO;
import com.playcentric.model.playfellow.PlayFellowMember;
import com.playcentric.model.playfellow.PlayFellowMemberRepository;
import com.playcentric.service.playfellow.PfGameService;
import com.playcentric.service.playfellow.PlayFellowMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class PfGameController {

	@Autowired
	PfGameService pfGameService;
	
	@Autowired
	PlayFellowMemberService playFellowMemberService;
	
	@Autowired
	PlayFellowMemberRepository playFellowMemberRepository;
	@Autowired
	GameRepository gameRepository;

	@GetMapping("/playFellow/pfGame/{playFellowId}")
	public String pfGamePage(@PathVariable Integer playFellowId, Model model) {
		
	    PlayFellowMember playFellowMember = playFellowMemberService.getPlayFellowMemberById(playFellowId);

	    PfGame pfGame = new PfGame();
	    pfGame.setPlayFellowMember(playFellowMember);
	    
	    List<Game> games = gameRepository.findAll();
	    model.addAttribute("games", games);
	    model.addAttribute("pfGame", pfGame);

	    return "playFellow/addPfGame"; 
	}
	

	@PostMapping("/playFellow/pfGame/save")
	public ResponseEntity<PfGameDTO> saveOrUpdatePfGame(@RequestBody PfGame pfGame) {
		PfGameDTO savedPfGame = pfGameService.saveOrUpdatePfGame(pfGame);
		return ResponseEntity.ok(savedPfGame);
	}

	@ResponseBody
	@GetMapping("/playFellow/pfGame/list/{playFellowId}")
	public List<PfGameDTO> getPfGamesByPlayFellowId(@PathVariable Integer playFellowId) {
		return pfGameService.findByPlayFellowId(playFellowId);
	}
	
	@DeleteMapping("/playFellow/pfGame/{pfGameId}")
    public ResponseEntity<Void> deletePfGame(@PathVariable Integer pfGameId) {
        pfGameService.deletePfGame(pfGameId);
        return ResponseEntity.noContent().build();
    }
	
	
}
