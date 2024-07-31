package com.playcentric.controller.forum;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.playcentric.model.forum.Forum;
import com.playcentric.model.forum.ForumPhoto;
import com.playcentric.model.forum.Texts;
import com.playcentric.model.forum.TextsKeep;
import com.playcentric.model.forum.TextsKeepId;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.service.forum.ForumService;
import com.playcentric.service.forum.PhotoService;
import com.playcentric.service.forum.TextsKeepService;
import com.playcentric.service.forum.TextsService;
import com.playcentric.service.member.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("loginMember")
public class TextsController {

	@Autowired
	private TextsService textsService;

	@Autowired
	private ForumService forumService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private TextsKeepService textsKeepService;

	@Autowired
	private PhotoService photoService;

	// 更新Status
	@ResponseBody
	@PostMapping("/texts/updateTextsShowStatus")
	public Texts updatePostShowStatus(@RequestBody Texts texts) {
		System.out.println("Received request to update texts status");
		System.out.println("Texts ID: " + texts.getTextsId());
		System.out.println("Hide Texts: " + texts.getHideTexts());

		return textsService.updateTextsShowStatus(texts.getTextsId(), texts.getHideTexts());
	}

	// 顯示當前主題文章
	@GetMapping("/texts/findTextsByForumId")
	public String findTextsByForumId(@RequestParam("forumId") Integer forumId,
			@RequestParam(value = "p", defaultValue = "1") Integer pageNum, Model model) {
		List<Texts> texts = textsService.findTextsByForumId(forumId);
		Page<Texts> page = textsService.findByPage(forumId, pageNum); // 使用新的分页方法
		model.addAttribute("texts", texts);
		model.addAttribute("page", page);
		model.addAttribute("forumId", forumId); // 将themeId传递给前端

		System.out.println("Forum Id: " + forumId);
		System.out.println("Page Number: " + pageNum);

		return "forum/texts/listFront";
	}

	// 處理文章發布與圖片上傳
	@PostMapping("/personal/texts/publish")
	public String publish(@RequestParam("textsContent") String textsContent, @RequestParam("title") String title,
			@RequestParam("files") MultipartFile[] files, @RequestParam("forumId") Integer forumId,
			HttpSession httpSession, Model model) throws IOException {

		// 從 session 中獲取 member 對象
		LoginMemDto loginMember = (LoginMemDto) httpSession.getAttribute("loginMember");

		// 檢查 member 對象是否為 null
//		if (loginMember == null) {
//			// 如果 member 為 null，說明用戶未登入，重定向到登入頁面
//			return "redirect:/member/showLoginErr/notLogin"; // 確保你有一個處理 /login 的路由
//		}

		Forum forum = forumService.findById(forumId);

		// 建立一個新的文章物件
		Texts texts = new Texts();
		texts.setHideTexts(null);
		texts.setTextsContent(textsContent); // 設置文章內容
		texts.setDoneTime(new Timestamp(System.currentTimeMillis())); // 設置發佈日期
		texts.setTitle(title);
		texts.setForum(forum);
		texts.setMemId(loginMember.getMemId());

		// 如果有上傳的圖片，處理圖片上傳
		if (files != null && files.length > 0) {
			ArrayList<ForumPhoto> forumPhotoList = new ArrayList<>();

			for (MultipartFile file : files) {
				ForumPhoto forumPhoto = new ForumPhoto();
				forumPhoto.setPhotoFile(file.getBytes());
				forumPhoto.setTexts(texts); // 設置圖片對應的文章
				forumPhotoList.add(forumPhoto);
			}

			texts.setForumPhoto(forumPhotoList); // 將圖片列表設置到文章中
		}

		textsService.insert(texts); // 儲存文章到資料庫

		// 重定向到文章列表頁面或其他適當的處理
		return "redirect:/texts/page";
	}

	// 導入後台
	@GetMapping("/findAllTexts")
	public String findAllTexts(Model model) {

		List<Texts> arrayList = textsService.findAll();
		model.addAttribute("arrayList", arrayList);

		return "forum/texts/list";
	}

	// 查詢Id 跳轉到文章內容
	@GetMapping("/texts/findTextsById")
	public String findTextsById(@RequestParam Integer textsId, Model model) {
		Texts texts = textsService.findById(textsId);
		model.addAttribute("texts", texts);

		return "forum/texts/textsContent";

	}

	// 查詢名稱
	@PostMapping("/member/findByNameTexts")
	public String findByNameTexts(@RequestParam("title") String title, Model model) {

		List<Texts> arrayList = textsService.findAllTexts(title);
		System.out.println(arrayList);

		model.addAttribute("arrayList", arrayList);

		return "forum/texts/listFront";
	}

	// TinyMCE新增
	@GetMapping("/personal/texts/insertTexts2")
	public String insertTexts2(Model model) {
		List<Forum> arrayList = forumService.findAll();
		model.addAttribute("arrayList", arrayList);
		return "forum/texts/TinyMCE";
	}

	@PostMapping("/texts/insertTextsData2")
	public String insertTextsData2(@ModelAttribute Texts texts, @RequestParam("files") MultipartFile files, Model model,
			HttpSession httpSession) throws IOException {

		LoginMemDto loginMember = (LoginMemDto) httpSession.getAttribute("loginMember");

		Forum forum = forumService.findById(texts.getForumId());
		texts.setForum(forum);

		Member member = memberService.findById(loginMember.getMemId());
		texts.setMember(member);

		if (!files.isEmpty()) {
			ForumPhoto forumPhoto = new ForumPhoto();
			forumPhoto.setPhotoFile(files.getBytes());
			forumPhoto.setTexts(texts);
			ArrayList<ForumPhoto> photoList = new ArrayList<>();
			photoList.add(forumPhoto);
			texts.setForumPhoto(photoList);
//			forumPhoto = photoService.insertPhoto(forumPhoto);
		}

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

		Page<Texts> page = textsService.findAllByPage(pageNum);

		model.addAttribute("page", page);
		return "forum/texts/listFront";
	}

	// 尋找修改ID
	@GetMapping("/personal/texts/edit/{textsId}")
	public String showEditForm(@PathVariable Integer textsId, Model model) {
		Texts texts = textsService.findById(textsId);
		if (texts == null) {
			model.addAttribute("errorScript", "alert('找不到對應的文章，請確認文章ID是否正確。');");
			return "forum/texts/edit";
		}
		model.addAttribute("texts", texts);
		List<Forum> arrayList = forumService.findAll();
		model.addAttribute("arrayList", arrayList);
		return "forum/texts/edit";
	}

	@PostMapping("/texts/update")
	public String updateTexts(@RequestParam("textsContent") String textsContent, @RequestParam("title") String title,
			@RequestParam("textsId") Integer textsId, @RequestParam("files") MultipartFile[] files,
			@RequestParam("forumId") Integer forumId, @RequestParam("hideTexts") Boolean hideTexts,
			HttpSession httpSession, Model model) throws IOException {

		// 從 session 中獲取登錄會員資訊
		LoginMemDto loginMember = (LoginMemDto) httpSession.getAttribute("loginMember");

		// 檢查用戶是否登錄
		if (loginMember == null) {
			// 如果未登錄，重定向到登錄錯誤頁面
			return "redirect:/member/showLoginErr/notLogin";
		}

		// 根據 textsId 獲取現有的 Texts 對象
		Texts texts = textsService.findById(textsId);
		if (texts == null) {
			model.addAttribute("errorScript", "alert('找不到對應的文章，請確認文章ID是否正確。');");
			return "forum/texts/edit";
		}

		// 更新論壇信息（如果有變化）
		if (forumId != null && !forumId.equals(texts.getForum().getForumId())) {
			Forum forum = forumService.findById(forumId);
			texts.setForum(forum);
		}

		// 更新會員信息
		Member member = memberService.findById(loginMember.getMemId());
		texts.setMember(member);

		// 更新文本內容
		texts.setHideTexts(hideTexts);
		texts.setTextsContent(textsContent);
		texts.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
		texts.setTitle(title);

		// 處理文件上傳
		if (files != null && files.length > 0) {
			List<ForumPhoto> forumPhotosList = new ArrayList<>();

			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					ForumPhoto forumPhoto = new ForumPhoto();
					forumPhoto.setPhotoFile(file.getBytes());
					forumPhoto.setTexts(texts);
					forumPhotosList.add(forumPhoto);
				}
			}

			if (!forumPhotosList.isEmpty()) {
				System.err.println("更新照片");
				// 如果有新照片，更新文本的照片列表
				texts.setForumPhoto(forumPhotosList);
				photoService.deleteByTexts(texts);
			}
		}

		// 更新文本在數據庫中
		textsService.update(texts);

		// 更新成功後重定向到前台列表頁面
		return "redirect:/texts/"+textsId+"/talk";
	}

	// 刪除文章
	@GetMapping("/personal/texts/delete")
	public String deleteTexts(@RequestParam("textsId") Integer textsId) {
		textsKeepService.deleteByTextsId(textsId);
		textsService.deleteTextsById(textsId);

		return "redirect:/texts/page"; // 導入前台
	}

	@PostMapping("/personal/api/texts/keepText")
	@ResponseBody
	public String keepText(@RequestParam Integer textsId, @RequestParam Integer memId) {
		return textsKeepService.keepText(new TextsKeepId(textsId,memId));
	}
	
	@GetMapping("/texts/keepNum/{textsId}")
	@ResponseBody
	public String getMethodName(@PathVariable Integer textsId) {
		return textsKeepService.getKeepNum(textsId).toString();
	}

	@GetMapping("/personal/texts/keepList")
	public String getMethodName() {
		return "member/memberKeepTextsPage";
	}
	
	@GetMapping("/personal/api/texts/keepList")
	@ResponseBody
	public Page<TextsKeep> getMethodName(@RequestParam Integer pageNum,@ModelAttribute("loginMember") LoginMemDto loginMember) {
		return textsKeepService.getMemKeepTexts(loginMember.getMemId(), pageNum);
	}
	
}
