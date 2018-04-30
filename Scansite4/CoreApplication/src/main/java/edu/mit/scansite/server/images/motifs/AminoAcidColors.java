package edu.mit.scansite.server.images.motifs;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import edu.mit.scansite.server.images.Colors;
import edu.mit.scansite.shared.transferobjects.AminoAcid;

/**
 * @author Thomas (reassigned colors)
 * @author Tobieh
 */
public enum AminoAcidColors {
  P (AminoAcid.P, Colors.GREEN_DARK),  // small

  D (AminoAcid.D, Colors.RED_LIGHT), // negative
  E (AminoAcid.E, Colors.RED_LIGHT), // negative

  H (AminoAcid.H, Colors.BLUE_DARK),  // positive
  K (AminoAcid.K, Colors.BLUE_DARK),  // positive
  O (AminoAcid.O, Colors.BLUE_DARK), // positive
  R (AminoAcid.R, Colors.BLUE_DARK), // positive

  G (AminoAcid.G, Colors.YELLOW),  // small
  A (AminoAcid.A, Colors.YELLOW),  // small
  V (AminoAcid.V, Colors.YELLOW), // aliphatic

  I (AminoAcid.I, Colors.TAN), // aliphatic
  L (AminoAcid.L, Colors.TAN), // aliphatic
  M (AminoAcid.M, Colors.TAN),

  S (AminoAcid.S, Colors.ORANGE), // small
  T (AminoAcid.T, Colors.ORANGE),  // small
  C (AminoAcid.C, Colors.ORANGE), // small
  U (AminoAcid.U, Colors.ORANGE),  // small
  N (AminoAcid.N, Colors.ORANGE),  // small
  Q (AminoAcid.Q, Colors.ORANGE),

  F (AminoAcid.F, Colors.PURPLE_DARK), // aromatic
  Y (AminoAcid.Y, Colors.PURPLE_DARK), // aromatic
  W (AminoAcid.W, Colors.PURPLE_DARK), // aromatic

  
  _N (AminoAcid._N, Colors.MARPLE),
  _C (AminoAcid._C, Colors.MARPLE),
  X (AminoAcid.X, Colors.BLACK),
               
  B (AminoAcid.B, Colors.BLACK),
  Z (AminoAcid.Z, Colors.BLACK),
  J (AminoAcid.J, Colors.BLACK),

  pS (AminoAcid.pS, Colors.PINK),
  pT (AminoAcid.pT, Colors.PINK),
  pY (AminoAcid.pY, Colors.PINK);


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
