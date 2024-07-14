package com.playcentric.service.playfellow;

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

	public List<PfGameDTO> findAllPfGame() {
		List<PfGame> pfGames = pfGameRepository.findAll();
		return pfGames.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<PfGameDTO> findByPlayFellowId(Integer playFellowId) {
		List<PfGame> pfGames = pfGameRepository.findByPlayFellowMemberPlayFellowId(playFellowId);
		return pfGames.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<PfGame> getAllPlayFellowMembersByGameId(Integer gameId) {
        List<PfGame> pfGames = pfGameRepository.findByGameId(gameId);
        System.out.println("Number of games found: " + pfGames.size());
        return pfGames;
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
		PfGameDTO dto = new PfGameDTO();
		dto.setPfGameId(pfGame.getPfGameId());
		dto.setPlayFellowId(
				pfGame.getPlayFellowMember() != null ? pfGame.getPlayFellowMember().getPlayFellowId() : null);
		dto.setGameId(pfGame.getGame() != null ? pfGame.getGame().getGameId() : null);
		dto.setGameName(pfGame.getGame() != null ? pfGame.getGame().getGameName() : null);
		dto.setPricingCategory(pfGame.getPricingCategory());
		dto.setAmount(pfGame.getAmount());
		dto.setPfGameStatus(pfGame.getPfGameStatus());
		dto.setPfNickname(pfGame.getPlayFellowMember() != null ? pfGame.getPlayFellowMember().getPfnickname() : null);

		if (pfGame.getPlayFellowMember() != null && pfGame.getPlayFellowMember().getImageLibAssociations() != null
				&& !pfGame.getPlayFellowMember().getImageLibAssociations().isEmpty()) {
			byte[] imageBytes = pfGame.getPlayFellowMember().getImageLibAssociations().get(0).getImageLib()
					.getImageFile();
			String base64Image = Base64.getEncoder().encodeToString(imageBytes);
			dto.setBase64Image(base64Image);
		}
		return dto;
	}
}
