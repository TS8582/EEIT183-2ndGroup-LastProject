package com.playcentric.model.game.transaction;

import java.time.LocalDateTime;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gameOrder")
public class GameOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gameOrderId;
	@Column(insertable=false, updatable=false)
	private Integer memId;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;
	private Integer status = 1;//1為成功購買 0為取消訂單
	@Column(insertable=false, updatable=false)
	private Integer paymentId;
	private Integer total;
	private String tradeNo;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memId")
	private Member member;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paymentId")
	private Payment payment;
}
