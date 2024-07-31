package com.playcentric.model.forum;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.member.Member;

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
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
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
	private Integer textsId;

	@Column(insertable = false, updatable = false)
	private Integer forumId;

	// @JsonIgnore
	@ManyToOne
	@JoinColumn(name = "forumId")
	private Forum forum;

	@Column(insertable = false, updatable = false)
	private Integer memId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "memId")
	private Member member;

	@JsonIgnore
	@OneToMany(mappedBy = "texts", cascade = CascadeType.ALL)
	private List<Talk> talk = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "texts")
    private List<ForumPhoto> forumPhoto = new ArrayList<>();

	@Transient
	private String photoPath;

	private String title;

	private String textsContent;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy年MM月dd日 HH:mm")
	// @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 若要在 thymeleaf 強制使用本格式，需雙層大括號
	@Temporal(TemporalType.TIMESTAMP)
//    @CreationTimestamp
	private Timestamp doneTime = new Timestamp(System.currentTimeMillis());

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
//    @UpdateTimestamp
	private Timestamp updatedTime = new Timestamp(System.currentTimeMillis());

	private Integer textsLikeNum;

	private Boolean hideTexts = false;

	@PrePersist
	@PostLoad
	public void initializeText(){
		this.photoPath = "";
		if (this.getForumPhoto().size()>0) {
			this.photoPath = "/PlayCentric/photo/showPhoto2?textsId="+this.textsId;
		}
	}

}