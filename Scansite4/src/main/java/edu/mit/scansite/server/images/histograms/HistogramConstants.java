package edu.mit.scansite.server.images.histograms;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import edu.mit.scansite.server.images.Colors;
import edu.mit.scansite.shared.ScansiteConstants;

/**
 * @author Tobieh
 */
public class HistogramConstants {
  public static final String HIST_TITLE_PREFIX = "Proteome Reference Histogram for '";
  public static final String HIST_TITLE_POSTFIX = "' Site Search";
  
  public static final int IMAGE_WIDTH = ScansiteConstants.HIST_IMAGE_WIDTH;
  public static final int IMAGE_HEIGHT = ScansiteConstants.HIST_IMAGE_HEIGHT;
  public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;
  public static final double HIST_E_OFFSET = IMAGE_WIDTH / 11;
  public static final double HIST_W_OFFSET = IMAGE_WIDTH / 11;
  public static final double HIST_N_OFFSET = IMAGE_HEIGHT / 10;
  public static final double HIST_S_OFFSET = HIST_N_OFFSET * 2;
  public static final double HIST_HEIGHT = IMAGE_HEIGHT - HIST_N_OFFSET - HIST_S_OFFSET;
  public static final double HIST_WIDTH = IMAGE_WIDTH - HIST_E_OFFSET - HIST_W_OFFSET;
  
  public static final float DEFAULT_STROKE_WIDTH = 2;
  public static final float AXIS_STROKE_WIDTH = 1;
  public static final float LEGEND_STROKE_WIDTH = DEFAULT_STROKE_WIDTH;

  public static final float DATA_STROKE_WIDTH = 1;
  public static final Stroke STROKE_DATA = new BasicStroke(HistogramConstants.DATA_STROKE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  
  public static final double AXIS_MARKER_LENGTH = 5;
  public static final String AXIS_TEXT_SCORE = "Score";
  public static final String AXIS_TEXT_ABSFREQ = "Abs Freq:\n";
  public static final String AXIS_TEXT_PERCENTILE = "Percentile:\n100%";
  public static final Stroke AXIS_STROKE = new BasicStroke(HistogramConstants.AXIS_STROKE_WIDTH);
  public static final String LEGEND_TEXT_ABSFREQ = "Absolute Frequency";
  public static final String LEGEND_TEXT_PERCENTILE = "Percentile";
  public static final double LEGEND_COORD_X = HIST_WIDTH * 0.8;
  public static final double LEGEND_COORD_Y = HIST_HEIGHT * 0.07;
  public static final double LEGEND_LINE_LENGTH = 15;
  
  public static final Color COLOR_DEFAULT = Colors.BLACK;
  public static final Color COLOR_BACKGROUND = Colors.WHITE;
  public static final Color COLOR_ABSFREQ = Colors.RED;
  public static final Color COLOR_PERCENTILE = Colors.BLUE;
  public static final Color COLOR_INFO = Colors.GREEN_DARK;
  public static final Color COLOR_OTHER = Colors.GRAY_DARK;
  
  public static final double OFFSET_SMALL = 3;
  
  public static final double N_OFFSET_DEFAULT = 5;
  
  public static final int FONT_SIZE_DEFAULT = 12;
  public static final int FONT_SIZE_AXIS = 12;
  public static final int FONT_SIZE_LEGEND = 13;
  public static final int FONT_SIZE_MEDIAN = 12;
  public static final int FONT_SIZE_THRESHOLD = 12;
  public static final int FONT_SIZE_INFO = 13;
  public static final int FONT_SIZE_DISCLAIMER = 10;
  public static final int FONT_SIZE_SCORE_COMMENT = 11;
  public static final int FONT_SIZE_TITLE = 17;
  public static final int FONT_SIZE_TITLE_MIN = 5;
  public static final Font FONT_AXIS = new Font("MonoSpaced", Font.PLAIN, FONT_SIZE_AXIS);
  public static final Font FONT_TITLE = new Font("Sans Serif", Font.BOLD, FONT_SIZE_TITLE);
  public static final Font FONT_LEGEND = new Font("MonoSpaced", Font.PLAIN, FONT_SIZE_LEGEND);
  public static final Font FONT_MEDIAN = new Font("MonoSpaced", Font.PLAIN, FONT_SIZE_MEDIAN);
  public static final Font FONT_THRESHOLD = new Font("MonoSpaced", Font.BOLD, FONT_SIZE_THRESHOLD);
  public static final Font FONT_INFO = new Font("MonoSpaced", Font.PLAIN, FONT_SIZE_INFO);
  public static final Font FONT_DISCLAIMER = new Font("SansSerif", Font.PLAIN, FONT_SIZE_DISCLAIMER);
  public static final Font FONT_SCORE_COMMENT = new Font("SansSerif", Font.BOLD, FONT_SIZE_SCORE_COMMENT);

  public static final double INFO_X_COORD = HIST_WIDTH * 0.6;
  public static final double INFO_Y_COORD = HIST_HEIGHT * 0.85;
  public static final double INFO_SPACER_MULTIPLIER = 1.2;
  
  public static final double ADDITIONAL_INFO_X_COORD = INFO_X_COORD;
  public static final double ADDITIONAL_INFO_Y_COORD = HIST_HEIGHT * 0.6;
  
  public static final double SCORE_MAX = ScansiteConstants.HIST_SCORE_MAX;
  public static final double SCORE_MAX_DRAW = 1.5;
  public static final double SCORE_STEP_SIZE = 0.1;
  public static final double SCORE_STEP_SIZE_PX = HIST_WIDTH / (SCORE_MAX / SCORE_STEP_SIZE);
  
  public static final Stroke STROKE_MEDIAN = new BasicStroke(HistogramConstants.AXIS_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
      0, new float[]{(float) (HistogramConstants.AXIS_MARKER_LENGTH), (float) (HistogramConstants.AXIS_MARKER_LENGTH)}, 0);
  public static final String MEDIAN_TEXT_MEDIAN = "Median: ";
  public static final String MEDIAN_TEXT_MAD = "Median Abs Dev: ";
  public static final double MEDIAN_LINE_Y_COORD = -HIST_S_OFFSET  / 3;
  public static final double MEDIAN_TEXT_X_OFFSET = OFFSET_SMALL;
  public static final int MAD_COUNTS = 2;
  public static final double MAD_MARKER_Y_COORD = MEDIAN_LINE_Y_COORD + AXIS_MARKER_LENGTH + OFFSET_SMALL;
  
  public static final Stroke STROKE_THRESHOLD = new BasicStroke(HistogramConstants.AXIS_STROKE_WIDTH + 1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
      0, new float[]{(float) (HistogramConstants.AXIS_MARKER_LENGTH), (float) (HistogramConstants.AXIS_MARKER_LENGTH) * 2}, 0);
  public static final double THRESHOLD_LINE_Y_COORD = HIST_HEIGHT;

  public static final double DISCLAIMER_X_COORD = OFFSET_SMALL - HIST_W_OFFSET;
  public static final double DISCLAIMER_Y_COORD = FONT_SIZE_DISCLAIMER * 2.5 - HIST_S_OFFSET;
  public static final String DISCLAIMER = "Note: Although the distribution may look similar to a normal " +
                                          "for searches with many sites, the percentile and Z-score reported here are computed " +
                                          "directly from\n the histogram, and NOT from a Z-table.";

  public static final double SCORE_COMMENT_X_COORD = INFO_X_COORD;
  public static final double SCORE_COMMENT_Y_COORD = HIST_HEIGHT * 0.4;
}
