package edu.mit.scansite.shared.transferobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.mit.scansite.shared.ScansiteConstants;

/**
 * @author Konstantin Krismer
 */
public class SequenceAlignment implements IsSerializable {
  private final static char GAP_CHAR = '-';
  private List<SequenceAlignmentElement> sequences;
  private int phosphoSitePosition = 0;

  public SequenceAlignment() {

  }

  public void addSequence(SequenceAlignmentElement sequence) {
    if (sequences == null) {
      sequences = new LinkedList<SequenceAlignmentElement>();
    }
    sequences.add(sequence);
  }

  public int getPhosphoSitePosition() {
    return phosphoSitePosition;
  }

  public void setPhosphoSitePosition(int phosphoSitePosition) {
    this.phosphoSitePosition = phosphoSitePosition;
  }

  private boolean checkPosition(int i, char currentAA) {
    for (SequenceAlignmentElement sequence : sequences) {
      if (sequence.getSequence().charAt(i) != currentAA
          && sequence.getSequence().charAt(i) != GAP_CHAR) {
        return false;
      }
    }
    return true;
  }

  private boolean checkPreconditions() {
    if (sequences == null || sequences.isEmpty()) {
      return false;
    }
    int length = sequences.get(0).getSequence().length();
    for (SequenceAlignmentElement sequence : sequences) {
      if (sequence.getSequence().length() != length) {
        return false;
      }
    }
    return true;
  }

  public String getHTMLFormattedAlignment() {
    Collections.sort(sequences);
    if (checkPreconditions()) {
      List<Boolean> match = indicateMatches();
      StringBuilder HTML = new StringBuilder();
      int longestIdentifier = getLongestIdentifierLength();
      int longestOrganismName = getLongestOrganismNameLength();
      int sequenceLength = sequences.get(0).getSequence().length();
      int gapsInLeftHalfWindow = 0;
      int gapsInRightHalfWindow = 0;
      String borderTop = null;
      for (SequenceAlignmentElement sequence : sequences) {
        gapsInLeftHalfWindow = countLeftGaps(sequence.getSequence(), phosphoSitePosition);
        gapsInRightHalfWindow = countRightGaps(sequence.getSequence(), phosphoSitePosition);
        HTML.append(formatValue(sequence.getIdentifier(), longestIdentifier))
            .append('|');
        HTML.append(
            formatValue(sequence.getOrganismName(), longestOrganismName))
            .append('|');
        if(borderTop == null) {
          borderTop = " style=\"border-top: 1px dotted green;\"";
        } else {
          borderTop = "";
        }
        for (int i = 0; i < sequenceLength; ++i) {
          if (i == phosphoSitePosition - (ScansiteConstants.HALF_WINDOW + gapsInLeftHalfWindow)) {
            if (match.get(i)) {
              HTML.append("<span class=\"leftBorderSiteRegion\"")
                  .append(borderTop).append('>')
                  .append(sequence.getSequence().charAt(i)).append("</span>");
            } else {
              HTML.append("<span class=\"leftBorderSiteRegion mismatch\"")
                  .append(borderTop).append('>')
                  .append(sequence.getSequence().charAt(i)).append("</span>");
            }
          } else if (i == phosphoSitePosition + (ScansiteConstants.HALF_WINDOW + gapsInRightHalfWindow)) {
            if (match.get(i)) {
              HTML.append("<span class=\"rightBorderSiteRegion\"")
                  .append(borderTop).append('>')
                  .append(sequence.getSequence().charAt(i)).append("</span>");
            } else {
              HTML.append("<span class=\"rightBorderSiteRegion mismatch\"")
                  .append(borderTop).append('>')
                  .append(sequence.getSequence().charAt(i)).append("</span>");
            }
          } else if (i == phosphoSitePosition && sequence.hasPhosphoSite()) {
            HTML.append("<span class=\"innerSiteRegion phosphoSite\"")
                .append(borderTop).append('>')
                .append(sequence.getSequence().charAt(i)).append("</span>");
          } else if (i > phosphoSitePosition - (ScansiteConstants.HALF_WINDOW + gapsInLeftHalfWindow)
              && i < phosphoSitePosition + (ScansiteConstants.HALF_WINDOW + gapsInRightHalfWindow)) {
            if (match.get(i)) {
              HTML.append("<span class=\"innerSiteRegion\"").append(borderTop)
                  .append('>').append(sequence.getSequence().charAt(i))
                  .append("</span>");
            } else {
              HTML.append("<span class=\"innerSiteRegion mismatch\"")
                  .append(borderTop).append('>')
                  .append(sequence.getSequence().charAt(i)).append("</span>");
            }
          } else {
            if (match.get(i)) {
              HTML.append(sequence.getSequence().charAt(i));
            } else {
              HTML.append("<span class=\"mismatch\">").append(sequence.getSequence().charAt(i))
                  .append("</span>");
            }
          }
        }
        HTML.append("<br />");
      }
      return HTML.toString();
    } else {
      return "<span>wrong sequence alignment format</span>";
    }
  }

  private int countLeftGaps(String sequence, int phosphoSitePosition) {
    int gaps = 0;
    int halfWindowSize = 0;
    
    while(halfWindowSize < ScansiteConstants.HALF_WINDOW && phosphoSitePosition - 1 - halfWindowSize - gaps >= 0) {
      if(sequence.charAt(phosphoSitePosition - 1 - halfWindowSize - gaps) == GAP_CHAR) {
        ++gaps;
      } else {
        ++halfWindowSize;
      }
    }
    
    return gaps;
  }

  private int countRightGaps(String sequence, int phosphoSitePosition) {
    int gaps = 0;
    int halfWindowSize = 0;
    
    while(halfWindowSize < ScansiteConstants.HALF_WINDOW && phosphoSitePosition + 1 + halfWindowSize + gaps < sequence.length()) {
      if(sequence.charAt(phosphoSitePosition + 1 + halfWindowSize + gaps) == GAP_CHAR) {
        ++gaps;
      } else {
        ++halfWindowSize;
      }
    }
    
    return gaps;
  }

  private Object formatValue(String identifier, int longestIdentifier) {
    String spacer = "";
    for (int i = 0; i < longestIdentifier - identifier.length(); ++i) {
      spacer += "&nbsp;";
    }
    return spacer + identifier;
  }

  private int getLongestOrganismNameLength() {
    int longestLength = 0;
    for (SequenceAlignmentElement sequence : sequences) {
      if (sequence.getOrganismName().length() > longestLength) {
        longestLength = sequence.getOrganismName().length();
      }
    }
    return longestLength;
  }

  private int getLongestIdentifierLength() {
    int longestLength = 0;
    for (SequenceAlignmentElement sequence : sequences) {
      if (sequence.getIdentifier().length() > longestLength) {
        longestLength = sequence.getIdentifier().length();
      }
    }
    return longestLength;
  }

  private List<Boolean> indicateMatches() {
    String firstSequence = sequences.get(0).getSequence();
    List<Boolean> match = new ArrayList<Boolean>(firstSequence.length());

    for (int i = 0; i < firstSequence.length(); ++i) {
      if (firstSequence.charAt(i) == GAP_CHAR) {
        match.add(checkPosition(i, getNonGapAtPosition(i)));
      } else {
        match.add(checkPosition(i, firstSequence.charAt(i)));
      }
    }

    return match;
  }

  private char getNonGapAtPosition(int i) {
    for (SequenceAlignmentElement sequence : sequences) {
      if (sequence.getSequence().charAt(i) != GAP_CHAR) {
        return sequence.getSequence().charAt(i);
      }
    }
    return GAP_CHAR;
  }
}
