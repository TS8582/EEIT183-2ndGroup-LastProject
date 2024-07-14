package com.playcentric.controller.playfellow;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.playcentric.model.ImageLib;
import com.playcentric.model.playfellow.ImageLibPfmemberAssociation;
import com.playcentric.model.playfellow.PfGame;
import com.playcentric.model.playfellow.PfGameDTO;
import com.playcentric.model.playfellow.PlayFellowMember;
import com.playcentric.service.playfellow.PfGameService;
import com.playcentric.service.playfellow.PlayFellowMemberService;

@Controller
public class PageController {

	@Autowired
	PlayFellowMemberService playFellowMemberService;
	@Autowired
	PfGameService pfGameService;

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

		model.addAttribute("PlayFellowMember", playFellowMembers);
		return "playFellow/playFellow";
	}

	@GetMapping("/playFellow/playFellowByGameId")
	public String getPlayFellowMembersByGameId(@RequestParam("gameId") Integer gameId, Model model) {
		List<PfGame> pfGames = pfGameService.getAllPlayFellowMembersByGameId(gameId);

		List<PlayFellowMember> playFellowMembers = new ArrayList<>();

		for (PfGame pfGame : pfGames) {
			PlayFellowMember playFellowMember = pfGame.getPlayFellowMember();
			if (playFellowMember != null) {
				for (ImageLibPfmemberAssociation association : playFellowMember.getImageLibAssociations()) {
					ImageLib imageLib = association.getImageLib();
					String base64Image = Base64.getEncoder().encodeToString(imageLib.getImageFile());
					imageLib.setBase64Image(base64Image);
				}
				playFellowMembers.add(playFellowMember);
			}
		}

		model.addAttribute("PlayFellowMember", playFellowMembers);
		return "playFellow/playFellow";
	}

}
