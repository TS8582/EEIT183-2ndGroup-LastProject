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
public class TextsReportId implements Serializable {

	private static final long serialVersionUID = 1L;

	private int textsId;

	private int memId;

	private int reportId;

	@Override
	public int hashCode() {
		return Objects.hash(textsId, memId, reportId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextsReportId other = (TextsReportId) obj;
		return Objects.equals(textsId, other.textsId) && Objects.equals(memId, other.memId)
				&& Objects.equals(reportId, other.reportId);
	}

}
