package com.playcentric.controller.playfellow;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.playcentric.model.ImageLib;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.member.Member;
import com.playcentric.model.playfellow.ImageLibPfmemberAssociation;
import com.playcentric.model.playfellow.PfGame;
import com.playcentric.model.playfellow.PfGameDTO;
import com.playcentric.model.playfellow.PfOrder;
import com.playcentric.model.playfellow.PfOrder3;
import com.playcentric.model.playfellow.PfOrder3DTO;
import com.playcentric.model.playfellow.PfOrderDTO;
import com.playcentric.model.playfellow.PlayFellowMember;
import com.playcentric.service.member.MemberService;
import com.playcentric.service.playfellow.PfGameService;
import com.playcentric.service.playfellow.PfOrder3Service;
import com.playcentric.service.playfellow.PfOrderService;
import com.playcentric.service.playfellow.PlayFellowMemberService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {

	@Autowired
	PlayFellowMemberService playFellowMemberService;
	@Autowired
	PfGameService pfGameService;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	PfOrderService pfOrderService;

	@Autowired
	PfOrder3Service pfOrder3Service;

	@Autowired
	MemberService memberService;

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
	public String getPlayFellowById(Model model) {
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

		List<PlayFellowMember> lastest5playFellowMembers = playFellowMemberService
				.getTopFiveReviewSuccessPlayFellowMembers();
		model.addAttribute("TopFiveReviewSuccessMembers", lastest5playFellowMembers);

		Integer gameId1 = 1;
		Integer gameId2 = 2;
		List<PfGame> pfGames = pfGameService.getAllPlayFellowMembersByGameId(gameId1);
		List<PfGame> pfGames2 = pfGameService.getAllPlayFellowMembersByGameId(gameId2);
		List<Game> findGameName = pfGameService.findAllGame();

		model.addAttribute("findGameIdAndName", findGameName);
		model.addAttribute("PfGame", pfGames);
		model.addAttribute("PfGame2", pfGames2);
		model.addAttribute("PlayFellowMember", playFellowMembers);

		return "playFellow/playFellow";
	}

	@ResponseBody
	@GetMapping("/playFellow/showGame")
	public ResponseEntity<List<PfGameDTO>> getPfGameDTOsByPlayFellowId(@RequestParam Integer playFellowId) {
		List<PfGameDTO> pfGameDTOs = pfGameService.findByPlayFellowId(playFellowId);
		if (pfGameDTOs.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(pfGameDTOs, HttpStatus.OK);
	}

	@PostMapping("/playFellow/{gameId}")
	public String showGameMember(@PathVariable("gameId") Integer gameId, Model model) {
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

		Game game = pfGameService.getGameById(gameId);
		model.addAttribute("game", game);

		List<Game> findGameName = pfGameService.findAllGame();
		model.addAttribute("findGameIdAndName", findGameName);

		List<PfGame> pfGames = pfGameService.getAllPlayFellowMembersByGameId(gameId);
		model.addAttribute("PfGame", pfGames);

		List<PfGame> pfGamesMales = pfGameService.getAllPlayFellowMembersByGameIdAndMale(gameId);
		model.addAttribute("Males", pfGamesMales);

		List<PfGame> pfGamesFemales = pfGameService.getAllPlayFellowMembersByGameIdAndFemale(gameId);
		model.addAttribute("Females", pfGamesFemales);

		return "playFellow/showGameMember";
	}

	@ResponseBody
	@PostMapping("playFellow/addPfOrder")
	public String addOrder(@RequestBody PfOrderDTO pfOrderDTO) {
	    PfOrder pfOrder = new PfOrder();

	    PfGame pfGame = entityManager.getReference(PfGame.class, pfOrderDTO.getPfGameId());
	    Member member = entityManager.getReference(Member.class, pfOrderDTO.getMemId());

	    pfOrder.setPfGame(pfGame);

	    Member orderMem = memberService.findById(pfOrderDTO.getMemId());
	    int currentPoints = orderMem.getPoints();
	    int totalAmount = pfOrderDTO.getTotalAmount();

	    if (currentPoints < totalAmount) {
	        return "點數不足，無法扣款";
	    }

	    orderMem.setPoints(currentPoints - totalAmount);
	    pfOrder.setMember(member);
	    
	    

	    String transactionID = pfOrderDTO.getTransactionID();
	    if (transactionID == null || transactionID.trim().isEmpty()) {
	        transactionID = null;
	    }
	    pfOrder.setTransactionID(transactionID);

	    pfOrder.setPaymentStatus(pfOrderDTO.getPaymentStatus());
	    pfOrder.setQuantity(pfOrderDTO.getQuantity());
	    pfOrder.setTotalAmount(totalAmount);
	    pfOrder.setPaymentTime(new Date());
	    pfOrder.setAdded(new Date());

	    pfOrderService.savePfOrder(pfOrder);

	    return "付款成功";
	}

	
	
	
	
	

//	@ResponseBody
//	@PostMapping("playFellow/addPfOrder3")
//	public String addOrder(@RequestBody PfOrder3DTO pfOrderDTO3) {
//		PfOrder3 pfOrder3 = new PfOrder3();
//
//		PfGame pfGame = entityManager.getReference(PfGame.class, pfOrderDTO3.getPfGameId());
//		Member member = entityManager.getReference(Member.class, pfOrderDTO3.getMemId());
//
//		pfOrder3.setPfGame(pfGame);
//		pfOrder3.setMember(member);
//
//		String transactionID = pfOrderDTO3.getTransactionID();
//		if (transactionID == null || transactionID.trim().isEmpty()) {
//			transactionID = null;
//		}
//		pfOrder3.setTransactionID(transactionID);
//
//		pfOrder3.setPaymentStatus(pfOrderDTO3.getPaymentStatus());
//		pfOrder3.setQuantity(pfOrderDTO3.getQuantity());
//		pfOrder3.setTotalAmount(pfOrderDTO3.getTotalAmount());
//
//		pfOrder3.setAdded(new Date());
//
//		pfOrder3Service.savePfOrder3(pfOrder3);
//
//		return "訂單提交成功";
//	}
}
