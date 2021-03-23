package edu.mit.scansite.server.images.histograms;

import java.awt.image.BufferedImage;

import edu.mit.scansite.server.dataaccess.file.ImageInOut;
import edu.mit.scansite.shared.transferobjects.DataSource;
import edu.mit.scansite.shared.transferobjects.DatabaseSearchScanResultSite;
import edu.mit.scansite.shared.transferobjects.Histogram;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.transferobjects.ScanResultSite;
import edu.mit.scansite.shared.transferobjects.Taxon;

/**
 * A data structure for representing, painting and editing histograms on the
 * serverside.
 * 
 * @author tobieh
 */
public class ServerHistogram extends Histogram {

	private BufferedImage plot;

	public ServerHistogram() {
	}

	public ServerHistogram(Motif motif, DataSource dataSource, Taxon taxon,
			int sitesScored, int proteinsScored, BufferedImage plot) {
		super(motif, dataSource, taxon);
		setSitesScored(sitesScored);
		setProteinsScored(proteinsScored);
		this.plot = plot;
	}

	public ServerHistogram(Histogram hist) {
		super(hist);
		if (hist.getImageFilePath() != null) {
			ImageInOut imgIO = new ImageInOut();
			plot = imgIO.getImage(hist.getImageFilePath());
		}
	}

	public ServerHistogram(Motif motif, DataSource dataSource, Taxon taxon) {
		super(motif, dataSource, taxon);
	}

	/**
	 * @return The histogram as an Image.
	 */
	public BufferedImage getReferenceHistogramPlot(ScanResultSite site) {
		HistogramDrawer painter = new HistogramDrawer(this);
		plot = painter.getReferenceHistogramPlot(site);
		return plot;
	}

	/**
	 * @return The histogram as an Image.
	 */
	public BufferedImage getDbSearchReferenceHistogramPlot(
			String histogramBasePath, DatabaseSearchScanResultSite site) {
		if (histogramBasePath != null) {
		    ImageInOut imgIO = new ImageInOut();
			plot = imgIO.getImage(histogramBasePath);
			HistogramDrawer painter = new HistogramDrawer(this);
			plot = painter.getReferenceHistogramPlot(site);
			return plot;
		} else {
			return null;
		}
	}

	/**
	 * @return The histogram as an Image.
	 */
	public BufferedImage getDbEditHistogramPlot() {
		HistogramDrawer painter = new HistogramDrawer(this);
		plot = painter.getDbEditHistogramPlot();
		return plot;
	}

	/**
	 * @return The histogram as an Image.
	 */
	public BufferedImage getDbHistogramPlot() {
		HistogramDrawer painter = new HistogramDrawer(this);
		plot = painter.getDbHistogramPlot();
		return plot;
	}

	/**
	 * @return This histogram as client-histogram.
	 */
	public Histogram toClientHistogram() {
		return new Histogram(motif, dataSource, taxon, thresholdHigh,
				thresholdMedium, thresholdLow, getMedian(), getMedianAbsDev(),
				sitesScored, proteinsScored, imageFilePath, getDataPoints());
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public boolean hasImage() {
		return plot != null;
	}

	public BufferedImage getPlot() {
		return plot;
	}

	public void setPlot(BufferedImage plot) {
		this.plot = plot;
	}
}
