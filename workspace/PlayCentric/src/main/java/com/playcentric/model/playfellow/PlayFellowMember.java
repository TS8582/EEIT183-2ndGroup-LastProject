package com.playcentric.model.playfellow;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.ImageLib;
import com.playcentric.model.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "playFellowMember")
public class PlayFellowMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer playFellowId; // id 自增 遞增

	private String pfnickname; // 暱稱

	private String pfdescription; // 自介

	

	@OneToMany(mappedBy = "playFellowMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageLibPfmemberAssociation> imageLibAssociations; // 使用中介表管理多張照片的關聯

	private Byte pfstatus; // 狀態:1 待審核、2審核失敗、 3開啟，4關閉

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pfcreatedTime; // 創建時間 SQL自建

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memId")
	private Member member;// fK memId

	@PrePersist // 當物件要轉換成 Persistent 狀態以前，先做以下方法
	public void onCreate() {
		if (pfcreatedTime == null) {
			pfcreatedTime = new Date();
		}
	}

}