package com.playcentric.model.forum;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.member.Member;

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
@Table(name = "texts")
public class Texts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int textsId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "forumId")
	private Forum forum;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memId")
	private Member member;

	private String title;

	private String textsContent;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 若要在 thymeleaf 強制使用本格式，需雙層大括號
	@Temporal(TemporalType.TIMESTAMP)
	private Date doneTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 若要在 thymeleaf 強制使用本格式，需雙層大括號
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;

	private int textsLikeNum;

	private boolean hideTexts;

	public Texts(Forum forum, Member member, String title, String textsContent, Date doneTime, Date updatedTime,
			int textsLikeNum, boolean hideTexts) {
		this.forum = forum;
		this.member = member;
		this.title = title;
		this.textsContent = textsContent;
		this.doneTime = doneTime;
		this.updatedTime = updatedTime;
		this.textsLikeNum = textsLikeNum;
		this.hideTexts = hideTexts;
	}

}