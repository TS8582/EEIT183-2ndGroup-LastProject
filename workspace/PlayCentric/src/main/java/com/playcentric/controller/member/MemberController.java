package com.playcentric.controller.member;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.config.NgrokConfig;
import com.playcentric.model.ImageLib;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.service.ImageLibService;
import com.playcentric.service.member.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes(names = { "loginMember" })
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private ImageLibService imageLibService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private NgrokConfig ngrokConfig;

	@PostMapping("/back/api/regist")
	@ResponseBody
	public String backRegistMember(@ModelAttribute Member member, @RequestParam("photoFile") MultipartFile photoFile,
			Model model) throws IOException {
		return registMember(member, photoFile, model);
	}

	@PostMapping("/regist")
	@ResponseBody
	public String registMember(@ModelAttribute Member member, @RequestParam("photoFile") MultipartFile photoFile,
			Model model)
			throws IOException {
		System.err.println(member);
		try {
			if (!hasInfo(member)) {
				return "請填妥資料!";
			}
			if (memberService.checkAccountExist(member.getAccount())) {
				return "帳號已存在";
			}
			if (memberService.checkEmailExist(member.getEmail())) {
				return "Email已註冊";
			}
			if (!photoFile.isEmpty()) {
				ImageLib imageLib = new ImageLib();
				imageLib.setImageFile(photoFile.getBytes());
				Integer imageId = imageLibService.saveImage(imageLib).getImageId();
				member.setPhoto(imageId);
			}
			LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
			loginMember = memberService.checkLoginMember(loginMember);
			if (loginMember == null || loginMember.getRole() != 1) {
				member.setRole((short) 0);
			}
			memberService.addMember(member);
			return "註冊成功!";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "註冊失敗!";
	}

	// 把錯誤提示顯示在首頁
	@GetMapping("/homeShowErr/{err}")
	public String homeShowErr(@PathVariable String err, RedirectAttributes redirectAttributes) {
		if ("notLogin".equals(err) || "loginAgain".equals(err)) {
			return showLoginErr(err, redirectAttributes);
		}
		String redirectMsg = checkErr(err);
		redirectAttributes.addFlashAttribute("redirectMsg", redirectMsg);
		return "redirect:/";
	}

	// 把錯誤提示顯示在登入頁面
	// @GetMapping("/showLoginErr/{err}")
	public String showLoginErr(@PathVariable String err, RedirectAttributes redirectAttributes) {
		String redirectMsg = checkErr(err);
		redirectAttributes.addFlashAttribute("redirectMsg", redirectMsg);
		return "redirect:/member/login";
	}

	// 把Err轉成給用戶的提示文字
	private String checkErr(String err) {
		switch (err) {
			case "notLogin":
				return "請先登入會員!";

			case "loginAgain":
				return "請重新登入會員!";

			case "anotherLogin":
				return "登入帳號已變!";

			case "notMng":
				return "您不是管理員!";

			default:
				return err;
		}
	}

	// 登入頁面URL
	@GetMapping("/login")
	public String loginPage(Model model, RedirectAttributes redirectAttributes) {
		if (model.getAttribute("loginMember") != null) {
			redirectAttributes.addFlashAttribute("redirectMsg", "已經登入!");
			return "redirect:/";
		}
		return "member/loginPage";
	}

	// 處理登入請求
	@PostMapping("/login")
	@ResponseBody
	public String loginPost(@RequestParam String account, @RequestParam String password,
			@RequestParam(required = false) Boolean addCookie, Model model,
			RedirectAttributes redirectAttributes, HttpServletResponse response) {
		System.err.println("是否存入Cookie?" + addCookie);
		if (model.getAttribute("loginMember") != null) {
			redirectAttributes.addFlashAttribute("redirectMsg", "已登入，請先登出!");
			return "redirect:/";
		}
		Member loginMember = memberService.checkLogin(account, password);
		if (loginMember == null) {
			// model.addAttribute("errorMsg", "登入失敗");
			return "登入失敗!";
		}
		loginMember = memberService.memberLogin(loginMember);
		model.addAttribute("loginMember", memberService.setLoginDto(loginMember));
		if (addCookie) {
			Cookie cookie = new Cookie("loginToken", loginMember.getLoginToken());
			cookie.setMaxAge(7 * 24 * 60 * 60);
			cookie.setPath("/PlayCentric");
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			response.addCookie(cookie);
		}
		return "登入成功!";
	}

	// 登入成功後回到首頁顯示登入成功
	@GetMapping("/loginSuccess")
	public String loginSeccess(RedirectAttributes redirectAttributes, Model model) {
		LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
		redirectAttributes.addFlashAttribute("redirectMsg", "登入錯誤，請重試!");
		if (loginMember != null) {
			String loginName = loginMember.getNickname();
			redirectAttributes.addFlashAttribute("redirectMsg", loginName + "登入成功!");
		}
		return "redirect:/";
	}

	// 登出
	@GetMapping("/logout")
	public String logout(SessionStatus status, RedirectAttributes redirectAttributes, HttpServletResponse response) {
		Cookie cookie = new Cookie("loginToken", null);
		cookie.setMaxAge(0);
		cookie.setPath("/PlayCentric");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		response.addCookie(cookie);

		status.setComplete();
		redirectAttributes.addFlashAttribute("okMsg", "登出完成");
		return "redirect:/";
	}

	@GetMapping("/back/memManage")
	public String managePage() {
		return "member/managePage";
	}

	@GetMapping("/back/api/searchMemPage")
	@ResponseBody
	public Page<Member> searchMemberByPage(@RequestParam("page") Integer page,
			@RequestParam("keyword") String keyword) {
		Page<Member> memPage = memberService.findByKeyword(keyword, page);
		for (Member member : memPage) {
			member.setPhotoUrl(
					member.getPhoto() != null
							? ngrokConfig.getUrl() + "/PlayCentric/imagesLib/image" + member.getPhoto()
							: member.getGoogleLogin() != null ? member.getGoogleLogin().getPhoto()
									: ngrokConfig.getUrl() + "/PlayCentric/imagesLib/image144");
		}
		return memPage;
	}

	@GetMapping("/back/api/getMember")
	@ResponseBody
	public Member getOneMember(@RequestParam("memId") Integer memId) {
		Member member = memberService.findById(memId);
		member.setPhotoUrl(
				member.getPhoto() != null ? ngrokConfig.getUrl() + "/PlayCentric/imagesLib/image" + member.getPhoto()
						: member.getGoogleLogin() != null ? member.getGoogleLogin().getPhoto()
								: ngrokConfig.getUrl() + "/PlayCentric/imagesLib/image144");
		return member;
	}

	@PostMapping("/personal/api/updateSelf")
	@ResponseBody
	public String updateSelfMember(@ModelAttribute Member member, @RequestParam("photoFile") MultipartFile photoFile,
			Model model)
			throws IOException {
		LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
		if (loginMember!=null && loginMember.getMemId()!=member.getMemId()) {
			return "anotherLogin";
		}
		return updateMember(member, photoFile, model);
	}

	@PostMapping("/back/api/update")
	@ResponseBody
	public String updateMember(@ModelAttribute Member member, @RequestParam("photoFile") MultipartFile photoFile,
			Model model)
			throws IOException {
		LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
		if (loginMember == null) {
			return "更新失敗!";
		}
		try {
			Member originMem = memberService.findById(member.getMemId());
			System.err.println("origin:" + originMem);
			System.err.println("updateMem:" + member);
			member.setPassword("updating");
			if (!hasInfo(member)) {
				return "請填妥資訊";
			}
			if (!originMem.getAccount().equals(member.getAccount())
					&& memberService.checkAccountExist(member.getAccount())) {
				return "帳號已存在";
			}
			if (!originMem.getEmail().equals(member.getEmail())
					&& memberService.checkEmailExist(member.getEmail(), member.getMemId())) {
				return "Email已註冊";
			}
			if (!photoFile.isEmpty()) {
				ImageLib imageLib = new ImageLib();
				imageLib.setImageFile(photoFile.getBytes());
				Integer imageId = imageLibService.saveImage(imageLib).getImageId();
				member.setPhoto(imageId);
			}
			memberService.updateMember(member, originMem);
			if (member.getMemId() == loginMember.getMemId()) {
				model.addAttribute("loginMember", memberService.setLoginDto(member));
			}
			return "更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "更新失敗!";
	}

	@DeleteMapping("/back/api/delete")
	@ResponseBody
	public String deleteMem(@RequestParam("memId") Integer memId, Model model) {
		LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
		if (loginMember == null) {
			return "刪除失敗!";
		}
		return memberService.deleteMemById(memId) ? "刪除成功!" : "刪除失敗!";
	}

	@GetMapping("/personal/Info")
	public String memInfoPage() {
		return "member/memInfoPage";
	}

	@GetMapping("/personal/recharge")
	public String memRechargePage() {
		return "member/memRechargePage";
	}

	@GetMapping("/personal/api/getInfo")
	@ResponseBody
	public Member getMemInfo(Model model) {
		LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
		if (loginMember == null) {
			return null;
		}
		return getOneMember(loginMember.getMemId());
	}

	@PostMapping("/personal/api/sendPtUrl")
	@ResponseBody
	public String sendPTEmail(Model model, @RequestParam Integer memId) {
		LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
		if (loginMember != null && loginMember.getMemId() != memId) {
			return "redirect:/member/homeShowErr/anotherLogin";
		}
		return sendPTEmail(memId);
	}

	@PostMapping("/api/sendPtUrl")
	@ResponseBody
	public String sendPTEmail(@RequestParam String accOrEmail) {
		Member member = memberService.findByAccOrEmail(accOrEmail);
		if (member==null) {
			return "信件已發送";
		}
		return sendPTEmail(member.getMemId());
	}

	// 寄送修改密碼信件
	public String sendPTEmail(@RequestParam Integer memId) {
		try {

			// 取得改密碼的帳號
			Member member = memberService.changePassword(memId);

			String changePwdUrl = ngrokConfig.getUrl() + "/PlayCentric/member/changePassword/"
					+ member.getPasswordToken();
			// 寄信
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("owen0414chen@gmail.com");
			message.setTo(member.getEmail());
			message.setSubject("PlayCentric會員 " + member.getAccount() + " 更改密碼");
			message.setText("更改密碼請點擊網址:" + changePwdUrl + "\r\n(若不是本人請忽略)");

			mailSender.send(message);
			return "信件已發送";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "寄信失敗，請重試";
	}

	@GetMapping("/changePassword/{token}")
	public String changePasswordPage(@PathVariable String token, Model model, RedirectAttributes redirectAttributes) {
		Member member = memberService.findByPwdToken(token);
		if (member == null) {
			redirectAttributes.addFlashAttribute("redirectMsg", "網址已過期!");
			return "redirect:/";
		}
		model.addAttribute("loginMember", memberService.setLoginDto(member));
		model.addAttribute("token", token);
		return "member/changePasswordPage";
	}

	@PostMapping("/changePassword")
	@ResponseBody
	public String changePassword(@RequestParam String password, @RequestParam String token, SessionStatus status) {
		try {
			memberService.changePassword(password, token);
			status.setComplete();
			return "修改完成!";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "修改失敗!";
	}

	@GetMapping("/changePwdOK")
	public String getMethodName(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("redirectMsg", "請重新登入");
		return "redirect:login";
	}

	@PostMapping("/personal/api/sendVerUrl")
	@ResponseBody
	public String sendVerEmail(Model model, @RequestParam String email) {

		// 判斷登入帳號
		LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
		if (loginMember == null) {
			return "錯誤，請重新登入";
		}
		Member member = memberService.verifyEmail(loginMember.getMemId());

		if (member == null) {
			return "錯誤，請重新登入";
		}
		String verifyUrl = ngrokConfig.getUrl() + "/PlayCentric/member/verifyEmail/" + member.getEmailVerifyToken();
		// 寄信
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("owen0414chen@gmail.com");
			message.setTo(email);
			message.setSubject("PlayCentric會員 " + member.getAccount() + " 驗證Email");
			message.setText("點擊網址驗證email:" + verifyUrl + "\r\n(若不是本人請忽略)");

			mailSender.send(message);
			return "信件已發送";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "寄信失敗，請重試";
	}

	@GetMapping("/verifyEmail/{token}")
	public String verifyEmail(@PathVariable String token, Model model, RedirectAttributes redirectAttributes) {
		Member member = memberService.verifyEmail(token);
		if (member == null) {
			redirectAttributes.addFlashAttribute("redirectMsg", "網址已過期!");
			return "redirect:/";
		}
		redirectAttributes.addFlashAttribute("redirectMsg", "Email驗證成功!");
		model.addAttribute("loginMember", memberService.setLoginDto(member));
		return "redirect:/";
	}

	private boolean hasInfo(Member member) {
		return member.getAccount() != null &&
				member.getEmail() != null &&
				member.getPassword() != null &&
				member.getNickname() != null &&
				member.getMemName() != null;
	}
}
