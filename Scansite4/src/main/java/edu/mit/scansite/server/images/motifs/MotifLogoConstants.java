package edu.mit.scansite.server.images.motifs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import edu.mit.scansite.server.images.Colors;

/**
 * @author Tobieh
 */
public class MotifLogoConstants {
	public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;

	public static final int IMAGE_HEIGHT = 750;
	public static final int IMAGE_WIDTH = 1000;

	public static final int MARGIN_N = 20;

	public static final int BASELINE_OFFSET_W = 50;
	public static final int BASELINE_OFFSET_E = 50;
	public static final int BASELINE_OFFSET_S = 70;

	public static final int TERMINAL_WIDTH = 10;

	public static final int FULL_LOGO_WIDTH = IMAGE_WIDTH - 2 * TERMINAL_WIDTH
			- BASELINE_OFFSET_E - BASELINE_OFFSET_W;
	public static final int FULL_LOGO_HEIGHT = IMAGE_HEIGHT - BASELINE_OFFSET_S
			- MARGIN_N;

	public static final int SPACE_WIDTH = 3;

	// all coordinates from here are all relative to the baseline
	public static final int TITLE_OFFSET_Y = -50;
	public static final int TITLE_OFFSET_X = 0;

	public static final int N_TERM_OFFSET_X = -7;
	public static final int C_TERM_OFFSET_X = FULL_LOGO_WIDTH + 7;
	public static final int C_TERM_OFFSET_Y = 3;
	public static final int N_TERM_OFFSET_Y = 3;
	public static final int INDEX_OFFSET = -5;

	public static final float DEFAULT_STROKE_WIDTH = 1;

	public static final float AXIS_STROKE_WIDTH = 1;
	public static final BasicStroke DEFAULT_STROKE = new BasicStroke(
			AXIS_STROKE_WIDTH);
	public static final BasicStroke BASELINE_STROKE = new BasicStroke(
			AXIS_STROKE_WIDTH);

	public static final Color COLOR_BACKGROUND = Colors.WHITE;
	public static final Color COLOR_DEFAULT = Colors.BLACK;

	public static final int FONT_SIZE_DEFAULT = 12;
	public static final Font FONT_DEFAULT = new Font("SansSerif", Font.PLAIN,
			FONT_SIZE_DEFAULT);
	public static final Font FONT_DEFAULT_BOLD = new Font("SansSerif",
			Font.BOLD, FONT_SIZE_DEFAULT);
	public static final Font FONT_AMINO_ACID = new Font("SansSerif", Font.BOLD,
			FONT_SIZE_DEFAULT);

	public static final String TEXT_C_TERMINUS = "C";
	public static final String TEXT_N_TERMINUS = "N";
}
