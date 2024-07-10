package com.playcentric.service.announcement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playcentric.model.announcement.Announcement;
import com.playcentric.model.announcement.AnnouncementRepository;
import com.playcentric.model.announcement.AnnouncementType;
import com.playcentric.model.announcement.AnnouncementTypeRepository;

@Service
public class AnnouncementService {
	
	@Autowired
	private AnnouncementRepository aRepo;
	
	@Autowired
	private AnnouncementTypeRepository aTRepo;
	
	//用ID找公告
	public Announcement findById(Integer id) {
		return aRepo.findById(id).get();
	}
	
	//取得全部公告
	public List<Announcement> findAll() {
		return aRepo.findAll();
	}
	
	//更新以id找到的公告
	public Announcement updateById(Integer id,Announcement announcement) {
		Announcement anno = findById(id);
		anno.setAnnoTypeId(announcement.getAnnoTypeId());
		anno.setAnnouncementType(announcement.getAnnouncementType());
		anno.setContent(announcement.getContent());
		anno.setLastEditAt(announcement.getLastEditAt());
		anno.setTitle(announcement.getTitle());
		return aRepo.save(anno);
	}
	
	//新增公告
	public Announcement insert(Announcement announcement) {
		return aRepo.save(announcement);
	}
	
	//刪除公告(id)
	public void delete(Integer id) {
		aRepo.deleteById(id);
	}
	
	//id找公告類型
	public AnnouncementType findTypeById(Integer id) {
		return aTRepo.findById(id).get();
	}
	
	//取得所有公告類型
	public List<AnnouncementType> findAllType() {
		return aTRepo.findAll();
	}
	
	//取得某類型的公告
	public List<Announcement> findByAnnoTypeId(Integer typeId) {
		return aRepo.findByAnnoTypeId(typeId);
	}
	
}
