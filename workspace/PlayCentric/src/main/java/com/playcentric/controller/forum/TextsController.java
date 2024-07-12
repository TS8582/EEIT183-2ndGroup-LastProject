package com.playcentric.controller.forum;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.ImageLib;
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

	// 處理文章發布與圖片上傳
	@PostMapping("/texts/publish")
	public String publish(@RequestParam("textsContent") String textsContent, @RequestParam("title") String title,
			@RequestParam("imageFile") MultipartFile[] imageFile, HttpSession session, Model model) throws IOException {

		// 建立一個新的文章物件
		Texts texts = new Texts();
		texts.setTextsContent(textsContent); // 設置文章內容
		texts.setTitle(title);

		// 如果有上傳的圖片，處理圖片上傳
		if (imageFile != null && imageFile.length > 0) {
			ArrayList<ImageLib> imgList = new ArrayList<>();

			for (MultipartFile file : imageFile) {
				ImageLib imgLib = new ImageLib();
				imgLib.setImageFile(file.getBytes());
				imgLib.setTexts(texts); // 設置圖片對應的文章
				imgList.add(imgLib);
			}

			texts.setImgLib(imgList); // 將圖片列表設置到文章中
		}

		textsService.insert(texts); // 儲存文章到資料庫

		// 重定向到文章列表頁面或其他適當的處理
		return "redirect:/texts/page";
	}

	// 查詢全部文章
	@GetMapping("/texts/findAllTextHtml")
	public String findAllTextsHtml() {

		return "forum/texts/getAllTexts";
	}

	// 導入後台
	@GetMapping("/findAllTexts")
	public String findAllTexts(Model model) {

		List<Texts> arrayList = textsService.findAll();
		model.addAttribute("arrayList", arrayList);

		return "forum/texts/lsit";
	}

	// 查詢名稱
	@PostMapping("/findTextsByTitle")
	public String findTextsByTitle(@RequestParam("title") String title, Model model) {

		List<Texts> arrayList = textsService.findAllText(title);

		model.addAttribute("arrayList", arrayList);

		return "forum/texts/getAllTexts";
	}

	// 查詢Id 跳轉到文章內容
	@GetMapping("/texts/findTextsById")
	public String findTextsById(@RequestParam Integer textsId, Model model) {
		Texts texts = textsService.findById(textsId);
		model.addAttribute("texts", texts);

		return "forum/texts/textsContent";

	}

	// TinyMCE新增
	@GetMapping("/texts/insertTexts2")
	public String insertTexts2() {
		return "forum/texts/TinyMCE";
	}

	@PostMapping("/texts/insertTextsData2")
	public String insertTextsData2(@RequestParam("textsContent") String textsContent, Model model,
			HttpSession httpSession) {

		Texts texts = new Texts();
		texts.setTextsContent(textsContent);
		textsService.insert(texts);

		Texts lastestTexts = textsService.findLastestMsg();
		model.addAttribute("lastestTexts", lastestTexts);

		return "redirect:/texts/page"; // Ajax分頁(前台)
	}

	// 新增文章
	@GetMapping("/texts/insertTexts")
	public String insertTexts() {
		return "forum/texts/add";
	}

	@PostMapping("/texts/insertTextsData")
	public String insertTextsData(@RequestParam("textsContent") String textsContent, Model model,
			HttpSession httpSession) {

		Texts texts = new Texts();
		texts.setTextsContent(textsContent);// 設置文章內容

		textsService.insert(texts);

		Texts lastestTexts = textsService.findLastestMsg();
		model.addAttribute("lastestTexts", lastestTexts);

		return "forum/texts/add";
	}

	// Ajax分頁(前台)
	@GetMapping("/texts/page")
	public String findByPage2(@RequestParam(value = "p", defaultValue = "1") Integer pageNum, Model model) {

		Page<Texts> page = textsService.findByPage(pageNum);

		model.addAttribute("page", page);
		return "forum/texts/listFront";
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
		return "redirect:/texts/page"; // Ajax分頁(前台)
	}

	// 刪除文章
	@GetMapping("/texts/delete")
	public String deleteTexts(@RequestParam("textsId") Integer textsId) {
		textsService.deleteTextsById(textsId);

		return "redirect:/findAllTexts"; // 導入後台
	}

}
