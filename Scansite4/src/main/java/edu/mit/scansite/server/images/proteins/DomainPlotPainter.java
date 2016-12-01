package edu.mit.scansite.server.images.proteins;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import java.util.HashMap;

import edu.mit.scansite.server.images.Colors;
import edu.mit.scansite.server.images.Painter;
import edu.mit.scansite.shared.ScanResultSiteComparator;
import edu.mit.scansite.shared.ScanResultSiteComparator.ComparableFields;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.DomainPosition;

/**
 * @author Tobieh
 */
public class DomainPlotPainter extends Painter {
  
  private final int IMAGE_WIDTH;
  private final int SEQ_WIDTH;
  
  private int maxYCoord = 0;
  
  private String subtitle;
  private String sequence;
  private ArrayList<DomainPosition> domainPositions;
  
  private int dColorIdx = 0;
  private Color[] domainColors = {
      Colors.ORANGE_DARK, Colors.PURPLE_DARK, Colors.BROWN_DARK, Colors.PURPLE,
      Colors.TAN, Colors.RED, Colors.GRAY_DARK, Colors.BLUE_GREEN, Colors.ORANGE,  
      Colors.MARPLE, Colors.BROWN, Colors.GREEN, Colors.YELLOW, Colors.GREEN_DARK, Colors.RED_DARK
      };

  /**
   * Creates and Initializes a domainplotpainter that displays the protein with domains. Highlighted amino acids can be applied using the highlight..(..) method.
   * @param sequence The protein's sequence.
   * @param list A list of domainPositions or NULL if there was an error with retrieving domains. 
   */
  public DomainPlotPainter(String sequence, ArrayList<DomainPosition> list) {
    this.sequence = sequence;
    this.domainPositions = list;
    IMAGE_WIDTH = sequence.length() + DomainPlotConstants.IMAGE_WIDTH_CONSTANT;
    SEQ_WIDTH = sequence.length();
    
    bImg = new BufferedImage(IMAGE_WIDTH, DomainPlotConstants.IMAGE_HEIGHT, DomainPlotConstants.IMAGE_TYPE);
    image = bImg.createGraphics();
    image.setBackground(DomainPlotConstants.COLOR_BACKGROUND);
    image.clearRect(0, 0, IMAGE_WIDTH, DomainPlotConstants.IMAGE_HEIGHT);
    setColor(DomainPlotConstants.COLOR_DEFAULT);
    setStroke(DomainPlotConstants.DEFAULT_STROKE);
    init();
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }
  
  private void init() {
    drawProtein();
    drawAaLine();
  }
  
  private void drawProtein() {
    setColor(Colors.BLACK);
    setStroke(DomainPlotConstants.PROTEIN_STROKE);
    int yCoord = DomainPlotConstants.Y_OFFSET_PROTEIN;
    drawSequenceLine(yCoord, false, DomainPlotConstants.MARKER_LENGTH + 2, true);
  }

  private void drawDomains() {
    boolean noDomains = false;
    String domainsText = "";
    if (domainsFound()) {
      doDrawDomains();
      domainsText = DomainPlotConstants.TEXT_DOMAINS;
    } else {
      noDomains = true;
      if (domainPositions != null) { // no domains found
        domainsText = DomainPlotConstants.TEXT_DOMAINS_NONE_FOUND;
      } else { // domain retrieval failed
        domainsText = DomainPlotConstants.TEXT_DOMAINS_ERROR;
      }
    }
    if (noDomains) {
      drawString(domainsText, 5, DomainPlotConstants.Y_OFFSET_PROTEIN - getFontHeight(DomainPlotConstants.FONT_DEFAULT) - 10,
          DomainPlotConstants.FONT_DEFAULT, false);
    } else {
      drawString(domainsText, SEQ_WIDTH + DomainPlotConstants.PROTEIN_STROKE_WIDTH + 2, DomainPlotConstants.Y_OFFSET_PROTEIN - getFontHeight(DomainPlotConstants.FONT_DEFAULT) - 10,
          DomainPlotConstants.FONT_DEFAULT, false);
    }
  }

  private void doDrawDomains() {
    int yStr = DomainPlotConstants.Y_OFFSET_PROTEIN;
    int y = getY(yStr);
    double yLine1 = yStr - getFontHeight(DomainPlotConstants.FONT_DEFAULT_BOLD) - 5;
    double yLine2 = yLine1 - getFontHeight(DomainPlotConstants.FONT_DEFAULT_BOLD) - 2;
    Collections.sort(domainPositions);
    Map<String, Color> dColors = getDomainColors();
    for (DomainPosition domain : domainPositions) {
      setColor(dColors.get(domain.getName()));
      setStroke(DomainPlotConstants.DOMAIN_STROKE);
      double xFromStr = domain.getFrom();
      double xFrom = getX(xFromStr);
      double xToStr = domain.getTo();
      double xTo = getX(xToStr);
      image.draw(getLine(getPoint(xFrom, y), getPoint(xTo, y)));
      
      setColor(DomainPlotConstants.COLOR_DEFAULT);
      setStroke(DomainPlotConstants.DEFAULT_STROKE);
      drawString(domain.getName(), xFromStr, yLine1, DomainPlotConstants.FONT_DEFAULT_BOLD, false);
      drawString("(" + String.valueOf((int)xFromStr) + "-" + String.valueOf((int)xToStr) + ")", xFromStr, yLine2, DomainPlotConstants.FONT_DOMAIN_RANGE, false);
    }
  }
  
  private void drawPlotSubtitle() {
    if (subtitle != null && !subtitle.isEmpty()) {
      drawString(subtitle, 0, 10 + getFontHeight(DomainPlotConstants.FONT_DEFAULT), 
          DomainPlotConstants.FONT_DEFAULT, false);
    }
  }

  private void drawAaLine() {
    setColor(Colors.BLACK);
    setStroke(DomainPlotConstants.DEFAULT_STROKE);
    int yCoord = DomainPlotConstants.Y_OFFSET_AALINE;
    drawSequenceLine(yCoord, true, DomainPlotConstants.MARKER_LENGTH, false);
    //draw number of amino acids
    drawString(String.valueOf(sequence.length()) + " " + DomainPlotConstants.TEXT_AAs, SEQ_WIDTH + DomainPlotConstants.DEFAULT_STROKE_WIDTH + 2, yCoord - 1, 
        DomainPlotConstants.FONT_DEFAULT, false);
  }

  private void drawSequenceLine(int yCoord, boolean withNumbers, int markerLength, boolean isProtein) {
    //draw line
    int yCoordPlot = getY(yCoord);
    if (isProtein) {
      int halfThickness = DomainPlotConstants.PROT_THICKNESS;
      image.draw(getLine(getPoint(getX(0), yCoordPlot+halfThickness), getPoint(getX(SEQ_WIDTH), yCoordPlot+halfThickness)));
      image.draw(getLine(getPoint(getX(0), yCoordPlot-halfThickness), getPoint(getX(SEQ_WIDTH), yCoordPlot-halfThickness)));
    } else {
      image.draw(getLine(getPoint(getX(0), yCoordPlot), getPoint(getX(SEQ_WIDTH), yCoordPlot)));
    }
    //draw markers
    image.draw(getLine(getPoint(getX(0), yCoordPlot - markerLength - 3), getPoint(getX(0), yCoordPlot + markerLength + 3)));
    image.draw(getLine(getPoint(getX(SEQ_WIDTH), yCoordPlot - markerLength - 3), getPoint(getX(SEQ_WIDTH), yCoordPlot + markerLength + 3)));
    setStroke(DomainPlotConstants.DEFAULT_STROKE);
    for (int i = DomainPlotConstants.AALINE_STEPSIZE; i < SEQ_WIDTH; i += DomainPlotConstants.AALINE_STEPSIZE) {
      image.draw(getLine(getPoint(getX(i - 1), yCoordPlot - markerLength), getPoint(getX(i - 1), yCoordPlot + markerLength)));
      if (withNumbers) {
        drawString(String.valueOf(i), i - 1, yCoord - markerLength, DomainPlotConstants.FONT_DEFAULT, true);
      }
    }
  }

  protected int getX(double x) {
    return (int) x + DomainPlotConstants.Y_OFFSET_W;
  }
  
  protected int getY(double y) {
    if (y > maxYCoord) {
      maxYCoord = (int) y;
    }
    return (int) (DomainPlotConstants.IMAGE_HEIGHT - y); 
  }
  
  private boolean domainsFound() {
    return domainPositions!=null && !domainPositions.isEmpty();
  }

  private Map<String, Color> getDomainColors() {
    ScanResultSiteComparator comp = new ScanResultSiteComparator();
    comp.setCompareField(ComparableFields.GROUP);
    Map<String, Color> dColors = new HashMap<String, Color>(); 
    for (DomainPosition d : domainPositions) {
      dColors.put(d.getName(), nextDomainColor());
    }
    for (DomainPosition d : domainPositions) {
      Color color = dColors.get(d.getName());
      d.setColorR(color.getRed());
      d.setColorG(color.getGreen());
      d.setColorB(color.getBlue());
    }
    return dColors;
  }

  private Color nextDomainColor() {
    Color c = domainColors[dColorIdx];
    dColorIdx = (dColorIdx + 1) % domainColors.length;
    return c;
  }

  /**
   * @return The histogram as BufferedImage.
   */
  public BufferedImage getBufferedImage() {
    drawPlotSubtitle();
    drawDomains();
    if (maxYCoord < DomainPlotConstants.IMAGE_HEIGHT) {
      cropImage(IMAGE_WIDTH, maxYCoord + DomainPlotConstants.Y_OFFSET_N);
    }
    return bImg;
  }

  public void highlightPositions(AminoAcid centerAminoAcid, AminoAcid relativeAminoAcid, int position, String proteinName) {
    subtitle = centerAminoAcid.getFullName() + "s having " + relativeAminoAcid.getFullName() + "s in " 
               + (position > 0 ? "+" : "") + String.valueOf(position) + " residue" + (position > 1 ? "s" : "") +" relative distance\n";  
    subtitle += "Protein: " + proteinName; 
    
    char[] seq = sequence.toCharArray();
    ArrayList<Integer> highlightCenters = new ArrayList<Integer>();
    for (int i = 0; i < seq.length; ++i) {
      int relIdxCenter = i + position;
      if (relIdxCenter >= 0 && relIdxCenter < seq.length && seq[i] == centerAminoAcid.getOneLetterCode() && seq[relIdxCenter] == relativeAminoAcid.getOneLetterCode()) {
        highlightCenters.add(i);
      }
    }
    final int fontHeight = getFontHeight(DomainPlotConstants.FONT_DEFAULT);
    final int fontWidth = getFontHeight(DomainPlotConstants.FONT_DEFAULT);
    final String txt = String.valueOf(centerAminoAcid.getOneLetterCode()); 
    final int minDist = getFontWidth(DomainPlotConstants.FONT_DEFAULT) * (txt.length() + 1);
    final int maxLevel = 10;
    int level = 0;
    int lastIdx = 0;
    for (int i = highlightCenters.size() - 1; i >= 0; --i) {
      int idx = highlightCenters.get(i);
      if (level == maxLevel || Math.abs(lastIdx - idx) > minDist) {
        level = 0;
      }
      markSequenceIndex(txt, idx, level++, fontHeight, fontWidth);
      lastIdx = idx;
    }
  }

  private void markSequenceIndex(String annotation, int seqIndex, int level, int fontHeight, int fontWidth) {
    annotation = annotation == null ? "" : annotation;
    int x = getX(seqIndex);
    int yOffset = DomainPlotConstants.Y_OFFSET_PROTEIN + DomainPlotConstants.PROT_THICKNESS;
    image.draw(getLine(getPoint(x, getY(yOffset)), 
                       getPoint(x, getY(yOffset + DomainPlotConstants.Y_OFFSET_ANNOTATION_MIN + level * fontHeight))));
    drawString(annotation, seqIndex - 3, yOffset + DomainPlotConstants.Y_OFFSET_ANNOTATION_MIN + level * fontHeight + 1, DomainPlotConstants.FONT_DEFAULT_BOLD, false);
  }

  public ArrayList<DomainPosition> getDomainPositions() {
    return domainPositions;
  }

}
