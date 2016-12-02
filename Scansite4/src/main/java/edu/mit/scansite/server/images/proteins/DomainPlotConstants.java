package edu.mit.scansite.server.images.proteins;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import edu.mit.scansite.server.images.Colors;

/**
 * @author Tobieh
 */
public class DomainPlotConstants {
  
  public static final int IMAGE_WIDTH_CONSTANT = 110;
  public static final int IMAGE_HEIGHT = 2000;
  public static final int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;
  
  public static final int Y_OFFSET_N = 30;
  public static final int Y_OFFSET_W = 30;
  
  public static final int Y_OFFSET_AALINE = 60; 
  public static final int Y_OFFSET_PROTEIN = Y_OFFSET_AALINE + 45; 

  public static final int Y_OFFSET_ANNOTATION_MIN = 20;
  
  public static final int AALINE_STEPSIZE = 100;
  public static final int MARKER_LENGTH = 2;
  
  public static final float DEFAULT_STROKE_WIDTH = 1;
  public static final float PROTEIN_STROKE_WIDTH = 1;
  public static final float DOMAIN_STROKE_WIDTH = 9;
  public static final BasicStroke DEFAULT_STROKE = new BasicStroke(DEFAULT_STROKE_WIDTH);
  public static final BasicStroke PROTEIN_STROKE = new BasicStroke(PROTEIN_STROKE_WIDTH);
  public static final BasicStroke DOMAIN_STROKE =  new BasicStroke(DOMAIN_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
  public static final float AXIS_STROKE_WIDTH = 1;
  public static final int PROT_THICKNESS = 2;

  public static final Color COLOR_BACKGROUND = Colors.WHITE;
  public static final Color COLOR_DEFAULT = Colors.BLACK;
  
  public static final int FONT_SIZE_DEFAULT = 12;
  public static final int FONT_SIZE_HIT = 12;
  public static final int FONT_SIZE_DOMAIN_RANGE = 9;
  public static final Font FONT_DEFAULT = new Font("Sans Serif", Font.PLAIN, FONT_SIZE_DEFAULT);
  public static final Font FONT_DEFAULT_BOLD = new Font("Sans Serif", Font.BOLD, FONT_SIZE_DEFAULT);
  public static final Font FONT_HIT = new Font("Sans Serif", Font.PLAIN, FONT_SIZE_HIT);
  public static final Font FONT_DOMAIN_RANGE = new Font("Sans Serif", Font.PLAIN, FONT_SIZE_DOMAIN_RANGE);
  
  public static final String TEXT_AAs = "AAs";
  public static final String TEXT_DOMAINS_NONE_FOUND = "No domains found.";
  public static final String TEXT_DOMAINS_ERROR = "Error retrieving domains.";
  public static final String TEXT_DOMAINS = "Domains";
  
}
