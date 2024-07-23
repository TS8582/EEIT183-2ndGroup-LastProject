package com.playcentric.model.playfellow;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.playcentric.model.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "pfOrder3")
public class PfOrder3 {
    @Id
    private UUID pfOrderId; // 訂單編號, 主鍵, UUID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pfGameId")
    private PfGame pfGame; // 伴遊遊戲編號

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memId")
    private Member member; // 會員編號(下訂者)

    private Integer quantity; // 每單數量
    private Integer totalAmount; // 總金額

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date added; // 創建時間

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime; // 付款時間

    private String transactionID; // 交易ID
    private Integer paymentStatus; // 付款狀態 INT 1:交易中 2交易完成 3交易失敗(或取消)

    @PrePersist
    public void prePersist() {
        if (pfOrderId == null) {
            pfOrderId = UUID.randomUUID();
        }
        if (added == null) {
            added = new Date();
        }
    }
}
