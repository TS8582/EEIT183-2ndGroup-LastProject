package com.playcentric.controller.announcement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.playcentric.model.announcement.Announcement;
import com.playcentric.service.announcement.AnnouncementService;

@RestController
public class AnnoRestController {
	
	@Autowired
	private AnnouncementService aService;
	
	//回傳某類型的公告json(ajax)
		@GetMapping("/anno/getOneTypeAnno")
		public List<Announcement> getOneTypeAnno(@RequestParam Integer typeId) {
			return aService.findByAnnoTypeId(typeId);
		}
		
		//回傳全部公告json(ajax)
			@GetMapping("/anno/getAllAnno")
			public List<Announcement> getAllAnno() {
				return aService.findAll();
			}
	
}
