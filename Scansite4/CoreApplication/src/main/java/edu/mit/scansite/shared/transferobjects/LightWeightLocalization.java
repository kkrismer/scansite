package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class LightWeightLocalization implements IsSerializable {
	private int id = -1;
	private int score;
	private LocalizationType type;

	public LightWeightLocalization() {

	}

	public LightWeightLocalization(LocalizationType type, int score) {
		super();
		this.score = score;
		this.type = type;
	}

	public LightWeightLocalization(int id, LocalizationType type, int score) {
		super();
		this.id = id;
		this.score = score;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalizationType getType() {
		return type;
	}

	public void setType(LocalizationType type) {
		this.type = type;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Localization [type=" + type + ", score=" + score + "]";
	}
}
