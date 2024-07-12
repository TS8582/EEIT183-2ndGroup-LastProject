package com.playcentric.service.playfellow;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.playfellow.PfGame;
import com.playcentric.model.playfellow.PfGameDTO;
import com.playcentric.model.playfellow.PfGameRepository;

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
	
	 @Transactional
	    public void deletePfGame(Integer pfGameId) {
	        pfGameRepository.deleteById(pfGameId);
	    }

	@Transactional
	public PfGameDTO saveOrUpdatePfGame(PfGame pfGame) {
		Integer playFellowId = pfGame.getPlayFellowMember().getPlayFellowId();
		Integer gameId = pfGame.getGame().getGameId();

		Optional<PfGame> existingPfGame = pfGameRepository.findByPlayFellowMemberPlayFellowIdAndGameGameId(playFellowId, gameId);

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

	private PfGameDTO convertToDTO(PfGame pfGame) {
		PfGameDTO dto = new PfGameDTO();
		dto.setPfGameId(pfGame.getPfGameId());
		dto.setPlayFellowId(pfGame.getPlayFellowMember() != null ? pfGame.getPlayFellowMember().getPlayFellowId() : null);
		dto.setGameId(pfGame.getGame() != null ? pfGame.getGame().getGameId() : null);
		dto.setGameName(pfGame.getGame() != null ? pfGame.getGame().getGameName() : null);
		dto.setPricingCategory(pfGame.getPricingCategory());
		dto.setAmount(pfGame.getAmount());
		dto.setPfGameStatus(pfGame.getPfGameStatus());
		return dto;
	}
}
