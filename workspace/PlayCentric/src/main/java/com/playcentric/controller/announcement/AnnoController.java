package com.playcentric.controller.announcement;

import java.util.Date;
import java.util.List;

import org.apache.tomcat.util.openssl.openssl_h;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.playcentric.model.announcement.Announcement;
import com.playcentric.model.announcement.AnnouncementType;
import com.playcentric.service.announcement.AnnouncementService;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class AnnoController {
	
	@Autowired
	private AnnouncementService aService;
	
	//獲取新增公告資料
	@GetMapping("/anno/getInsertAnnoInfo")
	public String getInsertInfo(Model model) {
		List<AnnouncementType> allType = aService.findAllType();
		model.addAttribute("allAnnoType",allType);
		return "announcement/insertAnno";
	}
	
	//進行新增公告後轉回管理頁面
	@PostMapping("/anno/insertAnno")
	public String insertInfo(@RequestParam String title,
			@RequestParam String content,
			@RequestParam Integer typeId,Model model) {
		Announcement anno = new Announcement();
		anno.setAnnoTypeId(typeId);
		anno.setContent(content);
		anno.setTitle(title);
		anno.setAnnouncementType(aService.findTypeById(typeId));
		aService.insert(anno);
		
		List<AnnouncementType> allType = aService.findAllType();
		model.addAttribute("allAnnoType",allType);
		return "redirect:test-index";
	}
	
	//顯示所有公告
	@GetMapping("/anno/showAllAnno")
	public String showAllAnno(Model model) {
		List<Announcement> allAnno = aService.findAll();
		model.addAttribute("allAnno",allAnno);
		return "test-index";
	}
	
	//單個公告詳情
	@GetMapping("/anno/showOneAnno")
	public String getMethodName(@RequestParam Integer annoId,Model model) {
		Announcement anno = aService.findById(annoId);
		model.addAttribute("anno",anno);
		return "announcement/showOneAnno";
	}
	
	//獲取修改公告資料
	@GetMapping("/anno/getUpdatAnno")
	public String getUpdateAnno(@RequestParam Integer annoId,Model model) {
		Announcement anno = aService.findById(annoId);
		List<AnnouncementType> allType = aService.findAllType();
		model.addAttribute("allAnnoType",allType);
		model.addAttribute("anno",anno);
		return "announcement/getupdateanno";
	}
	
	@PostMapping("/anno/updateAnno")
	public String updateAnno(@RequestParam Integer annoId,
			@RequestParam String title,
			@RequestParam String content,
			@RequestParam Integer typeId) {
		AnnouncementType annoType = aService.findTypeById(typeId);
		Announcement anno = new Announcement();
		anno.setTitle(title);
		anno.setContent(content);
		anno.setAnnoTypeId(typeId);
		anno.setLastEditAt(new Date(System.currentTimeMillis()));
		anno.setAnnouncementType(annoType);
		aService.updateById(annoId, anno);
		return "redirect:/anno/showAllAnno";
	}
	
	@GetMapping("/anno/deleteAnno")
	public String deleteAnno(@RequestParam Integer annoId) {
		aService.delete(annoId);
		return "redirect:/anno/showAllAnno";
	}
	
	@GetMapping("/test")
	public String getMethodName() {
		return "nav";
	}
	
	
}