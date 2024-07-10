package com.playcentric.model.playfellow;

import com.playcentric.model.game.primary.Game;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pfGame")
public class PfGame {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pfGameId;// 伴遊遊戲編號

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "playFellowId") // 伴遊者id
	private PlayFellowMember playFellowMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gameId") // 遊戲id 一個伴遊者會有多個遊戲OneToMany
	private Game game;

	private String pricingCategory;// 計費方式(小時or計次)單位初始只能1

	private Integer amount;// 單價金額 沒小數

	private Byte pfGameStatus;// 狀態 1:開啟 2:關閉
}
