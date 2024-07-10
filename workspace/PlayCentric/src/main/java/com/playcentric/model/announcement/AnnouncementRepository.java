package com.playcentric.model.announcement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement,Integer>{
	
	List<Announcement> findByAnnoTypeId(Integer annoTypeId);
	
}
