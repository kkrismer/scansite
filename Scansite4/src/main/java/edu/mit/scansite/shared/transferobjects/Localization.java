package edu.mit.scansite.shared.transferobjects;

import java.util.List;

/**
 * @author Konstantin Krismer
 */
public class Localization extends LightWeightLocalization {
	private List<GOTermEvidence> goTerms;

	public Localization() {

	}

	public Localization(LocalizationType type, int score,
			List<GOTermEvidence> goTerms) {
		super(type, score);
		this.goTerms = goTerms;
	}

	public Localization(int id, LocalizationType type, int score,
			List<GOTermEvidence> goTerms) {
		super(id, type, score);
		this.goTerms = goTerms;
	}

	public List<GOTermEvidence> getGoTerms() {
		return goTerms;
	}

	public void setGoTerms(List<GOTermEvidence> goTerms) {
		this.goTerms = goTerms;
	}
}
