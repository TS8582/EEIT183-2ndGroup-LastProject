package com.playcentric.model.forum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tag")
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tagId;

	private String tagName;

	public Tag(String tagName) {
		this.tagName = tagName;
	}

}
