package com.playcentric.service.forum;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.playcentric.model.forum.Forum;
import com.playcentric.model.forum.ForumRepository;
import com.playcentric.model.forum.Texts;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.primary.GameRepository;

@Service
public class ForumService {

	@Autowired
	private ForumRepository forumRepository;

//	@Autowired
//	private GameRepository gameRepository;

	// id查詢
	public Forum findById(Integer forumId) {
		Optional<Forum> optional = forumRepository.findById(forumId);

		if (optional.isEmpty()) {
			return null;
		}

		return optional.get();
	}

	// 模糊forumName查詢
	public List<Forum> findForumByForumName(String forumName) {
		return forumRepository.findAllByForumName(forumName);
	}

	// 查詢全部
	public List<Forum> findAll() {
		return forumRepository.findAll();
	}

	// 新增
	public Forum insertForum(Forum forum) {
		return forumRepository.save(forum);
	}

	// 修改主题
	@Transactional
	public Forum update(Forum updateForum) {
		if (forumRepository.existsById(updateForum.getForumId())) {
			return forumRepository.save(updateForum);
		} else {
			System.out.println("查無討論串");
			return null;
		}
	}

	// 删除主题
	@Transactional
	public void deleteForumById(int forumId) {
		if (forumRepository.existsById(forumId)) {
			forumRepository.deleteById(forumId);
		} else {
			System.out.println("查無討論串");
		}
	}

}
