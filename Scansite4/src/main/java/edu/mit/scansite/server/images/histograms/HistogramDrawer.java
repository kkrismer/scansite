package edu.mit.scansite.server.images.histograms;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import edu.mit.scansite.server.images.Painter;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.HistogramDataPoint;
import edu.mit.scansite.shared.transferobjects.HistogramStringency;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * @author Tobieh
 */
public class HistogramDrawer extends Painter {
  
  private static final int MAX_TAXON_NAME_LENGTH = 25;

  private ServerHistogram histogram;
  
  private int thresholdCount = 0;
  private int infoCount = 0;
  private int addInfoCount = 0;
  
  public HistogramDrawer(ServerHistogram histogram) {
    this.histogram = histogram;
    if (!histogram.hasImage()) {
      bImg = new BufferedImage(HistogramConstants.IMAGE_WIDTH, 
          HistogramConstants.IMAGE_HEIGHT, HistogramConstants.IMAGE_TYPE);
      image = bImg.createGraphics();
      image.setBackground(HistogramConstants.COLOR_BACKGROUND);
      image.clearRect(0, 0, HistogramConstants.IMAGE_WIDTH, HistogramConstants.IMAGE_HEIGHT);
      prepare();
    } else {
      bImg = histogram.getPlot();
      image = bImg.createGraphics();
    }
  }
  
  /**
   * Prepares a histogram plot for the database. 
   * Axis, absolute frequencies, percentile, legend, basic information included.
   * @return A buffered image of the 'blank' database histogram plot.
   */
  public BufferedImage getDbHistogramPlot() {
    drawTitle(HistogramConstants.HIST_TITLE_PREFIX + histogram.getMotif().getDisplayName() + HistogramConstants.HIST_TITLE_POSTFIX);
    drawMedianAndMAD(histogram.getMedian(), histogram.getMedianAbsDev());
    drawHistogramInfo("Motif: ", histogram.getMotif().getShortName());
    drawHistogramInfo("Datasource: ", histogram.getDataSource().getShortName());
    if (histogram.getTaxon().getName().length() > MAX_TAXON_NAME_LENGTH) {
      drawHistogramInfo("Taxon Class: ", "");
      drawHistogramInfo("", histogram.getTaxon().getName());
    } else {
      drawHistogramInfo("Taxon Class: ", histogram.getTaxon().getName());
    }
    drawHistogramInfo("Number of Proteins: ", String.valueOf(histogram.getProteinsScored()));
    drawHistogramInfo("Number of Scored Sites: ", String.valueOf(histogram.getSitesScored()));
    drawData();
    return bImg;
  }
  
  /**
   * Prepares a histogram plot for a specific scoring site. 
   * @param site A site.
   * @return A buffered image of the reference histogram plot.
   */
  public BufferedImage getReferenceHistogramPlot(ScanResultSite site) {
    if (site.getScore() <= HistogramConstants.SCORE_MAX) {
      drawThreshold(site.getScore(), null, true);
    }
    drawAdditionalInfo("Protein: ", site.getProtein().getIdentifier());
    drawAdditionalInfo("Site: ", site.getSite());
    drawAdditionalInfo("Sequence: ", site.getSiteSequence());
    double zScore = calculateZScore(site.getScore());
    drawAdditionalInfo("Robust Z-Score Estimate: ", String.format("%.4f", Math.round(zScore * 10000.0) / 10000.0)); 
    drawScoreComment("Your sequence scores in the best " + String.format("%.3f", Math.round(site.getPercentile() * 100000.0) / 1000.0)
        + "% \nof sites when compared to all records used \nin this search.");
    drawDisclaimer();
    return bImg;
  }

  private double calculateZScore(double score) {
    return histogram.getMedianAbsDev() != 0 ? (score - histogram.getMedian()) / (histogram.getMedianAbsDev() * 1.4826) : 0;
  }

  public BufferedImage getReferenceHistogramPlot(DatabaseSearchScanResultSite site) {
    if (site.getScore() <= HistogramConstants.SCORE_MAX) {
      drawThreshold(site.getScore(), null, true);
    }
    drawAdditionalInfo("Protein: ", site.getProtein().getIdentifier());
    if (site.isMultiple()) {
      for (int i = 0; i < site.getSites().size(); ++i) {
        drawAdditionalInfo("Sequence " + (i+1) +": ", site.getSites().get(i).getSiteSequence() + " (" + site.getSites().get(i).getSite() + ")");
      }
    } else {
      drawAdditionalInfo("Sequence: ", site.getSite().getSiteSequence() + " (" + site.getSite().getSite() + ")");
    }
    return bImg;
  }
  
  /**
   * Prepares a histogram plot for applying stringency values.
   * @return A buffered image of the stringency-editable histogram plot.
   */
  public BufferedImage getDbEditHistogramPlot() {
    if (histogram.getThresholdHigh() != null) {
      drawThreshold(histogram.getThresholdHigh(), HistogramStringency.STRINGENCY_HIGH.getName(), true);
    } else {
      double thresh =  histogram.getScore(HistogramStringency.STRINGENCY_HIGH.getPercentileValue());
      histogram.setThresholdHigh(thresh);
      drawThreshold(thresh, HistogramStringency.STRINGENCY_HIGH.getName(), true);
    }
    if (histogram.getThresholdMedium() != null) {
      drawThreshold(histogram.getThresholdMedium(), HistogramStringency.STRINGENCY_MEDIUM.getName(), true);
    } else {
      double thresh =  histogram.getScore(HistogramStringency.STRINGENCY_MEDIUM.getPercentileValue());
      histogram.setThresholdMedium(thresh);
      drawThreshold(thresh, HistogramStringency.STRINGENCY_MEDIUM.getName(), true);
    }
    if (histogram.getThresholdLow() != null) {
      drawThreshold(histogram.getThresholdLow(), HistogramStringency.STRINGENCY_LOW.getName(), true);
    } else {
      double thresh =  histogram.getScore(HistogramStringency.STRINGENCY_LOW.getPercentileValue());
      histogram.setThresholdLow(thresh);
      drawThreshold(thresh, HistogramStringency.STRINGENCY_LOW.getName(), true);
    }
    return bImg;
  }
  
  /**
   * Prints the histogram's axis, and axis labels.
   */
  private void prepare() {
    printAxis();
    drawLegend();
    drawPercentileAxisAnnotation();
  }
  
  /**
   * Prints the histogram's axis and some labels and markers.
   */
  private void printAxis() {
    setStroke(HistogramConstants.AXIS_STROKE);
    setColor(null);
    // left y-axis
    Line2D.Double line = getLine(getHistPoint(0, 0), getHistPoint(0, HistogramConstants.HIST_HEIGHT));
    image.draw(line);
    
    // right y-axis
    line = getLine(getHistPoint(HistogramConstants.HIST_WIDTH, 0), getHistPoint(HistogramConstants.HIST_WIDTH, HistogramConstants.HIST_HEIGHT));
    image.draw(line);
    
    // x-axis
    line = getLine(getHistPoint(0, 0), getHistPoint(HistogramConstants.HIST_WIDTH, 0));
    image.draw(line);
    
    // draw markers
    for (double i = 0.25; i <= 1; i += 0.25) { // markers at 25%, 50%, 75%, and 100%
      line = getLine(getHistPoint(0, HistogramConstants.HIST_HEIGHT * i), getHistPoint( -HistogramConstants.AXIS_MARKER_LENGTH, HistogramConstants.HIST_HEIGHT * i));// top left y-axis
      image.draw(line);
      line = getLine(getHistPoint(HistogramConstants.HIST_WIDTH, HistogramConstants.HIST_HEIGHT * i), 
          getHistPoint(HistogramConstants.HIST_WIDTH + HistogramConstants.AXIS_MARKER_LENGTH, HistogramConstants.HIST_HEIGHT * i));
      image.draw(line);
    }
    
    DecimalFormat oneDec = new DecimalFormat("#,##0.0");
    Font f = HistogramConstants.FONT_AXIS;
    double fHeight = getFontHeight(f);
    drawString(HistogramConstants.AXIS_TEXT_SCORE, 0, -HistogramConstants.AXIS_MARKER_LENGTH  - 3*fHeight, f, false);
    for (double xCoord = 0, i = 0; xCoord <= HistogramConstants.HIST_WIDTH && i < HistogramConstants.SCORE_MAX_DRAW; 
         i += HistogramConstants.SCORE_STEP_SIZE, xCoord += HistogramConstants.SCORE_STEP_SIZE_PX) {
      line = getLine(getHistPoint(xCoord, 0), getHistPoint(xCoord, -HistogramConstants.AXIS_MARKER_LENGTH));
      image.draw(line);
      String s = oneDec.format(i);
      drawString(s, xCoord, -HistogramConstants.AXIS_MARKER_LENGTH - 1, f, true);
    }
    
    setStroke(null);
  }

  /**
   * Sets the percentile axis annotation.
   */
  private void drawPercentileAxisAnnotation() {
    setColor(HistogramConstants.COLOR_PERCENTILE);
    String s = HistogramConstants.AXIS_TEXT_PERCENTILE;
    Font f = HistogramConstants.FONT_AXIS;
    
    Rectangle2D rect = getStringBoundsRect(s, f);
    double xCenter = HistogramConstants.HIST_WIDTH + rect.getWidth() / 2;
    double YCenter = HistogramConstants.HIST_HEIGHT + rect.getHeight() - HistogramConstants.OFFSET_SMALL;
    drawString(s, xCenter, YCenter, f, true);
    
    setColor(HistogramConstants.COLOR_DEFAULT);
  }
  
  /**
   * Prints the legend.
   */
  private void drawLegend() {
    setColor(HistogramConstants.COLOR_ABSFREQ);
    double x = HistogramConstants.LEGEND_COORD_X;
    double y = HistogramConstants.LEGEND_COORD_Y;
    drawLegendEntry(HistogramConstants.LEGEND_TEXT_ABSFREQ, x, y);
    
    setColor(HistogramConstants.COLOR_PERCENTILE);
    drawLegendEntry(HistogramConstants.LEGEND_TEXT_PERCENTILE, x, y - getFontHeight(HistogramConstants.FONT_LEGEND));
    
    setColor(HistogramConstants.COLOR_DEFAULT);
  }
  
  /**
   * Prints one legend entry at the given coordinates.
   * Color and stroke have to be set before!
   * @param s The legend text.
   * @param x The x coordinate of the entry.
   * @param y The y coordinate of the entry.
   */
  private void drawLegendEntry(String s, double x, double y) {
    setStroke(new BasicStroke(HistogramConstants.LEGEND_STROKE_WIDTH));
    Font font = HistogramConstants.FONT_LEGEND;
    Rectangle2D rect = getStringBoundsRect(s, font);
    double lineXFrom = x - HistogramConstants.OFFSET_SMALL - HistogramConstants.LEGEND_LINE_LENGTH;
    double lineY = y + rect.getHeight() / 4;// + getFontHeight(font);
    image.draw(getLine(getHistPoint(lineXFrom, lineY), getHistPoint(lineXFrom + HistogramConstants.LEGEND_LINE_LENGTH, lineY)));
    drawString(s, x, y, font, false);
    setStroke(null);
  }

  /**
   * Sets the diagrams title.
   * @param title The title.
   */
  private void drawTitle(String title) {
    Font f = HistogramConstants.FONT_TITLE;
    Rectangle2D rect = getStringBoundsRect(title, f);
    // adapt font size if necessary:
    while (rect.getWidth() > HistogramConstants.IMAGE_WIDTH && f.getSize() > HistogramConstants.FONT_SIZE_TITLE_MIN) {
      f = new Font(f.getFontName(), f.getStyle(), f.getSize() - 1);
      rect = getStringBoundsRect(title, f);
    }
    double strXCenter = HistogramConstants.HIST_WIDTH / 2;
    double strYCenter = HistogramConstants.HIST_HEIGHT + HistogramConstants.HIST_N_OFFSET -  HistogramConstants.N_OFFSET_DEFAULT;
    double fontHeight = getFontHeight(f);
    drawString(title, strXCenter, strYCenter, f, true);
    double yCoord =  strYCenter - fontHeight - rect.getHeight() / 2 - HistogramConstants.OFFSET_SMALL;
    image.draw(getLine(
        getHistPoint(strXCenter - (rect.getWidth() / 2), yCoord), 
        getHistPoint(strXCenter + (rect.getWidth() / 2), yCoord)));
  }
  
  /**
   * Sets the median and the MAD.
   * @param scoreMedian The median score.
   * @param medianAbsDev The median absolute deviation.
   */
  private void drawMedianAndMAD(double scoreMedian, double medianAbsDev) {
    setStroke(HistogramConstants.STROKE_MEDIAN);
    setColor(HistogramConstants.COLOR_DEFAULT);
    DecimalFormat dec = new DecimalFormat("#,##0.000");
    double x = score2Px(scoreMedian);
    double y = HistogramConstants.MEDIAN_LINE_Y_COORD;
    image.draw(getLine(getHistPoint(x, HistogramConstants.HIST_HEIGHT), getHistPoint(x, y))); //vertical line
    double madW = score2Px(medianAbsDev);
    double madY = HistogramConstants.MAD_MARKER_Y_COORD;
    setStroke(HistogramConstants.AXIS_STROKE);
    int madCount = HistogramConstants.MAD_COUNTS;
    image.draw(getLine(getHistPoint(x - madW*madCount, madY), getHistPoint(x + madW*madCount, madY))); // horizontal MAD-line
    for (int i = 0; i <= madCount; ++i) {
      double axisMarkerLength = (i == 0) ? 0 : (HistogramConstants.AXIS_MARKER_LENGTH) / 2.;
      image.draw(getLine(getHistPoint(x - madW*i, madY + axisMarkerLength), getHistPoint(x - madW*i, madY - axisMarkerLength)));
      image.draw(getLine(getHistPoint(x + madW*i, madY + axisMarkerLength), getHistPoint(x + madW*i, madY - axisMarkerLength)));
    }
    
    drawString(HistogramConstants.MEDIAN_TEXT_MEDIAN + dec.format(scoreMedian) + "\n" + HistogramConstants.MEDIAN_TEXT_MAD + dec.format(medianAbsDev),
        x, y + HistogramConstants.OFFSET_SMALL * 2, HistogramConstants.FONT_MEDIAN, true);
  }
  
  /**
   * Adds a given threshold.
   * @param thresh A threshold.
   * @param text An optional text that is added in front of the threshold, separated by a space-character.
   * @param thresholdIsScore If TRUE, thresh is given as a score, otherwise a percentile is expected.
   */
  private void drawThreshold(double thresh, String text, boolean thresholdIsScore) {
    setStroke(HistogramConstants.STROKE_THRESHOLD);
    setColor(HistogramConstants.COLOR_OTHER);
    DecimalFormat dec = new DecimalFormat("#,##0.0000");
    double fontHeight = getFontHeight(HistogramConstants.FONT_THRESHOLD);
    double val = thresh;
    if (!thresholdIsScore) {
      val = histogram.getScore(thresh);
    }
    double x = score2Px(val);
    double y = HistogramConstants.THRESHOLD_LINE_Y_COORD - thresholdCount * fontHeight * 4;
    image.draw(getLine(getHistPoint(x, 0), getHistPoint(x, y)));
    drawString(((text == null) ? "" : text + " " )+ dec.format(val), x, y + fontHeight * 2, HistogramConstants.FONT_THRESHOLD, true);
    ++thresholdCount;
    setStroke(null);
    setColor(null);
  }

  /**
   * Sets the absolute frequency axis annotation.
   * @param freq The absolute frequency.
   */
  private void drawAbsoluteFrequency(int freq) {
    setColor(HistogramConstants.COLOR_ABSFREQ);
    String s = HistogramConstants.AXIS_TEXT_ABSFREQ + String.valueOf(freq);
    Font f = HistogramConstants.FONT_AXIS;
    
    Rectangle2D rect = getStringBoundsRect(s, f);
    double xCenter = 0 - rect.getWidth() / 2;
    double YCenter = HistogramConstants.HIST_HEIGHT + rect.getHeight() - HistogramConstants.OFFSET_SMALL;
    drawString(s, xCenter, YCenter, f, true);
    
    setColor(HistogramConstants.COLOR_DEFAULT);
  }

  /**
   * @return The histogram as BufferedImage.
   */
  public BufferedImage getBufferedImage() {
    drawData();
    return bImg;
  }
  
  /**
   * Adds a line of histogram information to the histogram.
   * The title is printed in a different color than the info.
   * @param title The info's title (eg. "Motif: ").
   * @param info The information.
   */
  public void drawHistogramInfo(String title, String info) {
    title = (title == null) ? "" : title;
    info = (info == null) ? "" : info;
    setColor(null);
    Font f = HistogramConstants.FONT_INFO;
    double x = HistogramConstants.INFO_X_COORD;
    double y = HistogramConstants.INFO_Y_COORD - infoCount * getFontHeight(f) * HistogramConstants.INFO_SPACER_MULTIPLIER;
    drawString(title, x, y, f, false);
    
    setColor(HistogramConstants.COLOR_INFO);
    drawString(info, x + getStringBoundsRect(title, f).getWidth(), y, f, false);
    ++infoCount;
  }
  
  /**
   * Adds a line of additional histogram information to the histogram.
   * @param info The information.
   */
  private void drawAdditionalInfo(String title, String info) {
    title = (title == null) ? "" : title;
    info = (info == null) ? "" : info;  
    setColor(null);
    Font f = HistogramConstants.FONT_INFO;
    double x = HistogramConstants.ADDITIONAL_INFO_X_COORD;
    double y = HistogramConstants.ADDITIONAL_INFO_Y_COORD - addInfoCount * getFontHeight(f) * HistogramConstants.INFO_SPACER_MULTIPLIER;
    drawString(title, x, y, f, false);
    
    setColor(HistogramConstants.COLOR_INFO);
    drawString(info, x + getStringBoundsRect(title, f).getWidth(), y, f, false);
    ++addInfoCount ;
  }

  /**
   * Draws a datapoint line to the histogram.
   * @param p1 The start datapoint.
   * @param p2 The end datapoint.
   * @param absFreqMax The maximum of absolute frequencies.
   */
  private void drawDataLineAbsFreq(HistogramDataPoint p1, HistogramDataPoint p2, double absFreqMax) {
    image.draw(getLine(getHistPoint(score2Px(p1.getScore()), absFreq2Px(p1.getAbsFreq(), absFreqMax)),
                       getHistPoint(score2Px(p2.getScore()), absFreq2Px(p2.getAbsFreq(), absFreqMax))));
  }
  
  /**
   * Draws a datapoint line to the histogram.
   * @param p1 The start datapoint.
   * @param p2 The end datapoint.
   */
  private void drawDataLinePercentile(HistogramDataPoint p1, HistogramDataPoint p2) {
    image.draw(getLine(getHistPoint(score2Px(p1.getScore()), percentile2Px(p1.getAbsFreq())),
                       getHistPoint(score2Px(p2.getScore()), percentile2Px(p2.getAbsFreq()))));
  }
  
  /**
   * Draws the datapoints defined in absFreqs.
   */
  private void drawData() {
    double maxAbsFreq = 0D;
    double freqSum = histogram.getSitesScored();
    ArrayList<HistogramDataPoint> binPoints = histogram.getDataPoints(); 
    for (HistogramDataPoint p : binPoints) {
      double currentAF = p.getAbsFreq();
      if (currentAF > maxAbsFreq) {
        maxAbsFreq = currentAF;
      }
    }
    
    drawAbsoluteFrequency((int) maxAbsFreq);
    
    // print lines
    Collections.sort(binPoints);
    setStroke(HistogramConstants.STROKE_DATA);
    HistogramDataPoint p1 = new HistogramDataPoint(0, 0);
    HistogramDataPoint p2 = null;
    double currentSum1 = p1.getAbsFreq() / freqSum;
    double currentSum2 = 0;
    double sum = p1.getAbsFreq();
    
    boolean isDone = false;
    for (int i = 0; i <= binPoints.size() && !isDone; ++i) {
      setColor(HistogramConstants.COLOR_ABSFREQ);
      if (i == binPoints.size()) {
        p2 = new HistogramDataPoint(p1.getScore(), 0);
      } else {
        if (i < binPoints.size() - 1 && binPoints.get(i + 1).getScore() > ScansiteConstants.HIST_SCORE_MAX) {
          isDone = true;
        }
        p2 = binPoints.get(i);
      }
      drawDataLineAbsFreq(p1, p2, maxAbsFreq);
      
      setColor(HistogramConstants.COLOR_PERCENTILE);
      sum += p2.getAbsFreq();
      currentSum2 = sum / freqSum;
      drawDataLinePercentile(new HistogramDataPoint(p1.getScore(), currentSum1), new HistogramDataPoint(p2.getScore(), currentSum2));
      p1 = p2;
      currentSum1 = currentSum2;
    }

    setStroke(null);
    setColor(null);
  }

  
  /**
   * Sets the disclaimer text at the bottom of the image.
   */
  private void drawDisclaimer() {
    setColor(null);
    drawString(HistogramConstants.DISCLAIMER, HistogramConstants.DISCLAIMER_X_COORD, HistogramConstants.DISCLAIMER_Y_COORD,
        HistogramConstants.FONT_DISCLAIMER, false);
  }

  /**
   * Sets the score comment given in s.
   * @param s A comment about how good the score is.
   */
  private void drawScoreComment(String s) {
    setColor(HistogramConstants.COLOR_OTHER);
    drawString(s, HistogramConstants.SCORE_COMMENT_X_COORD, HistogramConstants.SCORE_COMMENT_Y_COORD, HistogramConstants.FONT_SCORE_COMMENT, false);
  }

  /**
   * @param absFreq Absolute Frequency.
   * @param maxAbsFreq Maximum absolute frequency.
   * @return absFreq in pixels.
   */
  private double absFreq2Px(double absFreq, double maxAbsFreq) {
    return HistogramConstants.HIST_HEIGHT * (absFreq / maxAbsFreq);
  }
  
  /**
   * @param percentile Percentile (0 <= percentile <= 1).
   * @return percentile in pixels.
   */
  private double percentile2Px(double percentile) {
    return HistogramConstants.HIST_HEIGHT * percentile;
  }
  
  /**
   * @param histX X-Coordinate in histogram.
   * @param histY Y-Coordinate in histogram.
   * @return The coordinates of the given point in the image.
   */
  private Point2D.Double getHistPoint(double histX, double histY) {
    return getPoint(getHistX(histX), getHistY(histY));
  }
  
  /**
   * @param x The x coordinate in the histogram.
   * @return The x coordinate in the image.
   */
  private double getHistX(double x) {
    return x + HistogramConstants.HIST_W_OFFSET;
  }
  
  /**
   * @param y The y coordinate in the histogram.
   * @return The y coordinate in the image.
   */
  private double getHistY(double y) {
    return HistogramConstants.HIST_N_OFFSET + (HistogramConstants.HIST_HEIGHT - y);
  }

  @Override
  protected int getX(double x) {
    return (int) getHistX(x);
  }

  @Override
  protected int getY(double y) {
    return (int) getHistY(y);
  }
  
  /**
   * @param score The score.
   * @return The score in px on the x-axis.
   */
  private double score2Px(double score) {
    return score * (HistogramConstants.HIST_WIDTH / HistogramConstants.SCORE_MAX);
  }
}
