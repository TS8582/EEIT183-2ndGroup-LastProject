package com.playcentric.controller.playfellow;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.playcentric.model.ImageLib;
import com.playcentric.model.ImageLibRepository;
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
import com.playcentric.service.game.GameService;
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

	@Autowired
	PfOrderService pfOrderService;

	@Autowired
	PfOrder3Service pfOrder3Service;

	@Autowired
	MemberService memberService;

	@Autowired
	ImageLibRepository imageLibRepository;

	@Autowired
	GameService gameService;

	@PersistenceContext
	private EntityManager entityManager;

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

		}
		return "playFellow/playFellowCart";
	}

	@GetMapping("/playFellow")
	public String getPlayFellowById(Model model) {
		// List<PlayFellowMember> playFellowMembers =
		// playFellowMemberService.getAllPlayFellowMembers();

		List<PlayFellowMember> lastest5playFellowMembers = playFellowMemberService
				.getTopFiveReviewSuccessPlayFellowMembers();
		model.addAttribute("TopFiveReviewSuccessMembers", lastest5playFellowMembers);

		// Integer gameId1 = 1;
		// Integer gameId2 = 76;
		// List<PfGame> pfGames = pfGameService.getAllPlayFellowMembersByGameId(97);
		// List<PfGame> pfGames2 = pfGameService.getAllPlayFellowMembersByGameId(76);
		List<Game> findGameName = pfGameService.findAllGame();

		model.addAttribute("findGameIdAndName", findGameName);
		// model.addAttribute("PfGame", pfGames);
		// model.addAttribute("PfGame2", pfGames2);
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

	@GetMapping("/playFellow/{gameId}")
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
		Member orderMem = entityManager.getReference(Member.class, pfOrderDTO.getMemId());

		pfOrder.setPfGame(pfGame);

		int currentPoints = orderMem.getPoints();
		int totalAmount = pfOrderDTO.getTotalAmount();

		if (currentPoints < totalAmount) {
			return "點數不足，無法扣款";
		}

		orderMem.setPoints(currentPoints - totalAmount);
		pfOrder.setMember(orderMem);

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

	@ResponseBody
	@GetMapping("/api/images/{imageId}")
	public ResponseEntity<byte[]> getImageById(@PathVariable Integer imageId) {
		Optional<ImageLib> imageOpt = imageLibRepository.findById(imageId);
		if (imageOpt.isPresent()) {
			byte[] imageData = imageOpt.get().getImageFile();
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@ResponseBody
	@GetMapping("/api/allGame")
	public List<Game> allGame() {
		return gameService.findAll();
	}
}
