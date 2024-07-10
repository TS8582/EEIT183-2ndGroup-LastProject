package com.playcentric.controller.playfellow;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.playcentric.model.member.Member;
import com.playcentric.model.member.MemberRepository;
import com.playcentric.model.playfellow.PlayFellowMember;
import com.playcentric.model.playfellow.PlayFellowMemberRepository;
import com.playcentric.service.playfellow.PlayFellowMemberService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class PlayFellowMemberController {

	@Autowired
	PlayFellowMemberService playFellowMemberService;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PlayFellowMemberRepository playFellowMemberRepository;
	
	

	@GetMapping("/playFellow/addImage")
	public String addImage() {
		return "playFellow/addImages";
	}
	
	@GetMapping("/playFellow/showImage")
	public String showImage() {
		return "playFellow/showImages";
	}
	
	@GetMapping("/playFellow/sideBar")
	public String sideBar() {
		return "playFellow/sideBar";
	}
	
	
	@GetMapping("/playFellow/cms")
	public String getMethodName(Model model) {
		List<PlayFellowMember> playFellowMembers = playFellowMemberService.getAllPlayFellowMembers();
		model.addAttribute("PlayFellowMember", playFellowMembers);

		return "playFellow/playFellowCMS";
	}

	@GetMapping("/playFellow/memId/{memId}") // 從memId下去查
										//用url去抓
	public String getMemberByIdAndAddPFmem(@PathVariable int memId, Model model) {
		Optional<Member> optMember = memberRepository.findById(memId);// 找會員id 性別和生日存到model
		System.out.println(memId);

		if (optMember.isPresent()) {
			Member member = optMember.get();
			model.addAttribute("members", member);

			return "playFellow/addPlayFellowMember";

		} else {
			model.addAttribute("錯誤", "查無會員或未登入");
			return "member/registMember";
		}
	}

	

	// 檢查暱稱是否重複
	@GetMapping("/playFellow/checkNickname")
	public ResponseEntity<String> checkNickname(@RequestParam String pfnickname) {
		boolean isDuplicate = playFellowMemberService.isNicknameExists(pfnickname);
		if (isDuplicate) {
			return ResponseEntity.ok("伴遊暱稱重複");
		} else {
			return ResponseEntity.ok("伴遊暱稱沒重複");
		}

	}

	

	@GetMapping("/playFellow/updateMember/{pfmemberId}")
	public String getPlayFellowMemberMsg(@PathVariable Integer pfmemberId, Model model) {
		Optional<PlayFellowMember> optPlayFellowMember = playFellowMemberRepository.findById(pfmemberId);
		System.out.println(pfmemberId);

		if (optPlayFellowMember.isPresent()) {
			PlayFellowMember playFellowMember = optPlayFellowMember.get();

			model.addAttribute("pfnickname", playFellowMember.getPfnickname())
					.addAttribute("pfdescription", playFellowMember.getPfdescription())
					.addAttribute("playFellowId", playFellowMember.getPlayFellowId())
					.addAttribute("pfstatus", playFellowMember.getPfstatus())
					.addAttribute("pfcreatedTime", playFellowMember.getPfcreatedTime());

			return "playFellow/updateMember"; // 返回伴遊者資訊的視圖
		} else {
			model.addAttribute("error", "查無此伴遊者，請重新登入");
			return "member/registMember"; // 返回錯誤視圖或路徑
		}
	}
	
	@GetMapping("/playFellow/reviewpfMember")
	public String postMethodName(Model model) {
		
		Byte pfstatus = 1;
		List<PlayFellowMember> playFellowMembers = playFellowMemberService.findPlayFellowMemberBypfStatus(pfstatus);
		model.addAttribute("PlayFellowMembers",playFellowMembers);
		
		return "playFellow/reviewpfMember";
	}
	



}
