package com.playcentric.model.prop.buyOrder;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropBuyOrderRepository extends JpaRepository<PropBuyOrder, PropBuyOrderId> {

    @Query("SELECT o FROM PropBuyOrder o WHERE o.propSellOrder.memberPropInventory.props.game.gameId = :gameId")
    List<PropBuyOrder> findAllByGameId(@Param("gameId") int gameId);

//    @Query("SELECT o FROM PropBuyOrder o WHERE o.propSellOrder.orderId = :orderId")
//    List<PropBuyOrder> findAllByOrderId(@Param("orderId") int orderId);

}
