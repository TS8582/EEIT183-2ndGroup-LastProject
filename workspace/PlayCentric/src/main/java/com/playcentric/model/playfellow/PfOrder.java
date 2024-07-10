package com.playcentric.model.playfellow;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.playcentric.model.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "pfOrder")
public class PfOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pfOrderId;//訂單編號,主鍵,自增
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pfGameId")
	private PfGame pfGame;//伴遊遊戲編號
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memId")
	private Member member;//會員編號(下訂者)
	
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date added;//創建時間
	
	@DateTimeFormat(pattern= "yyyy-MM-dd HH:mm:ss")
	private Date paymentTime;//付款時間
	
	private String transactionID;//交易ID
	
	private Integer paymentStatus;//付款狀態 INT 1:交易中 2交易完成 3交易失敗(或取消)
	
	

}
