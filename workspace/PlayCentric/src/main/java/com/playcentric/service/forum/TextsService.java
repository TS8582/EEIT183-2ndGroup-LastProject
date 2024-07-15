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

	// 當前所有主題的文章
	public List<Texts> findTextsByForumId(Integer forumId) {
		return textsRepository.findTextsByForumId(forumId);
	}

	// 分頁 當forumId下的
	public Page<Texts> findByPage(Integer forumId, Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 12, Sort.Direction.DESC, "doneTime");
		return textsRepository.findByForumForumId(forumId, pageable);
	}

	// 分頁 當forumId下的(All)
	public Page<Texts> findAllByPage(Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 12, Sort.Direction.DESC, "doneTime");
		return textsRepository.findAll(pageable);
	}

	// 是否顯示
	public Texts findByHideTexts(Boolean hideTexts) {
		List<Texts> optional = textsRepository.findAllByHideTexts(hideTexts);

		if (optional.isEmpty()) {
			return null;
		}

		return optional.get(0);
	}

	// 新增
	public Texts insert(Texts texts) {
		return textsRepository.save(texts);
	}

	// 模糊查詢
	public List<Texts> findAllText(String texts) {
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
