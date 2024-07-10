package com.playcentric.model.game.primary;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gameTypeLib")
public class GameTypeLib {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gameTypeId;
	
	private String gameTypeName;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "gameTypeLibs")
	private List<Game> games;
	
}
