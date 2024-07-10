package com.playcentric.model.game.primary;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.playcentric.model.ImageLib;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "game")
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gameId;
	private String gameName;
	private Integer price;
	private String description;
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy年MM月dd日")
	@DateTimeFormat(pattern = "yyyy年MM月dd日")
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime releaseAt;
	private String developer;
	private String publisher;
	private String gameFilePath;
	private Integer totalReviews;
	private Integer totalScore;
	private Boolean isShow = true;
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(name = "gameTypes",
	joinColumns = @JoinColumn(name ="gameId"),
	inverseJoinColumns = @JoinColumn(name = "gameTypeId"))
	private List<GameTypeLib> gameTypeLibs;
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(name = "gameImages",
	joinColumns = @JoinColumn(name ="gameId"),
	inverseJoinColumns = @JoinColumn(name = "imageId"))
	private List<ImageLib> imageLibs;
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "game")
	private List<GameDiscount> gameDiscounts;
	@Transient
	private Boolean isFirstRelease = true;
}
