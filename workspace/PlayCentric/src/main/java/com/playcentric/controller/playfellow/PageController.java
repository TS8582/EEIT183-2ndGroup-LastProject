package com.playcentric.controller.playfellow;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.playcentric.model.ImageLib;
import com.playcentric.model.playfellow.ImageLibPfmemberAssociation;
import com.playcentric.model.playfellow.PfGame;
import com.playcentric.model.playfellow.PlayFellowMember;
import com.playcentric.service.playfellow.PfGameService;
import com.playcentric.service.playfellow.PlayFellowMemberService;

@Controller
public class PageController {

	@Autowired
	PlayFellowMemberService playFellowMemberService;
	@Autowired
	PfGameService pfGameService;

	@GetMapping("/playFellow/testGameIdfunction")
	public String testGameIdfunction() {
		return "/playFellow/testGameId";
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
        model.addAttribute("ReviewSuccessPfGame",reviewSuccessPfGame);
        
        return "playFellow/playFellow";
    }
}
