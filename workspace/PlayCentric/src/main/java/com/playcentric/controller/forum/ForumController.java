package com.playcentric.controller.forum;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	public String addForumData(@RequestParam MultipartFile textsPhoto, @RequestParam("forumName") String forumName,
			@RequestParam("textsIntro") String textsIntro, Model model) throws IllegalStateException, IOException {
		Forum forum = new Forum();
		forum.setTextsPhoto(textsPhoto.getBytes());
		forum.setForumName(forumName);
		forum.setTextsIntro(textsIntro);

		forumService.insertForum(forum);

		model.addAttribute("insetOK", "成功");
		return "redirect:/findAllForum";
	}

	// 新增跳轉頁面
	@GetMapping("/forum/insertForum2")
	public String insertForum2() {
		return "/forum/addForumPage.html";

	}

	// 新增要改富文本的新增圖片要轉base64 未成功
	@PostMapping("/addForumData2")
	public String addForumData2(@RequestParam("forumPhoto") MultipartFile forumPhoto,
			@RequestParam("forumName") String forumName,
			@RequestParam("textsIntro") String textsIntro,
			@RequestParam("csrfmiddlewaretoken") String csrfToken, Model model)
			throws IllegalStateException, IOException {

		// 實現 CSRF token 驗證，如果需要
		// validateCsrfToken(csrfToken);

		// 確認文件不為空且類型為圖片
		if (forumPhoto != null && forumPhoto.getContentType().startsWith("image")) {
			// 讀取文件內容
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int bytesRead;
			try (InputStream fileContent = forumPhoto.getInputStream()) {
				while ((bytesRead = fileContent.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
			}

			byte[] imageBytes = outputStream.toByteArray();
			String base64Image = Base64.getEncoder().encodeToString(imageBytes);

			// 創建主题對象
			Forum forum1 = new Forum();
			forum1.setTextsPhoto(imageBytes); // 這裡保存原始的圖片字節數據
			forum1.setForumName(forumName);
			forum1.setTextsIntro(textsIntro);

			forumService.insertForum(forum1);

			model.addAttribute("insertOK", "成功");

			// 返回 JSON 響應
			return String.format("{\"type\":\"%s\", \"data\":\"%s\"}", forumPhoto.getContentType(), base64Image);
		} else {
			// 返回錯誤信息
			return "{\"error\": \"Invalid file type\"}";
		}
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
	public String editTheme(@RequestParam("forumId") Integer forumId, @RequestParam MultipartFile textsPhoto,
			@RequestParam("forumName") String forumName, @RequestParam("textsIntro") String textsIntro, Model model)
			throws IllegalStateException, IOException {

		Forum forumBean = new Forum();

		forumBean.setTextsPhoto(textsPhoto.getBytes());
		forumBean.setForumId(forumId);
		forumBean.setForumName(forumName);
		forumBean.setTextsIntro(textsIntro);

		forumService.update(forumBean);

		model.addAttribute("updataOK", "成功");
		return "redirect:/findAllForum2";
	}

	@GetMapping("/forumPhoto")
	public ResponseEntity<byte[]> downloadPhoto(@RequestParam Integer forumId) {
		Forum forumPhoto = forumService.findById(forumId);

		byte[] photoFile = forumPhoto.getTextsPhoto();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		// body, headers , http status code
		return new ResponseEntity<byte[]>(photoFile, headers, HttpStatus.OK);
	}
}
