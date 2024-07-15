package com.playcentric.service.playfellow;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.playfellow.PfGame;
import com.playcentric.model.playfellow.PfGameDTO;
import com.playcentric.model.playfellow.PfGameRepository;
import com.playcentric.model.playfellow.PlayFellowMember;

import jakarta.transaction.Transactional;

@Service
public class PfGameService {

	@Autowired
	private PfGameRepository pfGameRepository;

	public PfGameDTO addPfGame(PfGame pfGame) {
		PfGame savedPfGame = pfGameRepository.save(pfGame);
		return convertToDTO(savedPfGame);
	}

	public List<PfGame> getAllReviewSuccessPlayFellowMembers() {
		List<PfGame> pfGames = pfGameRepository.findAll();

		List<PfGame> reviewSuccessPfmem = new ArrayList<>();
		for (PfGame pfGame : pfGames) {
			if (pfGame.getPlayFellowMember() != null && pfGame.getPlayFellowMember().getPfstatus() == 3) {
				reviewSuccessPfmem.add(pfGame);
			}
		}
		return reviewSuccessPfmem;
	}

	public List<PfGameDTO> findAllPfGame() {
		List<PfGame> pfGames = pfGameRepository.findAll();
		List<PfGameDTO> pfGameDTOs = new ArrayList<PfGameDTO>();

		for (PfGame pfGame : pfGames) {
			PfGameDTO dto = convertToDTO(pfGame);
			pfGameDTOs.add(dto);
		}

		return pfGameDTOs;
	}

	public List<PfGameDTO> findByPlayFellowId(Integer playFellowId) {
		List<PfGame> pfGames = pfGameRepository.findByPlayFellowMemberPlayFellowId(playFellowId);
		List<PfGameDTO> pfGameDTOs = new ArrayList<>();
		for (PfGame pfGame : pfGames) {
			PfGameDTO pfGameDTO = convertToDTO(pfGame);
			pfGameDTOs.add(pfGameDTO);
		}
		return pfGameDTOs;
	}

	public List<PfGame> getAllPlayFellowMembersByGameId(Integer gameId) {
		List<PfGame> pfGames = pfGameRepository.findByGameId(gameId);

		List<PfGame> reviewSusscessPfmem = new ArrayList<>();
		for (PfGame pfGame : pfGames) {
			if (pfGame.getPlayFellowMember() != null && pfGame.getPlayFellowMember().getPfstatus() == 3) {
				reviewSusscessPfmem.add(pfGame);
			}
		}

		return reviewSusscessPfmem;
	}

	@Transactional
	public void deletePfGame(Integer pfGameId) {
		pfGameRepository.deleteById(pfGameId);
	}

	@Transactional
	public PfGameDTO saveOrUpdatePfGame(PfGame pfGame) {
		Integer playFellowId = pfGame.getPlayFellowMember().getPlayFellowId();
		Integer gameId = pfGame.getGame().getGameId();

		Optional<PfGame> existingPfGame = pfGameRepository.findByPlayFellowMemberPlayFellowIdAndGameGameId(playFellowId,
				gameId);

		PfGame savedPfGame;
		if (existingPfGame.isPresent()) {
			PfGame existing = existingPfGame.get();
			existing.setPricingCategory(pfGame.getPricingCategory());
			existing.setAmount(pfGame.getAmount());
			existing.setPfGameStatus(pfGame.getPfGameStatus());
			savedPfGame = pfGameRepository.save(existing);
		} else {
			savedPfGame = pfGameRepository.save(pfGame);
		}

		return convertToDTO(savedPfGame);
	}

	public PfGameDTO convertToDTO(PfGame pfGame) {
		PfGameDTO pfGameDTO = new PfGameDTO();
		pfGameDTO.setPfGameId(pfGame.getPfGameId());

		if (pfGame.getPlayFellowMember() != null) {
			pfGameDTO.setPlayFellowId(pfGame.getPlayFellowMember().getPlayFellowId());
			pfGameDTO.setPfNickname(pfGame.getPlayFellowMember().getPfnickname());
		} else {
			pfGameDTO.setPlayFellowId(null);
			pfGameDTO.setPfNickname(null);
		}

		if (pfGame.getGame() != null) {
			pfGameDTO.setGameId(pfGame.getGame().getGameId());
			pfGameDTO.setGameName(pfGame.getGame().getGameName());
		} else {
			pfGameDTO.setGameId(null);
			pfGameDTO.setGameName(null);
		}

		pfGameDTO.setPricingCategory(pfGame.getPricingCategory());
		pfGameDTO.setAmount(pfGame.getAmount());
		pfGameDTO.setPfGameStatus(pfGame.getPfGameStatus());

		if (pfGame.getPlayFellowMember() != null && pfGame.getPlayFellowMember().getImageLibAssociations() != null
				&& !pfGame.getPlayFellowMember().getImageLibAssociations().isEmpty()) {

			byte[] imageBytes = pfGame.getPlayFellowMember().getImageLibAssociations().get(0).getImageLib()
					.getImageFile();
			String base64Image = Base64.getEncoder().encodeToString(imageBytes);
			pfGameDTO.setBase64Image(base64Image);
		}

		return pfGameDTO;
	}

}
