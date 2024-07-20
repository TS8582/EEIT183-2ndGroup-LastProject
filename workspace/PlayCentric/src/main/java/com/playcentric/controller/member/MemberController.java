package com.playcentric.controller.member;

import java.io.IOException;
import java.util.UUID;

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

import com.playcentric.model.ImageLib;
import com.playcentric.model.member.LoginMemDto;
import com.playcentric.model.member.Member;
import com.playcentric.service.ImageLibService;
import com.playcentric.service.member.MemberService;


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


	@PostMapping("/back/regist")
	@ResponseBody
	public String backRegistMember(@ModelAttribute Member member, @RequestParam("photoFile") MultipartFile photoFile, Model model) throws IOException{
		return registMember(member, photoFile, model);
	}

	@PostMapping("/regist")
	@ResponseBody
	public String registMember(@ModelAttribute Member member, @RequestParam("photoFile") MultipartFile photoFile, Model model)
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
			LoginMemDto loginMember = (LoginMemDto)model.getAttribute("loginMember");
			loginMember = memberService.checkLoginMember(loginMember);
			if (loginMember==null || loginMember.getRole() != 1) {
				member.setRole((short)0);
			}
			memberService.addMember(member);
			return "註冊成功!";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "註冊失敗!";
	}

	//測試doAlert() 用
	@GetMapping("/homeShowErr/{err}")
	public String homeShowErr(@PathVariable String err, RedirectAttributes redirectAttributes) {
		String redirectMsg = "";
		if ("notMng".equals(err)) {
			redirectMsg = "您不是管理員!";
		}
		redirectAttributes.addFlashAttribute("redirectMsg", redirectMsg);
		return "redirect:/";
	}
	
	//測試doAlert() 用
	@GetMapping("/showLoginErr/{err}")
	public String showLoginErr(@PathVariable String err, RedirectAttributes redirectAttributes) {
		String redirectMsg = "";
		if ("notLogin".equals(err)) {
			redirectMsg = "請先登入會員!";
		}
		redirectAttributes.addFlashAttribute("redirectMsg", redirectMsg);
		return "redirect:/member/login";
	}

	@GetMapping("/login")
	public String loginPage(Model model, RedirectAttributes redirectAttributes) {
		if (model.getAttribute("loginMember") != null) {
			redirectAttributes.addFlashAttribute("redirectMsg","已經登入!");
			return "redirect:/";
		}
		return "member/loginPage";
	}

	@PostMapping("/login")
	@ResponseBody
	public String loginPost(@RequestParam String account, @RequestParam String password, Model model,
			RedirectAttributes redirectAttributes) {
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
		// redirectAttributes.addFlashAttribute("okMsg", "登入成功");
		return "登入成功!";
	}

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

	@GetMapping("/logout")
	public String logout(SessionStatus status, RedirectAttributes redirectAttributes) {
		status.setComplete();
		redirectAttributes.addFlashAttribute("okMsg", "登出完成");
		return "redirect:/";
	}

	@GetMapping("/back/memManage")
	public String managePage() {
		return "member/managePage";
	}

	@GetMapping("/back/searchMemPage")
	@ResponseBody
	public Page<Member> searchMemberByPage(@RequestParam("page") Integer page,
			@RequestParam("keyword") String keyword) {
		Page<Member> memPage = memberService.findByKeyword(keyword, page);
		for (Member member : memPage) {
			member.setPhotoUrl(
					member.getPhoto() != null ? "http://localhost:8080/PlayCentric/imagesLib/image" + member.getPhoto()
							: member.getGoogleLogin() != null ? member.getGoogleLogin().getPhoto()
									: "http://localhost:8080/PlayCentric/imagesLib/image144");
		}
		return memPage;
	}

	@GetMapping("/back/getMember")
	@ResponseBody
	public Member showMemberByPage(@RequestParam("memId") Integer memId) {
		Member member = memberService.findById(memId);
		member.setPhotoUrl(
				member.getPhoto() != null ? "http://localhost:8080/PlayCentric/imagesLib/image" + member.getPhoto()
						: member.getGoogleLogin() != null ? member.getGoogleLogin().getPhoto()
								: "http://localhost:8080/PlayCentric/imagesLib/image144");
		return member;
	}



	@PostMapping("/updateSelf")
	@ResponseBody
	public String updateSelfMember(@ModelAttribute Member member, @RequestParam("photoFile") MultipartFile photoFile, Model model)
			throws IOException {
		LoginMemDto loginMember = (LoginMemDto)model.getAttribute("loginMember");
		loginMember = memberService.checkLoginMember(loginMember);
		if (loginMember==null || loginMember.getMemId() != member.getMemId()) {
			return "請重新登入!";
		}
		return updateMember(member, photoFile, model);
	}

	@PostMapping("/back/update")
	@ResponseBody
	public String updateMember(@ModelAttribute Member member, @RequestParam("photoFile") MultipartFile photoFile, Model model)
			throws IOException {
		LoginMemDto loginMember = (LoginMemDto)model.getAttribute("loginMember");
		loginMember = memberService.checkLoginMember(loginMember);
		if (loginMember==null || loginMember.getRole() != 1) {
			return "無權修改!";
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
			if (!originMem.getEmail().equals(member.getEmail()) && memberService.checkEmailExist(member.getEmail())) {
				return "Email已註冊";
			}
			if (!photoFile.isEmpty()) {
				ImageLib imageLib = new ImageLib();
				imageLib.setImageFile(photoFile.getBytes());
				Integer imageId = imageLibService.saveImage(imageLib).getImageId();
				member.setPhoto(imageId);
			}
			memberService.updateMember(member, originMem);
			if (member.getMemId()==loginMember.getMemId()) {
				model.addAttribute("loginMember",memberService.setLoginDto(member));
			}
			return "更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "更新失敗!";
	}

	@DeleteMapping("/back/delete")
	@ResponseBody
	public String deleteMem(@RequestParam("memId") Integer memId, Model model) {
		LoginMemDto loginMember = (LoginMemDto)model.getAttribute("loginMember");
		loginMember = memberService.checkLoginMember(loginMember);
		if (loginMember==null || loginMember.getRole() != 1) {
			return "無權刪除!";
		}
		return memberService.deleteMemById(memId) ? "刪除成功" : "刪除失敗";
	}

	@GetMapping("/memInfo")
	public String memInfoPage(Model model, RedirectAttributes redirectAttributes) {
		LoginMemDto loginMember = (LoginMemDto)model.getAttribute("loginMember");
		loginMember = memberService.checkLoginMember(loginMember);
		if (loginMember == null) {
			redirectAttributes.addFlashAttribute("redirectMsg", "請先登入會員!");
			return "redirect:login";
		}
		return "member/memInfoPage";
	}

	@GetMapping("/getMemInfo")
	@ResponseBody
	public Member getMemInfo(Model model) {
		LoginMemDto loginMember = (LoginMemDto) model.getAttribute("loginMember");
		if (loginMember != null) {
			return memberService.findById(loginMember.getMemId());
		}
		return null;
	}

	@PostMapping("/sendPtUrl")
	@ResponseBody
	public String sendPTEmail(Model model,@RequestParam String email) {

		//生成Token
		String token = UUID.randomUUID().toString();
		String changePwdUrl = "http://localhost:8080/PlayCentric/member/changePassword/"+token;
		
		//判斷登入帳號
		LoginMemDto loginMember = (LoginMemDto)model.getAttribute("loginMember");
		if (loginMember == null) {
			return "錯誤，請重新登入";
		}
		Member member = memberService.changePassword(loginMember.getMemId(), token);

		if (member == null) {
			return "錯誤，請重新登入";
		}
		//寄信
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("owen0414chen@gmail.com");
			message.setTo(email);
			message.setSubject("PlayCentric會員 "+member.getAccount()+" 更改密碼");
			message.setText("更改密碼請點擊網址:"+changePwdUrl+"\r\n(若不是本人請忽略)");

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
			redirectAttributes.addFlashAttribute("redirectMsg","網址已過期!");
			return "redirect:/";
		}
		model.addAttribute("loginMember", memberService.setLoginDto(member));
		model.addAttribute("token", token);
		return "member/changePasswordPage";
	}

	@PostMapping("/changePassword")
	@ResponseBody
	public String changePassword(@RequestParam String password,@RequestParam String token, SessionStatus status) {
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
		redirectAttributes.addFlashAttribute("redirectMsg","請重新登入");
		return "redirect:login";
	}

	@PostMapping("/sendVerUrl")
	@ResponseBody
	public String sendVerEmail(Model model,@RequestParam String email) {

		//生成Token
		String token = UUID.randomUUID().toString();
		String changePwdUrl = "http://localhost:8080/PlayCentric/member/verifyEmail/"+token;
		
		//判斷登入帳號
		LoginMemDto loginMember = (LoginMemDto)model.getAttribute("loginMember");
		if (loginMember == null) {
			return "錯誤，請重新登入";
		}
		Member member = memberService.verifyEmail(loginMember.getMemId(), token);

		if (member == null) {
			return "錯誤，請重新登入";
		}
		//寄信
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("owen0414chen@gmail.com");
			message.setTo(email);
			message.setSubject("PlayCentric會員 "+member.getAccount()+" 驗證Email");
			message.setText("點擊網址驗證email:"+changePwdUrl+"\r\n(若不是本人請忽略)");

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
			redirectAttributes.addFlashAttribute("redirectMsg","網址已過期!");
			return "redirect:/";
		}
		redirectAttributes.addFlashAttribute("redirectMsg","Email驗證成功!");
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
