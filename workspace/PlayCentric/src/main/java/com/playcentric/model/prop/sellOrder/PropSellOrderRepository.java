package com.playcentric.model.prop.sellOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface PropSellOrderRepository extends JpaRepository<PropSellOrder, Integer> {
    
	//根據gameId找所有賣單
	@Query("SELECT o FROM PropSellOrder o WHERE o.memberPropInventory.props.game.gameId = :gameId")
    List<PropSellOrder> findAllByGameId(@Param("gameId") int gameId);
	
	//根據propId及status為0(販售中)找賣單
    @Query("SELECT o FROM PropSellOrder o WHERE o.propId = :propId AND o.orderStatus = 0")
    List<PropSellOrder> findAllByPropId(@Param("propId") int propId);
    
	//根據gameId及memId及status為0(販售中)的賣單
    @Query("SELECT o FROM PropSellOrder o WHERE o.sellerMemId = :sellerMemId AND o.memberPropInventory.props.game.gameId = :gameId AND o.orderStatus = 0")
    List<PropSellOrder> findAllByGameIdAndMemIdAndOrderStatus(@Param("sellerMemId") int memId, @Param("gameId") int gameId);
}

