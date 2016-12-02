package edu.mit.scansite.shared.transferobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.mit.scansite.shared.FilePaths;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;

/**
 * A histogram that can be used by the client.
 * 
 * @author tobieh
 */
public class Histogram implements IsSerializable {

  protected Motif motif;
  protected Taxon taxon;
  protected DataSource dataSource;
  protected Double thresholdHigh = null;
  protected Double thresholdMedium = null;
  protected Double thresholdLow = null;

  protected Double median = null;
  protected Double medianAbsDev = null;
  protected String imageFilePath;

  protected Integer sitesScored = 0;
  protected Integer proteinsScored = 0;

  protected ArrayList<HistogramDataPoint> binnedDataPoints = new ArrayList<HistogramDataPoint>();

  public Histogram() {
  }

  public Histogram(Motif motif, DataSource dataSource, Taxon taxon) {
    this.motif = motif;
    this.dataSource = dataSource;
    this.taxon = taxon;
  }

  public Histogram(Motif motif, DataSource dataSource, Taxon taxon,
      double threshHigh, double threshMed, double threshLow, double median,
      double medianAbsDev, int sitesScored, int proteinsScored,
      String imageFilePath, ArrayList<HistogramDataPoint> dataPoints) {
    this(motif, dataSource, taxon);
    this.thresholdHigh = threshHigh;
    this.thresholdMedium = threshMed;
    this.thresholdLow = threshLow;
    this.median = median;
    this.medianAbsDev = medianAbsDev;
    this.imageFilePath = imageFilePath;
    this.sitesScored = sitesScored;
    this.proteinsScored = proteinsScored;
    this.binnedDataPoints = dataPoints;
  }

  public Histogram(Histogram hist) {
    this(hist.getMotif(), hist.getDataSource(), hist.getTaxon(), hist
        .getThresholdHigh(), hist.getThresholdMedium(), hist.getThresholdLow(),
        hist.getMedian(), hist.getMedianAbsDev(), hist.getSitesScored(), hist
            .getProteinsScored(), hist.getImageFilePath(), hist.getDataPoints());
  }

  /**
   * @return The list of DataPoints.
   */
  public ArrayList<HistogramDataPoint> getDataPoints() {
    return binnedDataPoints;
  }

  public void setDataPoints(ArrayList<HistogramDataPoint> dataPoints) {
    this.binnedDataPoints = dataPoints;
  }

  /**
   * Adds all datapoints in the list to the list of datapoints.
   * 
   * @param list
   *          A list of datapoints.
   */
  public void addDataPoints(List<HistogramDataPoint> list) {
    binnedDataPoints.addAll(list);
  }

  /**
   * Adds a datapoint to the list of binned datapoints.
   * 
   * @param datapoint
   *          A datapoint.
   */
  public void addDataPoint(HistogramDataPoint datapoint) {
    binnedDataPoints.add(datapoint);
  }

  /**
   * @param score
   *          A score >=0.
   * @return The absolute frequency of occurences of this score, or -1 if the
   *         score was not found in the list.
   */
  public int getAbsoluteFrequency(double score) {
    for (HistogramDataPoint p : binnedDataPoints) {
      if (Double.compare(p.getScore(), score) == 0) {
        return (int) p.getAbsFreq();
      }
    }
    return -1;
  }

  /**
   * @param score
   * @return
   */
  public double getRelativeFrequency(double score) {
    return (sitesScored == 0 || binnedDataPoints.isEmpty()) ? 0
        : getAbsoluteFrequency(score) / ((double) sitesScored);
  }

  /**
   * @return A list of scores that have been added to the histogram so far.
   */
  public ArrayList<Double> getAbsFreqScores() {
    ArrayList<Double> scores = new ArrayList<Double>();
    for (HistogramDataPoint p : binnedDataPoints) {
      scores.add(p.getScore());
    }
    Collections.sort(scores);
    return scores;
  }

  public Motif getMotif() {
    return motif;
  }

  public void setMotif(Motif motif) {
    this.motif = motif;
  }

  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Taxon getTaxon() {
    return taxon;
  }

  public void setTaxon(Taxon taxon) {
    this.taxon = taxon;
  }

  /**
   * @return The data's median score.
   */
  public double getMedian() {
    return (median != null) ? median : ScansiteAlgorithms
        .median(getAbsFreqScores());
  }

  /**
   * @return The median absolute deviation of the scores.
   */
  public double getMedianAbsDev() {
    return (medianAbsDev != null) ? medianAbsDev : ScansiteAlgorithms
        .medianAbsDev(getAbsFreqScores(), median);
  }

  public void setMedian(Double median) {
    this.median = median;
  }

  public void setMedianAbsDev(Double medianAbsDev) {
    this.medianAbsDev = medianAbsDev;
  }

  /**
   * @return The number of sites scored when creating this histogram.
   */
  public int getSitesScored() {
    return sitesScored;
  }

  /**
   * @param sitesScored
   *          The number of sites scored when creating this histogram.
   */
  public void setSitesScored(int sitesScored) {
    this.sitesScored = sitesScored;
  }

  /**
   * @return The number of proteins used for creating this histogram.
   */
  public int getProteinsScored() {
    return proteinsScored;
  }

  /**
   * @param proteinCount
   *          The number of proteins used for creating this histogram.
   */
  public void setProteinsScored(int proteinCount) {
    this.proteinsScored = proteinCount;
  }

  public Double getThresholdHigh() {
    return thresholdHigh;
  }

  public void setThresholdHigh(Double thresholdHigh) {
    this.thresholdHigh = thresholdHigh;
  }

  public Double getThresholdMedium() {
    return thresholdMedium;
  }

  public void setThresholdMedium(Double thresholdMedium) {
    this.thresholdMedium = thresholdMedium;
  }

  public Double getThresholdLow() {
    return thresholdLow;
  }

  public void setThresholdLow(Double thresholdLow) {
    this.thresholdLow = thresholdLow;
  }

  public String getImageFilePath() {
    return imageFilePath;
  }

  public void setImageFilePath(String imageFilePath) {
    this.imageFilePath = imageFilePath;
  }

  /**
   * @param percentile
   *          A percentile threshold.
   * @return The percentile's score
   */
  public double getScore(double percentile) {
    Collections.sort(binnedDataPoints);
    double sumAbsFreqs = sitesScored;
    double sumAbsFreq = 0;
    double pSum = 0;
    int lastIndex = 0;
    HistogramDataPoint p = null;
    for (int i = 0; i < binnedDataPoints.size() && pSum <= percentile; ++i) {
      p = binnedDataPoints.get(i);
      sumAbsFreq += p.getAbsFreq();
      pSum = sumAbsFreq / sumAbsFreqs;
      lastIndex = i;
    }
    double score = 0;
    if (p != null) {
      score = p.getScore();
      if (lastIndex - 1 >= 0 && pSum > percentile) {// interpolate linearly
        HistogramDataPoint pLast = binnedDataPoints.get(lastIndex - 1);
        double scoreDelta = Math.abs(p.getScore() - pLast.getScore());
        double pSumLast = (sumAbsFreq - p.getAbsFreq()) / sumAbsFreqs;
        double fracDeltaY = Math.abs((pSumLast - percentile)
            / (pSum - pSumLast));

        score = pLast.getScore() + fracDeltaY * scoreDelta;
      }
    }
    return score;
  }

  /**
   * @param score
   *          A score.
   * @return The percentile the score represents.
   */
  public double getPercentile(double score) {
    Collections.sort(binnedDataPoints);
    double perc = 0;
    double sum = 0;
    double sumLeft = 0;
    HistogramDataPoint limLeft = null;
    HistogramDataPoint limRight = null;
    for (int i = 0; i < binnedDataPoints.size(); ++i) {
      HistogramDataPoint p = binnedDataPoints.get(i);
      sum += p.getAbsFreq();
      if (p.getScore() <= score) {
        sumLeft += p.getAbsFreq();
        limLeft = p;
      }
      if (limRight == null && p.getScore() >= score) {
        limRight = p;
      }
    }
    if (limLeft != null && limRight != null) {
      double diff = (limRight.getScore() - limLeft.getScore());
      double frac = diff == 0 ? 0 : (score - limLeft.getScore()) / diff;
      double abs = sumLeft + frac * limRight.getAbsFreq();
      perc = sum != 0 ? abs / sum : 0;
    }
    return perc;
  }

  @Override
  public String toString() {
    if (motif != null && taxon != null && dataSource != null) {
      return motif.getShortName() + FilePaths.SPACER + taxon.getId()
          + FilePaths.SPACER + dataSource.getShortName();
    } else {
      return super.toString();
    }
  }
}
