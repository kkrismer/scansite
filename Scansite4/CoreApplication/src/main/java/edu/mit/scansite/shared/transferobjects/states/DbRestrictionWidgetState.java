package edu.mit.scansite.shared.transferobjects.states;

import edu.mit.scansite.shared.transferobjects.RestrictionProperties;

/**
 * @author Konstantin Krismer
 */
public class DbRestrictionWidgetState extends State {
	private RestrictionProperties restrictionProperties;

	public DbRestrictionWidgetState() {

	}

	public DbRestrictionWidgetState(RestrictionProperties restrictionProperties) {
		this.restrictionProperties = restrictionProperties;
	}

	public RestrictionProperties getRestrictionProperties() {
		return restrictionProperties;
	}

	public void setRestrictionProperties(
			RestrictionProperties restrictionProperties) {
		this.restrictionProperties = restrictionProperties;
	}
}
