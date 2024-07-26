package com.playcentric.model.game.transaction;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameOrderDetailsId implements Serializable{
	
	private int gameOrderId;
	
	private int gameId;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
        GameOrderDetailsId that = (GameOrderDetailsId) obj;
        return Objects.equals(gameId, that.gameId) &&
               Objects.equals(gameOrderId, that.gameOrderId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(gameId,gameOrderId);
	}
}
