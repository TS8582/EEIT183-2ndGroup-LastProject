package com.playcentric.service.forum;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.playcentric.model.forum.Texts;
import com.playcentric.model.forum.TextsRepository;

@Service
public class TextsService {

	@Autowired
	private TextsRepository textsRepository;

	// 確定是否有重複文章
	public boolean checkTitlExist(String title) {
		List<Texts> allByTitle = textsRepository.findAllByTitle(title);
		return !allByTitle.isEmpty();
	}

	// 修改狀態
	public Texts updateTextsShowStatus(Integer textsId, Boolean hideTexts) {
		Optional<Texts> optional = textsRepository.findById(textsId);
		if (optional.isPresent()) {
			Texts texts = optional.get();
			texts.setHideTexts(hideTexts);

			return textsRepository.save(texts);
		} else {
			return null; // or handle the case where Forum is not found
		}
	}

	// 當前所有主題的文章
	public List<Texts> findTextsByForumId(Integer forumId) {
		return textsRepository.findTextsByForumId(forumId);
	}

	// 分頁 當forumId下的前12筆
	public Page<Texts> findByPage(Integer forumId, Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 12, Sort.Direction.DESC, "doneTime");
		return textsRepository.findByForumForumId(forumId, pageable);
	}

	// 分頁 查詢全部的最新前12筆的 
	public Page<Texts> findAllByPage(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 12, Sort.Direction.DESC, "doneTime");
		return textsRepository.findAll(pageable);
	}

	// 新增
	public Texts insert(Texts texts) {
		return textsRepository.save(texts);
	}

	// 模糊查詢
	public List<Texts> findAllTexts(String texts) {
		return textsRepository.findAllByTitle(texts);
	}

	// 查詢id
	public Texts findById(Integer textsId) {
		Optional<Texts> optional = textsRepository.findById(textsId);

		// isPresent檢查Optional是否為空直
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	// 刪除
	public void deleteTextsById(int textsId) {
		if (textsRepository.existsById(textsId)) {
			textsRepository.deleteById(textsId);
		} else {
			System.out.println("查無主題");
		}
	}

	// 編輯
	@Transactional
	public Texts update(Texts texts) {
		Texts existingTexts = textsRepository.findById(texts.getTextsId()).orElse(null);
		if (existingTexts != null) {
			existingTexts.setTitle(texts.getTitle());
			existingTexts.setTextsContent(texts.getTextsContent());
			existingTexts.setForum(texts.getForum());
			existingTexts.setMember(texts.getMember());
			existingTexts.setForumPhoto(texts.getForumPhoto());
			return textsRepository.save(existingTexts);
		}
		return null;
	}

	// 分頁
	public Texts findLastestMsg() {
		Pageable pgb = PageRequest.of(0, 1, Sort.Direction.DESC, "doneTime");
		Page<Texts> page = textsRepository.findLatest(pgb);

		List<Texts> resultList = page.getContent();
		System.out.println("Result List: " + resultList);
		if (resultList.isEmpty()) {
			return null;
		}

		return resultList.get(0);
	}

	// 查詢全部文章
	public List<Texts> findAll() {
		return textsRepository.findAll();
	}

}
