package com.playcentric.service.forum;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.playcentric.model.forum.Forum;
import com.playcentric.model.forum.ForumRepository;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.game.primary.GameRepository;

@Service
public class ForumService {

	@Autowired
	private ForumRepository forumRepository;
	
	@Autowired
	private GameRepository gameRepository;

	public List<Forum> getAllForums(int page, int size) {
		Pageable paging = PageRequest.of(page, size);
		Page<Forum> pagedResult = forumRepository.findAll(paging);
		return pagedResult.toList();
	}

	public Optional<Forum> findForumById(int forumId) {
		return forumRepository.findById(forumId);
	}

	public List<Forum> searchByTextsIntro(String keyword) {
        return forumRepository.findByTextsIntroContaining(keyword);
    }

	public Forum saveForum(Forum forum) {
		return forumRepository.save(forum);
	}

	public void deleteForumById(int forumId) {
		forumRepository.deleteById(forumId);
	}
	
	public Page<Forum> findByPage(Integer pageNumber){
		Pageable pgb = PageRequest.of(pageNumber-1, 10, Sort.Direction.DESC, "forumId");
		Page<Forum> page = forumRepository.findAll(pgb);
		return page;
	}

}
