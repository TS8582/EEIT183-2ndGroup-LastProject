package com.playcentric.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.playcentric.model.forum.Texts;
import com.playcentric.model.game.primary.Game;
import com.playcentric.model.prop.Props;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "imageLib")
public class ImageLib {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageId;
	
	@Lob
	private byte[] imageFile;
	
	@ManyToMany(mappedBy = "imageLibs")
	private List<Game> games;
	
	@OneToOne(mappedBy = "imageLib")
	private Props props;
	
	// 討論區使用
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "textsId")
    private Texts texts; 
}
