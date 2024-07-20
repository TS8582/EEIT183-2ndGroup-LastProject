package com.playcentric.controller.forum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.playcentric.model.forum.Talk;
import com.playcentric.model.forum.Texts;
import com.playcentric.service.forum.TalkService;
import com.playcentric.service.forum.TextsService;
import com.playcentric.service.member.MemberService;

@Controller
public class TalkController {

	@Autowired
	private TalkService talkService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private TextsService textsService;

	// 導入後台
	@GetMapping("/findAllTalk")
	public String findAllTalk(Model model) {

		List<Talk> arrayList = talkService.getAllTalk();

		model.addAttribute("arrayList", arrayList);

		return "forum/talk/talkTable";
	}

	// 新增訊息再當在ID
	@GetMapping("/texts/{textsId}/talk")
	public String getTalkByTextsId(@PathVariable Integer textsId, Model model) {

		Texts texts = textsService.findById(textsId);
		List<Talk> talk = talkService.getTalkByTextsId(textsId);

		model.addAttribute("texts", texts);
		model.addAttribute("talk", talk);
		model.addAttribute("textsId", textsId);
		return "forum/texts/textsContent";
	}

	@PostMapping("/talk/addByTextsId")
	public String createTalk(@RequestParam Integer textsId, @RequestParam String talkContent) {
		Talk talk = new Talk();

		talk.setTalkContent(talkContent);
		Texts texts = new Texts();
		texts.setTextsId(textsId);
		talk.setTexts(texts);
		talkService.insert(talk);
		return "redirect:/texts/" + textsId + "/talk";
	}

	// 新增跳轉頁面
	@GetMapping("/talk/add")
	public String insertTalk(Model model) {
		Talk lastestTalk = talkService.findLastestTalk();
		model.addAttribute("lastestTalk", lastestTalk);

		return "forum/Talk/addTalkPage";
	}

//	// Ajax分頁
//	@GetMapping("/talk/page")
//	public String findByPage(@RequestParam(value = "p", defaultValue = "1") Integer pageNum, Model model) {
//
//		Page<Talk> page = talkService.findByPage(pageNum);
//
//		model.addAttribute("page", page);
//
//		return "forum/talk/listTalkPageAjax";
//	}

	// 尋找修改ID
	@GetMapping("/talk/edit")
	public String editTalk(@RequestParam Integer talkId, Model model) {
		Talk talk = talkService.findTalkById(talkId);
		model.addAttribute("talk", talk);

		return "forum/talk/editPage";
	}

	// 更新談話
    @PutMapping("/talk/update")
    public String updateTalk(@RequestParam("talkId") Integer talkId,
                             @RequestParam("talkContent") String talkContent,
                             Model model) {
        Talk talk = talkService.findTalkById(talkId);
        if (talk != null) {
            talk.setTalkContent(talkContent);
            talkService.insert(talk);
        }
        return "redirect:/findAllTalk";
    }

	// 刪除留言
	@GetMapping("/talk/delete")
	public String deleteTalk(@RequestParam Integer talkId) {
		talkService.deleteTalkById(talkId);
		return "redirect:/findAllTalk";
	}

	// 新增一筆後，回傳傳最新的前三筆(ajax)
	@ResponseBody
	@PostMapping("/talk/ajaxPost")
	public Page<Talk> ajaxPost(@RequestBody Talk talk) {

		return talkService.addOneAndReturnThree(talk);
	}

	@ResponseBody
	@GetMapping("/talk/api/page")
	public Page<Talk> findByPageApi(@RequestParam("p") Integer page) {
		return talkService.findByPage(page);
	}
}
