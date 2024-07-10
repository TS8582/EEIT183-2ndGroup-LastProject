package com.playcentric.model.announcement;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity @Table(name = "announcementType")
public class AnnouncementType {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer annoTypeId;
	private String annoTypeName;
	
	@JsonIgnore
	@OneToMany(mappedBy = "announcementType", 
	cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Announcement> announcements;
	
}
