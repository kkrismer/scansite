package edu.mit.scansite.client.ui.widgets;

import edu.mit.scansite.shared.transferobjects.states.State;

/**
 * @author Konstantin Krismer
 */
public interface Stateful<T extends State> {

	/**
	 * @return A map representing the state of the widget.
	 */
	public abstract T getState();

	/**
	 * @param state
	 *            A map representing the state of the widget.
	 */
	public abstract void setState(final T state);

}
