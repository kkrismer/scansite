package edu.mit.scansite.client.ui.widgets.features;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

import edu.mit.scansite.shared.ImagePaths;

/**
 * @author Tobieh
 */
public class WaitPopup extends PopupPanel {
	
	public WaitPopup() {
		setWidget(new Image(ImagePaths.getStaticImagePath(ImagePaths.WAIT_HUGE)));
		setAnimationEnabled(true);
		setAutoHideEnabled(false);
		setGlassEnabled(true);
		setStyleName("waitPopup");
	}
}
