package com.playcentric.model.prop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GameSimpleDto {
    private int gameId;
    private String gameName;
}
