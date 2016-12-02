package edu.mit.scansite.shared.transferobjects;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class SequencePattern implements IsSerializable {
	private List<PatternPosition> positions;
	private int startIndex = 0;

	public SequencePattern() {
		super();
	}

	public List<PatternPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<PatternPosition> positions) {
		this.positions = positions;
	}

	public void addPosition(PatternPosition position) {
		if (positions == null) {
			positions = new LinkedList<PatternPosition>();
		}
		positions.add(position);
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public String getRegEx() {
		StringBuilder regEx = new StringBuilder();
		if (positions != null) {
			for (PatternPosition position : positions) {
				regEx.append(position.getRegEx());
			}
		}
		return regEx.toString();
	}

	public String getHtmlFormattedRegEx() {
		StringBuilder regEx = new StringBuilder();
		if (positions != null) {
			for (PatternPosition position : positions) {
				regEx.append(position.getHtmlFormattedRegEx());
			}
		}
		return regEx.toString();
	}

	public String getPlainPattern() {
		StringBuilder plain = new StringBuilder();
		if (positions != null) {
			for (PatternPosition position : positions) {
				plain.append(position.getPlain());
			}
		}
		return plain.toString();
	}

	public boolean hasExpectedPhosphorylationSites() {
		if (positions != null) {
			for (PatternPosition position : positions) {
				if (position.isExpectedPhosphorylationSite()) {
					return true;
				}
			}
		}
		return false;
	}

	public static List<String> getRegExs(List<SequencePattern> sequencePatterns) {
		List<String> regExs = new LinkedList<>();
		for (SequencePattern pattern : sequencePatterns) {
			regExs.add(pattern.getRegEx());
		}
		return regExs;
	}

	@Override
	public String toString() {
		return getRegEx();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((positions == null) ? 0 : positions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequencePattern other = (SequencePattern) obj;
		if (positions == null) {
			if (other.positions != null)
				return false;
		} else if (!positions.equals(other.positions))
			return false;
		return true;
	}
}
