package com.playcentric.model.forum;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "textsTag")
public class TextsTag {

	@EmbeddedId
	private TextsTagId textsTagId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("textsId")
	@JoinColumn(name = "textsId")
	private Texts texts;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("tagId")
	@JoinColumn(name = "tagId")
	private Tag tag;

}
