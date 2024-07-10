package com.playcentric.controller.forum;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.playcentric.model.forum.Forum;
import com.playcentric.service.forum.ForumService;

@Controller
public class ForumController {

	@Autowired
	private ForumService forumService;

	@GetMapping("forum/add")
	public String showAddPage(Model model) {
		model.addAttribute("forum", new Forum());
		return "forum/addForumPage";
	}

	@PostMapping("forum/addPost")
	public String addForum(@RequestParam("textsIntro") String textsIntro) {
		Forum forum = new Forum();
		forum.setTextsIntro(textsIntro);
		forumService.saveForum(forum);
		return "redirect:/forum/page";
	}

	@GetMapping("/forum/edit")
	public String showEditPage(@RequestParam("forumId") int forumId, Model model) {
		Optional<Forum> forum = forumService.findForumById(forumId);
		if (forum.isPresent()) {
			model.addAttribute("forum", forum.get());
			return "forum/editPage"; // 返回到名為"editPage"的HTML模板，顯示編輯forum的表單
		} else {
			return "redirect:/forum/forumhome"; // 如果forum不存在，重定向到首頁
		}
	}

	@PutMapping("/forum/editPost")
	public String editForum(@RequestParam("forumId") int forumId, @RequestParam("textsIntro") String textsIntro) {
		Optional<Forum> optionalForum = forumService.findForumById(forumId);
		if (optionalForum.isPresent()) {
			Forum forum = optionalForum.get();
			forum.setTextsIntro(textsIntro);
			forumService.saveForum(forum);
		}
		return "redirect:/forum/page"; // 更新完後重定向到首頁
	}

	@DeleteMapping("/forum/delete")
	public String deleteForum(@RequestParam("forumId") int forumId) {
		forumService.deleteForumById(forumId);
		return "redirect:/forum/page";
	}

	 @GetMapping("/forum/search")
	 public String search(@RequestParam("keyword") String keyword, Model model) {
	        List<Forum> forums = forumService.searchByTextsIntro(keyword);
	        model.addAttribute("forums", forums);
	        return "forum/search"; // 返回包含查詢結果的HTML視圖
	    }

	@GetMapping("/forum/page")
	public String findByPage(@RequestParam(value = "p", defaultValue = "1") Integer pageNum, Model model) {

		Page<Forum> page = forumService.findByPage(pageNum);

		model.addAttribute("page", page);

		return "forum/forumhome";
	}

//    private void setGameInfo(Forum forum) {
//
//		Game game = forum.getGame();
//		ForumGameDto gameIfo = forum.getGameIfo();
//		gameIfo.setGameId(game.getGameId());
//		gameIfo.setGameName(game.getGameName());
//		List<ImageLib> imageLibs = game.getImageLibs();
//		byte[] gameImg = imageLibs.get(0).getImageFile();
//		gameIfo.setGameImg(gameImg);
//    }
}
