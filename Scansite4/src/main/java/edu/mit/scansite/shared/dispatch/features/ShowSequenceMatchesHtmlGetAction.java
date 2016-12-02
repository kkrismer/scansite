package edu.mit.scansite.shared.dispatch.features;

import edu.mit.scansite.shared.transferobjects.Protein;
import net.customware.gwt.dispatch.shared.Action;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ShowSequenceMatchesHtmlGetAction implements
		Action<ShowSequenceMatchesHtmlGetResult> {
	private String sequenceRegex;
	private Protein protein;
	private int topPosition;
	private int leftPosition;

	public ShowSequenceMatchesHtmlGetAction() {
	}

	public ShowSequenceMatchesHtmlGetAction(String sequenceRegex,
			Protein protein) {
		super();
		this.sequenceRegex = sequenceRegex;
		this.protein = protein;
	}

	public ShowSequenceMatchesHtmlGetAction(String sequenceRegex,
			Protein protein, int topPosition, int leftPosition) {
		super();
		this.sequenceRegex = sequenceRegex;
		this.protein = protein;
		this.topPosition = topPosition;
		this.leftPosition = leftPosition;
	}

	public String getSequenceRegex() {
		return sequenceRegex;
	}

	public void setSequenceRegex(String sequenceRegex) {
		this.sequenceRegex = sequenceRegex;
	}

	public Protein getProtein() {
		return protein;
	}

	public void setProtein(Protein protein) {
		this.protein = protein;
	}

	public int getTopPosition() {
		return topPosition;
	}

	public void setTopPosition(int topPosition) {
		this.topPosition = topPosition;
	}

	public int getLeftPosition() {
		return leftPosition;
	}

	public void setLeftPosition(int leftPosition) {
		this.leftPosition = leftPosition;
	}
}
