package edu.mit.scansite.server.dataaccess.file;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.scansite.server.images.histograms.HistogramConstants;
import edu.mit.scansite.shared.DataAccessException;
import edu.mit.scansite.shared.ScansiteConstants;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class ImageInOut {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageInOut.class);

	public void saveImage(BufferedImage img, String filePath)
			throws DataAccessException {
		try {
			DirectoryManagement.prepareDirectory(filePath, true);
			File out = new File(filePath);
			ImageIO.write(img, ScansiteConstants.IMAGE_TYPE, out);
		} catch (Exception e) {
			logger.error(e.getMessage() + " (file: " + filePath + ")", e);
			throw new DataAccessException(e.getMessage());
		}
	}

	public BufferedImage getImage(String imageFilePath) {
		try {
			Image image = ImageIO.read(new File(imageFilePath));
			BufferedImage bImg = new BufferedImage(image.getWidth(null),
					image.getHeight(null), HistogramConstants.IMAGE_TYPE);
			Graphics2D g = bImg.createGraphics();
			g.drawImage(image, null, null);
			return bImg;
		} catch (IOException e) {
			logger.error(e.getMessage() + " (file: " + imageFilePath + ")", e);
			return null;
		}
	}

}
