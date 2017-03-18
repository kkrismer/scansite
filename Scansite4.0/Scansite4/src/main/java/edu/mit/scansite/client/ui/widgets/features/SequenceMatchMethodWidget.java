package edu.mit.scansite.client.ui.widgets.features;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.SequencePattern;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public interface SequenceMatchMethodWidget {
	public List<SequencePattern> getSequencePatterns();

	public boolean showPhosphorylatedProteinsOnly();
}
