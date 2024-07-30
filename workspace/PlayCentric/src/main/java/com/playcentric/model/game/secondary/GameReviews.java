package com.playcentric.model.game.secondary;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity @Table(name = "gameReviews")
public class GameReviews {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gameReviewsId;
	private Integer gameId;
	private Integer memId;
	private String reviewsContent;
	private Integer reviewsScore;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日 HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime reviewsAt = LocalDateTime.now();
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日 HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime lastEditAt;
	private boolean isShow = true;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memId",insertable = false,updatable = false)
	private Member member;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gameId",insertable = false,updatable = false)
	private Game game;
}
