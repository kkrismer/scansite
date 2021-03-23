package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class AminoAcidRegex implements IsSerializable {

  public static String getAaRegex(char charAt) {
    String aaVal = getAaValues(charAt);
    if (aaVal.equals("X")) {
      return "A-Z";
    } else {
      return aaVal; 
    }
  }
  
  /**
   * $ Aliphatic residues (GAVILM)
   * ! Polar uncharged residues (STNQ)
   * # Positive residues (HKR)
   * & Negative residues (DE)
   * @ Aromatic residues (FYW)
   * X Any residue
   * @param aaRegexChar a regex character, or an amino acid's one letter code.
   * @return a string that contains all possible aa values of the given character.
   */
  public static String getAaValues(char aaRegexChar) {
    switch (aaRegexChar) {
      case 'a': 
      case 'A': return "A";
      case 'b': 
      case 'B': return "BDN"; 
      case 'c': 
      case 'C': return "C"; 
      case 'd': 
      case 'D': return "D"; 
      case 'e':  
      case 'E': return "E"; 
      case 'f': 
      case 'F': return "F"; 
      case 'g': 
      case 'G': return "G"; 
      case 'h': 
      case 'H': return "H"; 
      case 'i': 
      case 'I': return "I"; 
      case 'j': 
      case 'J': return "JIL"; 
      case 'k': 
      case 'K': return "K"; 
      case 'l': 
      case 'L': return "L"; 
      case 'm': 
      case 'M': return "M"; 
      case 'n': 
      case 'N': return "N"; 
      case 'o': 
      case 'O': return "O"; 
      case 'p': 
      case 'P': return "P"; 
      case 'q': 
      case 'Q': return "Q"; 
      case 'r': 
      case 'R': return "R"; 
      case 's': 
      case 'S': return "S"; 
      case 't': 
      case 'T': return "T"; 
      case 'u': 
      case 'U': return "U"; 
      case 'v': 
      case 'V': return "V"; 
      case 'w': 
      case 'W': return "W"; 
      case 'x': 
      case 'X': return "X"; 
      case 'y': 
      case 'Y': return "Y"; 
      case 'z': 
      case 'Z': return "ZEQ"; 
      case '$': return "GAVILM"; 
      case '!': return "STWQ"; 
      case '#': return "HKR"; 
      case '&': return "DE"; 
      case '@': return "FYW";
      default: return "";
    }
  }
}
