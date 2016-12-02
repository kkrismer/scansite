package edu.mit.scansite.server.images;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Tobieh
 */
public abstract class Painter {

	protected BufferedImage bImg = null;
	protected Graphics2D image = null;

	public Painter() {
	}

	public abstract BufferedImage getBufferedImage();

	protected abstract int getY(double y);

	protected abstract int getX(double x);

	public Painter(BufferedImage img) {
		bImg = img;
		image = bImg.createGraphics();
	}

	/**
	 * @param stroke
	 *            The stroke to be set.
	 */
	protected void setStroke(Stroke stroke) {
		if (stroke == null) {
			stroke = new BasicStroke(1);
		}
		image.setStroke(stroke);
	}

	/**
	 * @param color
	 *            The colour to be set.
	 */
	protected void setColor(Color color) {
		if (color == null) {
			color = Colors.BLACK;
		}
		image.setColor(color);
	}

	/**
	 * Prints a string in the given Font centered with the top to the given
	 * coordinates.
	 * 
	 * @param s
	 *            The string.
	 * @param x
	 *            The X coordinate.
	 * @param y
	 *            The Y coordinate.
	 * @param center
	 *            String is centered with top border to given X/Y if TRUE,
	 *            otherwise left aligned to given X/Y.
	 * @param font
	 *            The string's font.
	 */
	protected void drawString(String s, double x, double y, Font font,
			boolean center) {
		image.setFont(font);
		String lines[] = s.split("\n");
		for (int i = 0; i < lines.length; ++i) {
			String line = lines[i];
			Rectangle2D rect = getStringBoundsRect(line, font);

			int textHeight = (int) (rect.getHeight());
			int textWidth = (int) (rect.getWidth());
			int lineHeight = textHeight / 2 + getFontHeight(font);
			double xx = x;
			if (center) {
				if (i == 0) {
					xx = x - textWidth / 2.0;
					y = y - lineHeight;
				} else {
					xx = x - textWidth / 2.0;
				}
			}
			image.drawString(line, getX(xx), getY(y - i * lineHeight));
		}
	}

	/**
	 * @param font
	 *            The font.
	 * @return The height of the given font.
	 */
	protected int getFontHeight(Font font) {
		return image.getFontMetrics(font).getAscent();
	}

	/**
	 * @param font
	 *            The font.
	 * @return The width of an 'M' using the given font.
	 */
	protected int getFontWidth(Font font) {
		return image.getFontMetrics(font).charWidth('M');
	}

	/**
	 * @param s
	 *            A string.
	 * @param f
	 *            The font of the string.
	 * @return The rectangle that borders the given String s from the outside.
	 */
	protected Rectangle2D getStringBoundsRect(String s, Font f) {
		String lines[] = s.split("\n");
		double width = 0;
		double height = 0;
		for (String line : lines) {
			FontMetrics fm = image.getFontMetrics(f);
			Rectangle2D rect = fm.getStringBounds(line, image);
			if (width < rect.getWidth()) {
				width = rect.getWidth();
			}
			height += rect.getHeight();
		}
		return new Rectangle2D.Double(0, 0, width, height);
	}

	/**
	 * @param from
	 *            The start-point of the line.
	 * @param to
	 *            The end-point of the line.
	 * @return A line from FROM to TO.
	 */
	protected Line2D.Double getLine(Point2D.Double from, Point2D.Double to) {
		return new Line2D.Double(from, to);
	}

	/**
	 * @param x
	 *            X-Coordinate.
	 * @param y
	 *            Y-Coordinate.
	 * @return A Point2D Object with the given coordinates.
	 */
	protected Point2D.Double getPoint(double x, double y) {
		return new Point2D.Double(x, y);
	}

	/**
	 * Crop image to the given size.
	 */
	protected void cropImage(int width, int height) {
		bImg = bImg.getSubimage(0, getY(height), width, height - 1);
		image = bImg.createGraphics();
	}

}
