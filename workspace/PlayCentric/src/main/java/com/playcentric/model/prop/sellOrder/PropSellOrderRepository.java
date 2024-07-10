package com.playcentric.model.prop.sellOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface PropSellOrderRepository extends JpaRepository<PropSellOrder, Integer> {
    
	@Query("SELECT o FROM PropSellOrder o WHERE o.memberPropInventory.props.game.gameId = :gameId")
    List<PropSellOrder> findAllByGameId(@Param("gameId") int gameId);
	

}

