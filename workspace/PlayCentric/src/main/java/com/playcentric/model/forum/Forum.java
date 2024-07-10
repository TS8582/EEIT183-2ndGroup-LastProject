package com.playcentric.model.forum;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.ImageLib;
import com.playcentric.model.game.primary.Game;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "forum")
public class Forum {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int forumId;

	@JsonIgnore
	@OneToOne( fetch = FetchType.LAZY )
	@JoinColumn(name = "gameId")
	private Game game;

//	@JsonIgnore
//	@OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	private List<ImageLib> imageLib = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Texts> texts = new ArrayList<>();

	private String textsIntro;
	
	@Transient
	private String gameName;
//	private ForumGameDto gameIfo;
	
//	public Forum() {
//		Game game = getGame();
//		gameIfo.setGameId(game.getGameId());
//		gameIfo.setGameName(game.getGameName());
//		List<ImageLib> imageLibs = game.getImageLibs();
//		byte[] gameImg = imageLibs.get(0).getImageFile();
//		gameIfo.setGameImg(gameImg);
//	}
	
	public Forum(List<Texts> texts, String textsIntro) {
		this();
		this.texts = texts;
		this.textsIntro = textsIntro;
	}

}
