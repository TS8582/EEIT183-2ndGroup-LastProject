package com.playcentric.model.forum;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "talk")
public class Talk {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer talkId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "textsId")
	private Texts texts;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "memId")
	private Member member;
	
	@Column(insertable = false, updatable = false)
	private Integer memId;
	
	private String talkContent;
	
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 若要在 thymeleaf 強制使用本格式，需雙層大括號
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp talkTime = new Timestamp(System.currentTimeMillis());
	
	private Integer talkLikeNum;
	
	private boolean hideTalk = false;

	

	
}
