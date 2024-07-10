package com.playcentric.model.prop.type;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.prop.Props;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "propType")
public class PropType {
    // PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int propTypeId;
    
    private String propType;
    
    @OneToMany(mappedBy = "propType", fetch = FetchType.LAZY)
    private Set<Props> props;

    // FK gameId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameId", referencedColumnName = "gameId", insertable = false, updatable = false)
    @JsonIgnore
    private Game game;
    
    private int gameId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
