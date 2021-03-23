package edu.mit.scansite.server.images.proteins;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import edu.mit.scansite.server.images.Colors;
import edu.mit.scansite.server.images.Painter;
import edu.mit.scansite.shared.ScanResultSiteComparator;
import edu.mit.scansite.shared.ScanResultSiteComparator.ComparableFields;
import edu.mit.scansite.shared.transferobjects.DomainPosition;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;

/**
 * @author Tobieh
 */
public class ProteinPlotPainter extends Painter {
  
  private final int IMAGE_WIDTH;
  private final int SEQ_WIDTH;
  
  private int maxYCoord = 0;
  
  private String proteinName;
  private String sequence;
  private Double[] surfaceAccessValues;
  private List<DomainPosition> domainPositions;
  private boolean displayDomains = false;
  
  private int gColorIdx = 0;
  private Color[] groupColors = {
      Colors.BLUE, Colors.BROWN, Colors.RED_DARK, Colors.YELLOW, Colors.BLUE_GREEN, Colors.GREEN_LIGHT, 
      Colors.GREEN_DARK, Colors.RED, Colors.ORANGE, Colors.BLUE_DARK, Colors.MARPLE, Colors.GRAY_LIGHT,
      Colors.TAN, Colors.PURPLE, Colors.GREEN, Colors.BROWN_DARK, Colors.ORANGE_DARK, Colors.PURPLE_DARK
      };
  
  private int dColorIdx = 0;
  private Color[] domainColors = {
      Colors.ORANGE_DARK, Colors.PURPLE_DARK, Colors.BROWN_DARK, Colors.GREEN_LIGHT, Colors.PURPLE,
      Colors.TAN, Colors.GRAY_LIGHT, Colors.RED, Colors.BLUE, Colors.BLUE_DARK, Colors.GRAY_DARK, Colors.BLUE_GREEN,  
      Colors.ORANGE,  Colors.MARPLE, Colors.BROWN, Colors.GREEN, Colors.YELLOW, Colors.GREEN_DARK, Colors.RED_DARK
      };

  /**
   * Creates and Initializes a proteinplotpainter that displays the protein with (optional) domains
   * and a surface accessibility plot. Sites can be applied using the applyHits(..) method.
   * @param sequence The protein's sequence.
   * @param savs An array of SurfaceAccessValues.
   * @param displayDomains TRUE, if a showing domains is requested, otherwise FALSE.
   * @param list A list of domainPositions or NULL if there was an error with retrieving domains. 
   * This value is only used, if the parameter displayDomains is TRUE.  
   */
  public ProteinPlotPainter(String proteinName, String sequence, Double[] savs, boolean displayDomains, List<DomainPosition> list) {
    this.proteinName = proteinName;
    this.sequence = sequence;
    this.surfaceAccessValues = savs;
    this.domainPositions = list;
    this.displayDomains = displayDomains;
    IMAGE_WIDTH = sequence.length() + ProteinPlotConstants.IMAGE_WIDTH_CONSTANT;
    SEQ_WIDTH = sequence.length();
    
    bImg = new BufferedImage(IMAGE_WIDTH, ProteinPlotConstants.IMAGE_HEIGHT, ProteinPlotConstants.IMAGE_TYPE);
    image = bImg.createGraphics();
    image.setBackground(ProteinPlotConstants.COLOR_BACKGROUND);
    image.clearRect(0, 0, IMAGE_WIDTH, ProteinPlotConstants.IMAGE_HEIGHT);
    setColor(ProteinPlotConstants.COLOR_DEFAULT);
    setStroke(ProteinPlotConstants.DEFAULT_STROKE);
    init();
  }
  
  private void init() {
    drawProtein();
    drawAaLine();
    drawSaPlot();
    drawProteinName();
  }
  
  private void drawProtein() {
    setColor(Colors.BLACK);
    setStroke(ProteinPlotConstants.PROTEIN_STROKE);
    int yCoord = ProteinPlotConstants.Y_OFFSET_PROTEIN;
    drawSequenceLine(yCoord, false, ProteinPlotConstants.MARKER_LENGTH + 2, true);
    drawString(ProteinPlotConstants.TEXT_PRED_SITES, SEQ_WIDTH + ProteinPlotConstants.PROTEIN_STROKE_WIDTH + 2, yCoord + getFontHeight(ProteinPlotConstants.FONT_DEFAULT), 
        ProteinPlotConstants.FONT_DEFAULT, false);
  }

  private void drawDomains() {
    boolean noDomains = false;
    String domainsText = "";
    if (displayDomains) {
      if (domainsFound()) {
        doDrawDomains();
        domainsText = ProteinPlotConstants.TEXT_DOMAINS;
      } else {
        noDomains = true;
        if (domainPositions != null) { // no domains found
          domainsText = ProteinPlotConstants.TEXT_DOMAINS_NONE_FOUND;
        } else { // domain retrieval failed
          domainsText = ProteinPlotConstants.TEXT_DOMAINS_ERROR;
        }
      }
    } else {
      noDomains = true;
      domainsText = ProteinPlotConstants.TEXT_DOMAINS_NOT_REQUESTED;
    }
    if (noDomains) {
      drawString(domainsText, 5, ProteinPlotConstants.Y_OFFSET_PROTEIN - getFontHeight(ProteinPlotConstants.FONT_DEFAULT) - 10,
          ProteinPlotConstants.FONT_DEFAULT, false);
    } else {
      drawString(domainsText, SEQ_WIDTH + ProteinPlotConstants.PROTEIN_STROKE_WIDTH + 2, ProteinPlotConstants.Y_OFFSET_PROTEIN - getFontHeight(ProteinPlotConstants.FONT_DEFAULT) - 10,
          ProteinPlotConstants.FONT_DEFAULT, false);
    }
  }

  private void doDrawDomains() {
    int yStr = ProteinPlotConstants.Y_OFFSET_PROTEIN;
    int y = getY(yStr);
    double yLine1 = yStr - getFontHeight(ProteinPlotConstants.FONT_DEFAULT_BOLD) - 5;
    double yLine2 = yLine1 - getFontHeight(ProteinPlotConstants.FONT_DEFAULT_BOLD) - 2;
    Map<String, Color> dColors = getDomainColors(domainPositions);
    Collections.sort(domainPositions);
    for (DomainPosition domain : domainPositions) {
      setColor(dColors.get(domain.getName()));
      setStroke(ProteinPlotConstants.DOMAIN_STROKE);
      double xFromStr = domain.getFrom();
      double xFrom = getX(xFromStr);
      double xToStr = domain.getTo();
      double xTo = getX(xToStr);
      image.draw(getLine(getPoint(xFrom, y), getPoint(xTo, y)));
      
      setColor(ProteinPlotConstants.COLOR_DEFAULT);
      setStroke(ProteinPlotConstants.DEFAULT_STROKE);
      drawString(domain.getName(), xFromStr, yLine1, ProteinPlotConstants.FONT_DEFAULT_BOLD, false);
      drawString("(" + String.valueOf((int)xFromStr) + "-" + String.valueOf((int)xToStr) + ")", xFromStr, yLine2, ProteinPlotConstants.FONT_DOMAIN_RANGE, false);
    }
  }

  private void drawSaPlot() {
    setColor(Colors.GRAY_LIGHT);
    setStroke(ProteinPlotConstants.DEFAULT_STROKE);
    int yCoord = ProteinPlotConstants.Y_OFFSET_SA_LINE;
    drawSequenceLine(yCoord + (int) ProteinPlotConstants.SA_FACTOR, false, ProteinPlotConstants.MARKER_LENGTH, false);
    drawString("1.0", SEQ_WIDTH + 2, yCoord - 1, ProteinPlotConstants.FONT_DEFAULT, false);
    
    setColor(Colors.GRAY_DARK);
    Point2D.Double lastPoint = getPoint(getX(0), getSaY(yCoord, 1));
    Point2D.Double currentPoint;
    boolean notFirst = false;
    for (int i = 4; i < SEQ_WIDTH - 3; ++i) {
      currentPoint = getPoint(getX(i), getSaY(yCoord, surfaceAccessValues[i]));
      if (notFirst) {
        image.draw(getLine(lastPoint, currentPoint));
      } else {
        notFirst = true;
      }
      lastPoint = currentPoint;
    }

    drawString(ProteinPlotConstants.TEXT_SA, SEQ_WIDTH + ProteinPlotConstants.DEFAULT_STROKE_WIDTH + 2, yCoord + getFontHeight(ProteinPlotConstants.FONT_DEFAULT), 
        ProteinPlotConstants.FONT_DEFAULT, false);
  }
  
  private double getSaY(int yCoord, double saValue) {
    return getY(yCoord + ProteinPlotConstants.SA_FACTOR*(saValue));// - 1));
  }
  
  private void drawProteinName() {
    if (proteinName != null && !proteinName.isEmpty()) {
      drawString("Protein: " + proteinName, 0, 20 + getFontHeight(ProteinPlotConstants.FONT_DEFAULT), 
          ProteinPlotConstants.FONT_DEFAULT, false);
    }
  }

  private void drawAaLine() {
    setColor(Colors.BLACK);
    setStroke(ProteinPlotConstants.DEFAULT_STROKE);
    int yCoord = ProteinPlotConstants.Y_OFFSET_AALINE;
    drawSequenceLine(yCoord, true, ProteinPlotConstants.MARKER_LENGTH, false);
    //draw number of amino acids
    drawString(String.valueOf(sequence.length()) + " " + ProteinPlotConstants.TEXT_AAs, SEQ_WIDTH + ProteinPlotConstants.DEFAULT_STROKE_WIDTH + 2, yCoord - 1, 
        ProteinPlotConstants.FONT_DEFAULT, false);
  }

  private void drawSequenceLine(int yCoord, boolean withNumbers, int markerLength, boolean isProtein) {
    //draw line
    int yCoordPlot = getY(yCoord);
    if (isProtein) {
      int halfThickness = ProteinPlotConstants.PROT_THICKNESS;
      image.draw(getLine(getPoint(getX(0), yCoordPlot+halfThickness), getPoint(getX(SEQ_WIDTH), yCoordPlot+halfThickness)));
      image.draw(getLine(getPoint(getX(0), yCoordPlot-halfThickness), getPoint(getX(SEQ_WIDTH), yCoordPlot-halfThickness)));
    } else {
      image.draw(getLine(getPoint(getX(0), yCoordPlot), getPoint(getX(SEQ_WIDTH), yCoordPlot)));
    }
    //draw markers
    image.draw(getLine(getPoint(getX(0), yCoordPlot - markerLength - 3), getPoint(getX(0), yCoordPlot + markerLength + 3)));
    image.draw(getLine(getPoint(getX(SEQ_WIDTH), yCoordPlot - markerLength - 3), getPoint(getX(SEQ_WIDTH), yCoordPlot + markerLength + 3)));
    setStroke(ProteinPlotConstants.DEFAULT_STROKE);
    for (int i = ProteinPlotConstants.AALINE_STEPSIZE; i < SEQ_WIDTH; i += ProteinPlotConstants.AALINE_STEPSIZE) {
      image.draw(getLine(getPoint(getX(i - 1), yCoordPlot - markerLength), getPoint(getX(i - 1), yCoordPlot + markerLength)));
      if (withNumbers) {
        drawString(String.valueOf(i), i - 1, yCoord - markerLength, ProteinPlotConstants.FONT_DEFAULT, true);
      }
    }
  }

  protected int getX(double x) {
    return (int) x + ProteinPlotConstants.Y_OFFSET_W;
  }
  
  protected int getY(double y) {
    if (y > maxYCoord) {
      maxYCoord = (int) y;
    }
    return (int) (ProteinPlotConstants.IMAGE_HEIGHT - y); 
  }
  
  private boolean domainsFound() {
    return domainPositions!=null && !domainPositions.isEmpty();
  }
  
  public void applyHits(List<ScanResultSite> hits) {
    setStroke(ProteinPlotConstants.DEFAULT_STROKE);
    Map<LightWeightMotifGroup, Color> gColors = getGroupColors(hits);

    ScanResultSiteComparator comp = new ScanResultSiteComparator();
    comp.setCompareField(ComparableFields.POSITION);
    Collections.sort(hits, comp);

    double level = 0;
    double fontHeight = getFontHeight(ProteinPlotConstants.FONT_DEFAULT);
    double yOffsMin = ProteinPlotConstants.Y_OFFSET_PROTEIN + ProteinPlotConstants.Y_OFFSET_HIT_MIN;
    double maxWidth = 0;
    double lastX = sequence.length();
    
    List<String> names = new ArrayList<String>();
    
    Set<String> currentGroups;
    for (int i = hits.size() - 1; i >= 0; --i) {
      ScanResultSite site = hits.get(i);
      
      int currentPos = site.getPosition();
      double xStr = currentPos;
      double x = getX(xStr);

      currentGroups = new HashSet<String>();
      LightWeightMotifGroup g = site.getMotif().getGroup();
      String gName = getGroupName(g);
      currentGroups.add((gName));
      for (int j = i; j > 0 && hits.get(j - 1).getPosition() == currentPos; --j) {
        currentGroups.add(getGroupName(hits.get(j).getMotif().getGroup()));
      }
      names.addAll(currentGroups);
      maxWidth = getMaxStringWidth(names);
      if (lastX - x > maxWidth || level > ProteinPlotConstants.MAX_LEVEL) {
        level = 0;
      }

      image.draw(getLine(getPoint(x, getY(ProteinPlotConstants.Y_OFFSET_PROTEIN+ProteinPlotConstants.PROT_THICKNESS)), 
                         getPoint(x, getY(yOffsMin + level * fontHeight))));
      drawString(site.getSite(), xStr + 1, yOffsMin + level * fontHeight + 1, ProteinPlotConstants.FONT_DEFAULT_BOLD, false);
      names.add(site.getSite());

      Set<LightWeightMotifGroup> paintedGroups = new HashSet<LightWeightMotifGroup>();
      g = site.getMotif().getGroup();
      level = drawMotifGroupName(gColors.get(g), level, fontHeight, getGroupName(g), xStr, yOffsMin);
      paintedGroups.add(g);
      while (i > 0 && hits.get(i - 1).getPosition() == currentPos) {
        site = hits.get(--i);
        g = site.getMotif().getGroup();
        if (!paintedGroups.contains(g)) {
          level = drawMotifGroupName(gColors.get(g), level, fontHeight, getGroupName(g), xStr, yOffsMin);
          paintedGroups.add(g);
        }
      }
      ++level;
      lastX = x;
      setColor(ProteinPlotConstants.COLOR_DEFAULT);
    }
  }

  private String getGroupName(LightWeightMotifGroup g) {
    String gName = (g == null || g.getShortName() == null || g.getShortName().isEmpty()) ? ProteinPlotConstants.USER_GROUP : g.getShortName();
    return gName;
  }

  private double drawMotifGroupName(Color color, double level,
      double fontHeight, String s, double x, double yOffsMin) {
    setColor(color);
    drawString(s, x, yOffsMin + ++level * fontHeight + 1, ProteinPlotConstants.FONT_DEFAULT, false);
    return level;
  }
  
  private double getMaxStringWidth(List<String> names) {
    double max = 0;
    for (String name : names) {
      if (name.length() > max) {
        max = name.length();
      }
    }
    max = max * getFontWidth(ProteinPlotConstants.FONT_DEFAULT) + 15;
    return max;
  }

  private Map<LightWeightMotifGroup, Color> getGroupColors(List<ScanResultSite> hits) {
    Map<LightWeightMotifGroup, Color> gColors = new HashMap<LightWeightMotifGroup, Color>(); 
    LightWeightMotifGroup group = new LightWeightMotifGroup();
    for (ScanResultSite site : hits) {
      group = site.getMotif().getGroup();
      group = (group == null) ? new LightWeightMotifGroup() : group;
      gColors.put(group, nextGroupColor());
    }
    gColors.put(new LightWeightMotifGroup(0, ProteinPlotConstants.USER_GROUP, ProteinPlotConstants.USER_GROUP), nextGroupColor());
    return gColors;
  }

  private Map<String, Color> getDomainColors(List<DomainPosition> domains) {
    ScanResultSiteComparator comp = new ScanResultSiteComparator();
    comp.setCompareField(ComparableFields.GROUP);
    Map<String, Color> dColors = new HashMap<String, Color>(); 
    for (DomainPosition d : domains) {
      dColors.put(d.getName(), nextDomainColor());
    }
    return dColors;
  }

  private Color nextGroupColor() {
    Color c = groupColors[gColorIdx];
    gColorIdx = (gColorIdx + 1) % groupColors.length;
    return c;
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
    drawDomains();
    if (maxYCoord < ProteinPlotConstants.IMAGE_HEIGHT) {
      cropImage(IMAGE_WIDTH, maxYCoord + ProteinPlotConstants.Y_OFFSET_N);
    }
    return bImg;
  }

}
