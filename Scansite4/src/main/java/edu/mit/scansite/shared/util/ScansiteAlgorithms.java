package edu.mit.scansite.shared.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.HistogramDataPoint;

/**
 * @author Tobieh
 */
public class ScansiteAlgorithms {
  private static final Map<Character, AminoAcid> oneLetter2Aa = AminoAcid.getOneLetterCodeMap();

  /**
   * @param d A number.
   * @return The base2-logarithm of the given number.
   */
  public static double log2(double d) {
    return (Math.log(d)/Math.log(2.0));
  }
  
  /**
   * @param scores A list of values.
   * @return The median of the values.
   */
  public static double median(List<Double> scores) {
    Collections.sort(scores);
    int size = scores.size(); 
    if (size == 0) {
      return 0;
    }
    return (size % 2 == 0) ? (scores.get(size / 2) + scores.get(size / 2 - 1)) / 2.0 : scores.get(size / 2);
  }
  
  /**
   * @param scores A list of values.
   * @param median The median. If NULL, the median is calculated.
   * @return The median absolute deviation.
   */
  public static double medianAbsDev(List<Double> scores, Double median) {
    if (scores == null) {
      return 0;
    }
    if (median == null) {
      median = median(scores);
    }
    ArrayList<Double> deviations = new ArrayList<Double>(); 
    for (int i = 0; i < scores.size(); ++i) {
      deviations.add(Math.abs(scores.get(i) - median));
    }
    return median(deviations);
  }
  
  /**
   * Calculates the proteins molecular weight.
   * @param sequence The protein's sequence.
   * @param phosphorylations The number of phosphorylations the mw is calculated for.
   * @return The protein's molecular weight.
   */
  public double calculateMolecularWeight(String sequence, int phosphorylations) {
    if (phosphorylations < 0) {
      phosphorylations = 0;
    }
    double mw = 0;
    char seq[] = sequence.toCharArray();
    if (sequence != null && sequence.length() > 0) {
      // Start with mw = N-term (2 H's) + C-term (O)
      mw = AminoAcid._N.getMolecularWeight() + AminoAcid._C.getMolecularWeight();
      for (int i =0; i < seq.length; ++i) {
        AminoAcid aa = oneLetter2Aa.get(seq[i]);
        if (aa != null) {
          mw += aa.getMolecularWeight();
        }
      }
    }
    mw += phosphorylations * ScansiteConstants.PHOSPHORYLATION_WEIGHT;
    return mw;
  }
  
  /**
   * Calculates the proteins isoelectric point (pI).
   * @param sequence The protein's sequence.
   * @param phosphorylations The number of phosphorylations the pI is calculated for.
   * @return The protein's pI.
   * Resource: Scansite 2 + expasy-tool for calculating pI
   */
  public double calculateIsoelectricPoint(String sequence, int phosphorylations) {
    if (phosphorylations < 0) {
      phosphorylations = 0;
    }
    double pI = 0;
    char seq[] = sequence.toCharArray();
    if (sequence != null && sequence.length() > 0) {
      int dCount = 0; // D   
      int eCount = 0; // E
      int cCount = 0; // C
      int yCount = 0; // Y
      int hCount = 0; // H
      int kCount = 0; // K
      int rCount = 0; // R 
      
      for (int i =0; i < seq.length; ++i) {
        AminoAcid aa = oneLetter2Aa.get(seq[i]);
        if (aa == null) {
          aa = AminoAcid.X;
        }
        switch (aa) {
          case D:  ++dCount; break;
          case E:  ++eCount; break;
          case C:  ++cCount; break;
          case Y:  ++yCount; break;
          case H:  ++hCount; break;
          case K:  ++kCount; break;
          case R:  ++rCount; break;
          default: break;
        }
      }

      // starting charges
      double charge = 1; //net charge in given pH
      double _cCharge = 0;  //C-terminal charge
      double _nCharge = 0;  //NH2 charge
      double dCharge = 0;  //D charge
      double eCharge = 0;  //E charge
      double cCharge = 0;  //C charge
      double yCharge = 0;  //Y charge
      double hCharge = 0;  //H charge
      double kCharge = 0;  //K charge
      double rCharge = 0;  //R charge

      //pKa of phosphate group
      double phos1Pk = ScansiteConstants.PK_PHOS1;  // first proton 
      double phos2Pk = ScansiteConstants.PK_PHOS2;  // second proton
      
      // starting pH values
      double pHMin = ScansiteConstants.PH_MIN;
      double pHMax = ScansiteConstants.PH_MAX;
      double chargePhos1 = 0;
      double chargePhos2 = 0;
      
      // pK-values and indices
      String resIndices = ScansiteConstants.PK_VALUE_RESIDUES;
      int _cIdx = resIndices.indexOf(seq[seq.length - 1]);  //C-terminal 
      int _nIdx = resIndices.indexOf(seq[0]);  //N-terminal
      
      // in case there is an invalid letter at one of the terminals use 'X' which is treated like 'neutral' AA
      if (_cIdx < 0) {
        _cIdx = resIndices.indexOf('X');
      }
      if (_nIdx < 0) {
        _nIdx = resIndices.indexOf('X');
      }
      
      int dIdx = resIndices.indexOf('D');  //D 
      int eIdx = resIndices.indexOf('E');  //E 
      int cIdx = resIndices.indexOf('C');  //C 
      int yIdx = resIndices.indexOf('Y');  //Y 
      int hIdx = resIndices.indexOf('H');  //H 
      int kIdx = resIndices.indexOf('K');  //K 
      int rIdx = resIndices.indexOf('R');  //R
      double pKs[][] = ScansiteConstants.PK_VALUES;
      
      for (int i = 0; i < ScansiteConstants.MAX_PI_ITERATIONS && (pHMax - pHMin) > ScansiteConstants.PI_THRESHOLD; ++i) {
        pI = (pHMin + pHMax) / 2.0;
        
        // Calculate the component charges at current pH
        _cCharge = Math.pow(10, - pKs[_cIdx][0]) / (Math.pow(10, -pKs[_cIdx][0]) + Math.pow(10, - pI));
        _nCharge = Math.pow(10, -pI) / (Math.pow(10, -pKs[_nIdx][1]) + Math.pow(10, - pI));
        rCharge = rCount * Math.pow(10, - pI) / (Math.pow(10, -pKs[rIdx][2]) + Math.pow(10, - pI));
        hCharge = hCount * Math.pow(10, - pI) / (Math.pow(10, -pKs[hIdx][2]) + Math.pow(10, - pI));
        kCharge = kCount * Math.pow(10, - pI) / (Math.pow(10, -pKs[kIdx][2]) + Math.pow(10, - pI));
        dCharge = dCount * Math.pow(10, - pKs[dIdx][2]) / (Math.pow(10, -pKs[dIdx][2]) + Math.pow(10, - pI));
        eCharge = eCount * Math.pow(10, - pKs[eIdx][2]) / (Math.pow(10, -pKs[eIdx][2]) + Math.pow(10, - pI));
        cCharge = cCount * Math.pow(10, - pKs[cIdx][2]) / (Math.pow(10, -pKs[cIdx][2]) + Math.pow(10, - pI));
        yCharge = yCount * Math.pow(10, - pKs[yIdx][2]) / (Math.pow(10, -pKs[yIdx][2]) + Math.pow(10, - pI));
       
        chargePhos1 = phosphorylations * Math.pow(10, - phos1Pk) / (Math.pow(10, - phos1Pk) + Math.pow(10, - pI));
        chargePhos2 = phosphorylations * Math.pow(10, - phos2Pk) / (Math.pow(10, - phos2Pk) + Math.pow(10, - pI));
  
        // Add up total charge at this pH
        charge = rCharge + kCharge + hCharge + _nCharge - (dCharge + eCharge + yCharge + cCharge + _cCharge + chargePhos1 + chargePhos2);
  
        // If protein is still charged, select new pH max/min
        if (charge > 0.0) {
          pHMin = pI;
        } else {
          pHMax = pI;
        }
      }
    }
    return pI;
  }
  
  /**
   * Calculates a protein sequence's surface accessibility values for each position in its sequence.
   * @param sequence A protein's sequence.
   * @return An array that contains the surface accessibility value for each position in the 
   * sequence, or NULL, in case the given sequence is NULL.
   */
  public Double[] calculateSurfaceAccessibility(String sequence) {
    Double[] sa = null;
    if (sequence != null) {
      sa = new Double[sequence.length()];

      double accessValue = 1; // value for each residue
      double productValue = 1;   // value for nearby residues
      for (int i = 0; i < sequence.length(); ++i) {
        productValue = 1.0;
        for (int j = Math.max(0, i - 2); j < Math.min(sequence.length(), i + 4); j++) {
          AminoAcid aa = oneLetter2Aa.get(sequence.charAt(j));
          if (aa != null) {
            accessValue = aa.getSurfaceAccessValue();
          } else {
            accessValue = 1;
          }
          productValue *= accessValue;
        }
        sa[i] = productValue * ScansiteConstants.SURFACE_ACCESSIBILITY_FACTOR;
      }
    }
    return sa;
  }
  
  /**
   * @param datapoints The list of HistogramDataPoints that is binned into groups.
   * @return A list of histogramdatapoints, binned to groups.
   */
  public ArrayList<HistogramDataPoint> getHistogramBins(Map<Double, Integer> datapoints) {
    HashMap<Double, Double> bins = new HashMap<Double, Double>();
    ScansiteScoring scoring = new ScansiteScoring();
    
    double binScore = 0;
    for (Double score : datapoints.keySet()) {
      double currentAF = datapoints.get(score);
      binScore = scoring.getHistogramScore(score, datapoints.size()); // bin operation
      
      Double tempVal = bins.get(binScore);
      bins.put(binScore, (tempVal == null ? 0 : tempVal) + currentAF);
    }
    ArrayList<HistogramDataPoint> hdps = new ArrayList<HistogramDataPoint>();
    for (Double score : bins.keySet()) {
      hdps.add(new HistogramDataPoint(score, bins.get(score)));
    }
    return hdps;
  }

  /**
   * @param tree A tree.
   * @param itemText The text that describes a treeItem in the given tree.
   * @return The treeItem that represents the itemText, or NULL if none is found.
   */
  public TreeItem getTreeItem(Tree tree, String itemText) {
    if (tree != null) {
      Iterator<TreeItem> it = tree.treeItemIterator();
      while (it.hasNext()) {
        TreeItem item = it.next();
        if (item.getText().equals(itemText)) {
          return item;
        }
      }
    }
    return null;
  }
}
