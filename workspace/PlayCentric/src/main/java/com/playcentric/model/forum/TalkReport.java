package com.playcentric.model.forum;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.member.Member;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "talkReport")
public class TalkReport {

	@EmbeddedId
	private TalkReportId talkReportId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("talkId")
	@JoinColumn(name = "talkId")
	private Talk talk;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("memId")
	@JoinColumn(name = "memId")
	private Member member;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("reportId")
	@JoinColumn(name = "reportId")
	private Report report;

	private String tkReason;

	private byte tkCondition;

	public TalkReport(String tkReason, byte tkCondition) {
		this.tkReason = tkReason;
		this.tkCondition = tkCondition;
	}

	

}
