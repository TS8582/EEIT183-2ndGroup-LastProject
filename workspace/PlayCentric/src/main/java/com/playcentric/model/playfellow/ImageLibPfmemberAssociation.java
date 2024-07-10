package com.playcentric.model.playfellow;

import com.playcentric.model.ImageLib;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ImageLibPfmemberAssociation")
public class ImageLibPfmemberAssociation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "playFellowId")
	private PlayFellowMember playFellowMember;

	@ManyToOne
	@JoinColumn(name = "imageId")
	private ImageLib imageLib;

	public ImageLibPfmemberAssociation(PlayFellowMember playFellowMember, ImageLib imageLib) {
		this.playFellowMember = playFellowMember;
		this.imageLib = imageLib;
	}
}
