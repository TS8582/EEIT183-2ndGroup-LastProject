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
@Table(name = "talkLike")
public class TalkLike {

	@EmbeddedId
	private TalkLikeId talkLikeId;

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

}
