package com.playcentric.service.forum;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.playcentric.model.forum.Texts;
import com.playcentric.model.forum.TextsRepository;

@Service
public class TextsService {

	@Autowired
	private TextsRepository textsRepository;

	// 是否顯示
	public Texts findByHideTexts(Boolean hideTexts) {
		List<Texts> optional = textsRepository.findAllByHideTexts(hideTexts);

		if (optional.isEmpty()) {
			return null;
		}

		return optional.get(0);
	}

	// 是否封鎖
	@Transactional
	public void updateIsShow(Integer textsId, boolean hideTexts) {
		textsRepository.updateHideTextsById(textsId, hideTexts);
	}

	public List<Texts> findAllText() {
		return textsRepository.findAll();
	}

	public List<Texts> searchTextsByTitle(String keyword) {
		return textsRepository.findAllByTitleContaining(keyword);
	}

	public Texts findById(int textsId) {
		Optional<Texts> optional = textsRepository.findById(textsId);

		if (optional.isEmpty()) {
			return null;
		}
		return optional.get();
	}

	public Texts insert(Texts texts) {
        return textsRepository.save(texts);
    }
	
	@Transactional
    public Texts update(Texts updateTexts) {
        Optional<Texts> existingTextsOpt = textsRepository.findById(updateTexts.getTextsId());
        if (existingTextsOpt.isPresent()) {
            Texts existingTexts = existingTextsOpt.get();
            existingTexts.setTitle(updateTexts.getTitle());
            existingTexts.setTextsContent(updateTexts.getTextsContent());
            existingTexts.setUpdatedTime(new Timestamp(System.currentTimeMillis())); // 更新 updatedTime 為當前時間
            existingTexts.setTextsLikeNum(updateTexts.getTextsLikeNum());
            existingTexts.setHideTexts(updateTexts.getHideTexts());
            return textsRepository.save(existingTexts);
        } else {
            System.out.println("查無文章");
            return null;
        }
    }

	public void deleteTexts(int textsId) {
		if (textsRepository.existsById(textsId)) {
	        textsRepository.deleteById(textsId);
	    } else {
	    	System.out.println("查無主題");
	    }
	}

}