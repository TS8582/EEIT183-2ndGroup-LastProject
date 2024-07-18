package com.playcentric.model.prop.buyOrder2;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "propBuyOrder2")
public class PropBuyOrder2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buyOrderId")
    private Integer buyOrderId;

    @Column(name = "buyerMemId")
    private Integer buyerMemId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "orderTime")
    private LocalDateTime orderTime;

    @Column(name = "paymentId")
    private Integer paymentId;

    @Column(name = "price")
    private Integer price;

}
