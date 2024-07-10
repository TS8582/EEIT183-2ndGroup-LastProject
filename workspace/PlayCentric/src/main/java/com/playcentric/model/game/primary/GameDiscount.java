package com.playcentric.model.game.primary;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gameDiscount")
@IdClass(GameDiscountId.class)  // 指定複合主鍵類
public class GameDiscount implements Serializable {

    @Id
    private Integer gameDiscountId;

    @Id
    private Integer gameId;

    private BigDecimal discountRate;
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "gameId", referencedColumnName = "gameId", insertable = false, updatable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "gameDiscountId", referencedColumnName = "gameDiscountId", insertable = false, updatable = false)
    private GameDiscountSet gameDiscountSet;
}
