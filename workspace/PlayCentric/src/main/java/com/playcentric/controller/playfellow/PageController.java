package com.playcentric.controller.playfellow;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.playcentric.model.ImageLib;
import com.playcentric.model.playfellow.ImageLibPfmemberAssociation;
import com.playcentric.model.playfellow.PfGame;
import com.playcentric.model.playfellow.PfGameDTO;
import com.playcentric.model.playfellow.PlayFellowMember;
import com.playcentric.service.playfellow.PfGameService;
import com.playcentric.service.playfellow.PlayFellowMemberService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

	@Autowired
	PlayFellowMemberService playFellowMemberService;
	@Autowired
	PfGameService pfGameService;

	
	
	
	// 進入cart
	@GetMapping("/playFellow/playFellowCart")
	public String viewPlayFellowCart(@RequestParam("pfGameId") Integer pfGameId, Model model) {
		Optional<PfGame> optionalPfGame = pfGameService.findByGameId(pfGameId);

		if (optionalPfGame.isPresent()) {
			PfGame pfGame = optionalPfGame.get();

			PlayFellowMember playFellowMember = pfGame.getPlayFellowMember();
			for (ImageLibPfmemberAssociation association : playFellowMember.getImageLibAssociations()) {
				ImageLib imageLib = association.getImageLib();
				String base64Image = Base64.getEncoder().encodeToString(imageLib.getImageFile());
				imageLib.setBase64Image(base64Image);
			}
			model.addAttribute("pfGame", pfGame);
//			model.addAttribute("pfNickname",pfGame.getPlayFellowMember().getPfnickname());
//			model.addAttribute("gameName",pfGame.getGame().getGameName());
//			model.addAttribute("amount",pfGame.getAmount());
			
		}
		return "playFellow/playFellowCart";
	}
	
	
	
	

	@GetMapping("/playFellow")
	public String getMethodName(Model model) {
		List<PlayFellowMember> playFellowMembers = playFellowMemberService.getAllPlayFellowMembers();

		for (PlayFellowMember playFellowMember : playFellowMembers) {
			for (ImageLibPfmemberAssociation association : playFellowMember.getImageLibAssociations()) {
				ImageLib imageLib = association.getImageLib();
				if (imageLib != null && imageLib.getImageFile() != null) {
					String base64Image = Base64.getEncoder().encodeToString(imageLib.getImageFile());
					imageLib.setBase64Image(base64Image);
				}
			}
		}

		Integer gameId1 = 1;
		Integer gameId2 = 2;
		List<PfGame> pfGames = pfGameService.getAllPlayFellowMembersByGameId(gameId1);
		List<PfGame> pfGames2 = pfGameService.getAllPlayFellowMembersByGameId(gameId2);
		List<PfGame> reviewSuccessPfGame = pfGameService.getAllReviewSuccessPlayFellowMembers();

		model.addAttribute("PfGame", pfGames);
		model.addAttribute("PfGame2", pfGames2);
		model.addAttribute("PlayFellowMember", playFellowMembers);
		model.addAttribute("ReviewSuccessPfGame", reviewSuccessPfGame);

		return "playFellow/playFellow";
	}
}
