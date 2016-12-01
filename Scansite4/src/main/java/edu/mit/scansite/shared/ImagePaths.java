package edu.mit.scansite.shared;

import com.google.gwt.user.client.ui.Image;

/**
 * A class for instantiating images.
 * @author tobieh
 */
public class ImagePaths {
  public static final String STATIC_IMAGE_DIRECTORY = "img/";
  
  // static images
  public static final String WAIT_SMALL = "wait_small.gif";
  public static final String WAIT_LARGE = "wait_large.gif";
  public static final String WAIT_HUGE = "wait_huge.gif";
  
  /**
   * @param imageName The name of the static image (eg one of this class's constants).
   * @return The path to the static image.
   */
  public static final String getStaticImagePath(String imageName) {
    return STATIC_IMAGE_DIRECTORY + imageName;
  }
  
  /**
   * @param imageName The name of the static image (eg one of this class's constants).
   * @return An image object of the chosen image.
   */
  public static final Image getStaticImage(String imageName) {
    return new Image(getStaticImagePath(imageName));
  }
  
}
