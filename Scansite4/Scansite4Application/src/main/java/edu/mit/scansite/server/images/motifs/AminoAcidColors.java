package edu.mit.scansite.server.images.motifs;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import edu.mit.scansite.server.images.Colors;
import edu.mit.scansite.shared.transferobjects.AminoAcid;

/**
 * @author Tobieh
 */
public enum AminoAcidColors {
  S (AminoAcid.S, Colors.BROWN), // small
  A (AminoAcid.A, Colors.BROWN_DARK),  // small
  G (AminoAcid.G, Colors.TAN),  // small
  
  D (AminoAcid.D, Colors.GRAY_DARK), // negative
  E (AminoAcid.E, Colors.GRAY_LIGHT), // negative

  H (AminoAcid.H, Colors.RED),  // positive
  K (AminoAcid.K, Colors.RED_LIGHT),  // positive
  O (AminoAcid.O, Colors.RED_LIGHT), // positive
  R (AminoAcid.R, Colors.RED_DARK), // positive

  P (AminoAcid.P, Colors.ORANGE),  // small
  N (AminoAcid.N, Colors.ORANGE_DARK),  // small

  Q (AminoAcid.Q, Colors.YELLOW),

  F (AminoAcid.F, Colors.GREEN), // aromatic
  Y (AminoAcid.Y, Colors.GREEN_LIGHT), // aromatic
  W (AminoAcid.W, Colors.GREEN_DARK), // aromatic

  M (AminoAcid.M, Colors.BLUE_GREEN),
  
  I (AminoAcid.I, Colors.BLUE_DARK), // aliphatic
  L (AminoAcid.L, Colors.BLUE_LIGHT), // aliphatic
  V (AminoAcid.V, Colors.BLUE), // aliphatic

  C (AminoAcid.C, Colors.PURPLE_DARK), // small
  U (AminoAcid.U, Colors.PURPLE_DARK),  // small
  T (AminoAcid.T, Colors.PURPLE),  // small
  
  _N (AminoAcid._N, Colors.MARPLE),
  _C (AminoAcid._C, Colors.MARPLE),
  X (AminoAcid.X, Colors.BLACK),
               
  B (AminoAcid.B, Colors.BLACK),
  Z (AminoAcid.Z, Colors.BLACK),
  J (AminoAcid.J, Colors.BLACK);
  
  final AminoAcid aa;
  final Color color;
  
  AminoAcidColors(AminoAcid aa, Color color) {
    this.aa = aa;
    this.color = color;
  }
  
  /**
   * @return a mapping of all amino acids and their colors (including wildcard-amino acids). 
   */
  public static Map<AminoAcid, Color> getAminoAcidColorMap() {
    Map<AminoAcid, Color> map = new HashMap<AminoAcid, Color>();
    for (AminoAcidColors aac : AminoAcidColors.values()) {
      map.put(aac.aa, aac.color);
    }
    return map;
  }
  
}
