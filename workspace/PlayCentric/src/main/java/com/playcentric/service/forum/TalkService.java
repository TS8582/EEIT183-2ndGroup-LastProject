package com.playcentric.service.forum;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.playcentric.model.forum.Talk;
import com.playcentric.model.forum.TalkRepository;

@Service
public class TalkService {

	@Autowired
	private TalkRepository talkRepository;

	// 當前所有文章的訊息
	public List<Talk> getTalkByTextsId(Integer textsId) {
		return talkRepository.findTalkByTextsId(textsId);
	}

	// 新增留言
	public Talk insert(Talk talk) {
		return talkRepository.save(talk);
	}

	// 查詢單筆留言(ID)
	public Talk findTalkById(Integer talkId) {
		Optional<Talk> optional = talkRepository.findById(talkId);
		// isPresent檢查Optional是否為空直
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	// 查詢全部留言
	public List<Talk> getAllTalk() {
		return talkRepository.findAll();
	}

	// 透過ID刪除留言
	public void deleteTalkById(Integer talkId) {
		talkRepository.deleteById(talkId);
	}

	// 排序
	public Talk findLastestTalk() {
		Pageable pgb = PageRequest.of(0, 1, Sort.Direction.DESC, "talkTime");
		Page<Talk> page = talkRepository.findLatest(pgb);

		List<Talk> resultList = page.getContent();
		// isEmpty檢查陣列,及何等是否為空直
		if (resultList.isEmpty()) {
			return null;
		}

		return resultList.get(0);
	}

	// 分頁
	public Page<Talk> findByPage(Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 3, Sort.Direction.DESC, "talkTime");
		Page<Talk> page = talkRepository.findAll(pgb);
		return page;
	}

	// 新增一筆後，回傳最新的前三筆
	public Page<Talk> addOneAndReturnThree(Talk talk) {
		talkRepository.save(talk);

		Pageable pgb = PageRequest.of(0, 3, Sort.Direction.DESC, "talkTime");

		Page<Talk> page = talkRepository.findAll(pgb);

		return page;
	}
}
