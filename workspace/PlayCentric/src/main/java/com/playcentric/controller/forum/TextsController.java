package com.playcentric.controller.forum;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.playcentric.model.forum.Forum;
import com.playcentric.model.forum.Texts;
import com.playcentric.model.member.Member;
import com.playcentric.service.forum.ForumService;
import com.playcentric.service.forum.TextsService;
import com.playcentric.service.member.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TextsController {

	@Autowired
	private TextsService textsService;
	
	@Autowired
	private ForumService forumService;
	
	@Autowired
	private MemberService memberService;

	// 顯示所有文章
	@GetMapping("/texts/page")
	public String listAllTexts(Model model) {
		List<Texts> textsList = textsService.findAllText();
		model.addAttribute("textsList", textsList);
		return "forum/texts/list"; // 對應的 Thymeleaf 模板名稱
	}
	
	@GetMapping("/texts/view")
	public String viewTexts(@RequestParam("textsId") int textsId, Model model) {
	    Texts texts = textsService.findById(textsId);
	    //if (texts != null) {
	        model.addAttribute("texts", texts);
	        model.addAttribute("forumId", texts.getForum().getForumId());
	        model.addAttribute("memId", texts.getMember().getMemId());
	        return "forum/texts/view"; // 對應的 Thymeleaf 模板名稱
	   // } else {
	        // 如果找不到文章，返回錯誤頁面或重新導向到文章列表頁面
	       // return "redirect:/texts/page";
	   // }
	}

	// 新增文章
	@GetMapping("/texts/add")
	public String showAddForm() {
//		model.addAttribute("texts", new Texts());
		return "forum/texts/add"; // 對應的 Thymeleaf 模板名稱
	}

	@PostMapping("/texts/add")
	public String addTexts(@ModelAttribute Texts texts, HttpSession httpSession) {
		Optional<Forum> optionalForum = forumService.findForumById(texts.getForumId());
		Member mem = memberService.findById(texts.getMemId());
		texts.setMember(mem);
		if (optionalForum.isPresent()) {
			Forum forum = optionalForum.get();
			texts.setForum(forum);
		}
		System.out.println(texts.getForumId());
		textsService.insert(texts);
		return "redirect:/texts/page";
	}

	// 編輯文章
	@GetMapping("/texts/edit")
	public String showEditForm(@RequestParam("textsId") int textsId, Model model) {
		Texts texts = textsService.findById(textsId);
		if (texts != null) {
			model.addAttribute("texts", texts);
			return "texts/edit"; // 對應的 Thymeleaf 模板名稱
		} else {
			return "forum/texts/edit";
		}
	}

	@PostMapping("/texts/edit")
	public String editTexts(@ModelAttribute Texts texts) {
		textsService.update(texts);
		return "redirect:/texts/page";
	}

	// 刪除文章
	@PostMapping("/texts/delete")
	public String deleteTexts(@RequestParam("textsId") int textsId) {
		textsService.deleteTexts(textsId);
		return "redirect:/texts/page";
	}

	// 搜尋文章
	@GetMapping("texts/search")
	public String searchTexts(@RequestParam("keyword") String keyword, Model model) {
		List<Texts> textsList = textsService.searchTextsByTitle(keyword);
		model.addAttribute("textsList", textsList);
		return "forum/texts/list"; // 確保這裡返回的模板與顯示所有文章的模板一致
	}

	// 更新文章是否顯示
	@PostMapping("texts/updateVisibility")
	public String updateVisibility(@RequestParam("textsId") int textsId, @RequestParam("hideTexts") boolean hideTexts) {
		textsService.updateIsShow(textsId, hideTexts);
		return "redirect:/texts/page";
	}

}
