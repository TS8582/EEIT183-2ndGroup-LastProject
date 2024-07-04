package com.playcentric.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberDto;
import com.playcentric.service.member.MemberService;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/member")
@SessionAttributes(names = {"loginMember"})
public class MemberController {

	@Autowired
	private MemberService memberService;
	
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
	public String registMember(@ModelAttribute Member member) {
		try {
			if (hasInfo(member)) {
				memberService.addMember(member);
				return "註冊成功!";
			}
			return "請填妥資料";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "註冊失敗";
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
		model.addAttribute("loginMember", new MemberDto(loginMember));
		redirectAttributes.addFlashAttribute("okMsg", "登入成功");
		return "redirect:home";
	}
	
	@GetMapping("/logout")
	public String logout(SessionStatus status, RedirectAttributes redirectAttributes) {
		status.setComplete();
		redirectAttributes.addFlashAttribute("okMsg", "登出完成");
		return "redirect:home";
	}
	
	@GetMapping("/memManage")
	public String managePage(Model model) {
		Page<Member> memPage = memberService.findByPage(1);
		model.addAttribute("memPage", memPage);
		return "member/managePage";
	}
	
	private boolean hasInfo(Member member) {
		return member.getAccount()!=null &&
				member.getEmail()!=null &&
				member.getPassword()!=null &&
				member.getNickname()!=null &&
				member.getMemName()!=null;
	}
}
