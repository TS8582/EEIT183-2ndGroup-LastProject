package com.playcentric.model.forum;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class TalkLikeId implements Serializable {

	private static final long serialVersionUID = 1L;

	private int talkId;

	private int memId;

	@Override
	public int hashCode() {
		return Objects.hash(talkId, memId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TalkLikeId other = (TalkLikeId) obj;
		return Objects.equals(talkId, other.talkId) && Objects.equals(memId, other.memId);
	}
}
