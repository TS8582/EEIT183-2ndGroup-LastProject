package com.playcentric.model.prop;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.ImageLib;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.prop.type.PropType;

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

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="props")
public class Props {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer propId;
    private String propName;
    private String propRarity;
    private String propDescription;
    private int propImageId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private int gameId;
    private int propTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gameId", referencedColumnName = "gameId", insertable = false, updatable = false)
    @JsonIgnore
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propTypeId", referencedColumnName = "propTypeId", insertable = false, updatable = false)
    @JsonIgnore
    private PropType propType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propImageId", referencedColumnName = "imageId", insertable = false, updatable = false)
    @JsonIgnore
    private ImageLib imageLib;
}
