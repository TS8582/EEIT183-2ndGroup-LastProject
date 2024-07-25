package com.playcentric.model.game.transaction;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity @Table(name = "gameOrderDetails")
@IdClass(GameOrderDetailsId.class)
public class GameOrderDetails {
	
	@Id
	private Integer gameOrderId;
	@Id
	private Integer gameId;
	private Integer amount;
	private Integer unitPrice;
	private BigDecimal discountRate;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gameId",insertable = false,updatable = false)
	private Game game;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gameOrderId",insertable = false,updatable = false)
	private GameOrder gameOrder;
	
}
