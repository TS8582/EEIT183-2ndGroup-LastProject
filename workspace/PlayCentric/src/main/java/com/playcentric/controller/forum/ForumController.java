package com.playcentric.controller.forum;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.forum.Forum;
import com.playcentric.service.forum.ForumService;

@Controller
public class ForumController {

	@Autowired
	private ForumService forumService;

	// 新增討論串
	@GetMapping("/forum/insertForum")
	public String insertForum() {
		return "/forum/addForumPage";
	}

	@PostMapping("/addForumData")
	public String addForumData(@RequestParam MultipartFile imageLib, @RequestParam("forumName") String forumName,
			@RequestParam("textsIntro") String textsIntro, Model model) throws IllegalStateException, IOException {
		Forum forum = new Forum();
		forum.setTextsPhoto(imageLib.getBytes());
		forum.setForumName(forumName);
		forum.setTextsIntro(textsIntro);

		forumService.insertForum(forum);

		model.addAttribute("insetOK", "成功");
		return "redirect:/findAllForum";
	}

	// 顯示主頁
	@GetMapping("/findAllForum")
	public String findAllForum(Model model) {

		List<Forum> arrayList = forumService.findAll();
		model.addAttribute("arrayList", arrayList);
		return "forum/forumhome";
	}

	// 後臺查詢全部
	@GetMapping("/findAllForum2")
	public String findAllForum2(Model model) {

		List<Forum> arrayList = forumService.findAll();
		model.addAttribute("arrayList", arrayList);

		return "/forum/getAllForum";
	}

	// 模糊查詢
	@PostMapping("/findByForumName")
	public String findByForumName(@RequestParam("forumName") String forumName, Model model) {
		List<Forum> arrayList = forumService.findForumByForumName(forumName);
		model.addAttribute("arrayList", arrayList);
		return "/forum/getAllForum";
	}

	// 刪除討論串
	@GetMapping("/deleteForum")
	public String deleteForum(@RequestParam("forumId") Integer forumId) {
		forumService.deleteForumById(forumId);

		return "redirect:/findAllForum2";
	}

	// 編輯討論串
	@GetMapping("/updateByForumId")
	private String updateByForumId(Model model, @RequestParam("forumId") Integer forumId) {

		Forum forumBean = forumService.findById(forumId);

		model.addAttribute("forumBean", forumBean);

		return "forum/editPage";

	}

	@PostMapping("/editTheme")
	public String editTheme(@RequestParam("forumId") Integer forumId, @RequestParam MultipartFile imageLib,
			@RequestParam("forumName") String forumName, @RequestParam("textsIntro") String textsIntro, Model model)
			throws IllegalStateException, IOException {

		Forum forumBean = new Forum();

		forumBean.setTextsPhoto(imageLib.getBytes());
		forumBean.setForumId(forumId);
		forumBean.setForumName(forumName);
		forumBean.setTextsIntro(textsIntro);

		forumService.update(forumBean);

		model.addAttribute("updataOK", "成功");
		return "redirect:/findAllForum2";
	}

	@GetMapping("/forumImg")
	public ResponseEntity<byte[]> downloadImage(@RequestParam Integer forumId) {
		Forum forumImg = forumService.findById(forumId);

		byte[] imgFile = forumImg.getTextsPhoto();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		// body, headers , http status code
		return new ResponseEntity<byte[]>(imgFile, headers, HttpStatus.OK);
	}
}
