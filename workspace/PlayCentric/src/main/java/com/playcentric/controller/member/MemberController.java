	package com.playcentric.controller.member;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@SessionAttributes(names = {"loginMember"})
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private ImageLibService imageLibService;
	
	@GetMapping("/home")
	public String home() {
		return "member/home";
	}
	

	@GetMapping("/regist")
	public String registPage() {
		return "member/registMember";
	}
	
	@PostMapping("/regist")
	@ResponseBody
	public String registMemberTest(@ModelAttribute Member member,@RequestParam("photoFile") MultipartFile photoFile) throws IOException {
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
			memberService.addMember(member);
			return "註冊成功!";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "註冊失敗!";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "member/loginPage";
	}

	@PostMapping("/login")
	public String loginPost(@RequestParam String account,@RequestParam String password, Model model, RedirectAttributes redirectAttributes) {
		Member loginMember = memberService.checkLogin(account, password);
		if (loginMember==null) {
			model.addAttribute("errorMsg", "登入失敗");
			return "member/loginPage";
		}
		loginMember = memberService.memberLogin(loginMember);
		model.addAttribute("loginMember", new LoginMemDto(loginMember));
		redirectAttributes.addFlashAttribute("okMsg", "登入成功");
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(SessionStatus status, RedirectAttributes redirectAttributes) {
		status.setComplete();
		redirectAttributes.addFlashAttribute("okMsg", "登出完成");
		return "redirect:/";
	}
	
	@GetMapping("/memManage")
	public String managePage(Model model) {
		return "member/managePage";
	}

	@GetMapping("/searchMemPage")
	@ResponseBody
	public Page<Member> searchMemberByPage(@RequestParam("page") Integer page,@RequestParam("keyword") String keyword) {
		Page<Member> memPage = memberService.findByKeyword(keyword, page);
		for (Member member : memPage) {
			member.setPhotoUrl(member.getPhoto()!=null? "http://localhost:8080/PlayCentric/imagesLib/image"+member.getPhoto():
			member.getGoogleLogin()!=null? member.getGoogleLogin().getPhoto():
			"http://localhost:8080/PlayCentric/imagesLib/image144");
		}
		return memPage;
	}

	@GetMapping("/getMember")
	@ResponseBody
	public Member showMemberByPage(@RequestParam("memId") Integer memId) {
		Member member = memberService.findById(memId);
		member.setPhotoUrl(member.getPhoto()!=null? "http://localhost:8080/PlayCentric/imagesLib/image"+member.getPhoto():
		member.getGoogleLogin()!=null? member.getGoogleLogin().getPhoto():
		"http://localhost:8080/PlayCentric/imagesLib/image144");
		return member;
	}

	@PostMapping("/update")
	@ResponseBody
	public String updateMemberTest(@ModelAttribute Member member,@RequestParam("photoFile") MultipartFile photoFile) throws IOException {
		try {
			Member originMem = memberService.findById(member.getMemId());
			System.err.println("origin:"+originMem);
			System.err.println("updateMem:"+member);
			member.setPassword("updating");
			if (!hasInfo(member)) {
				return "請填妥資訊";
			}
			if (!originMem.getAccount().equals(member.getAccount()) && memberService.checkAccountExist(member.getAccount())) {
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
			memberService.updateMember(member,originMem);
			return "更新成功!";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "更新失敗!";
	}

	@DeleteMapping("/deleteMem")
	@ResponseBody
	public String deleteMem(@RequestParam("memId") Integer memId){
		return memberService.deleteMemById(memId)? "刪除成功":"刪除失敗";
	}
	
	
	private boolean hasInfo(Member member) {
		return member.getAccount()!=null &&
				member.getEmail()!=null &&
				member.getPassword()!=null &&
				member.getNickname()!=null &&
				member.getMemName()!=null;
	}
}
