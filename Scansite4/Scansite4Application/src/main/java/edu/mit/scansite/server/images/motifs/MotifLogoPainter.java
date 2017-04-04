package edu.mit.scansite.server.images.motifs;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.mit.scansite.server.images.Colors;
import edu.mit.scansite.server.images.Painter;
import edu.mit.scansite.shared.ScansiteConstants;
import edu.mit.scansite.shared.transferobjects.AminoAcid;
import edu.mit.scansite.shared.transferobjects.Motif;
import edu.mit.scansite.shared.util.ScansiteAlgorithms;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifLogoPainter extends Painter {

    private Map<AminoAcid, Color> aaColors = AminoAcidColors
            .getAminoAcidColorMap();
    private Motif motif;

    private ArrayList<AminoAcid> singleAas = new ArrayList<AminoAcid>();
    private double[][] aminoAcidHeights;

    public MotifLogoPainter(Motif motif, boolean toggled) {
        this.motif = motif;
        List<Integer> centralPositions;

        bImg = new BufferedImage(MotifLogoConstants.IMAGE_WIDTH,
                MotifLogoConstants.IMAGE_HEIGHT, MotifLogoConstants.IMAGE_TYPE);
        image = bImg.createGraphics();
        image.setBackground(MotifLogoConstants.COLOR_BACKGROUND);
        image.clearRect(0, 0, MotifLogoConstants.IMAGE_WIDTH,
                MotifLogoConstants.IMAGE_HEIGHT);
        setColor(MotifLogoConstants.COLOR_DEFAULT);
        setStroke(MotifLogoConstants.DEFAULT_STROKE);
        init(toggled);
    }

    private void init(boolean toggled) {
        drawBaseLine();
        drawMotifName();
        prepareData();
        printAminoAcids(toggled);

    }

    private void printAminoAcids(boolean toggled) {
        double aaWidth = getAaWidth(ScansiteConstants.WINDOW_SIZE);
        int[] aaIdxs = new int[singleAas.size()];
        double[] values = new double[singleAas.size()];
        for (int i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
            for (int aa = 0; aa < singleAas.size(); ++aa) {
                aaIdxs[aa] = aa;
                values[aa] = aminoAcidHeights[aa][i];
            }
            aaIdxs = sort(aaIdxs, values);
            int nextHeight = 1;
            double xPrintOffset = MotifLogoConstants.SPACE_WIDTH * (1 + i)
                    + aaWidth * i;
            for (int aa = 0; aa < singleAas.size(); ++aa) { // todo: if toggled -- special treatment?
                // print amino acid
                boolean isCentralPosition = (i == (ScansiteConstants.WINDOW_SIZE-1)/2
                        && motif.getValue(singleAas.get(aaIdxs[aa]), i) > 0);

                image.setFont(MotifLogoConstants.FONT_AMINO_ACID);
                nextHeight = printAminoAcid(singleAas.get(aaIdxs[aa]),
                        aminoAcidHeights[aaIdxs[aa]][i], nextHeight, aaWidth,
                        xPrintOffset, image.getFontMetrics(), toggled, isCentralPosition);
            }

            // print position annotation
            image.setColor(Color.BLACK);
            int pos = i - ScansiteConstants.WINDOW_CENTER_INDEX;
            drawString((pos > 0 ? "+" : "") + String.valueOf(pos),
                    getXFromBaseLine(xPrintOffset + aaWidth / 2),
                    getYFromBaseLine(MotifLogoConstants.INDEX_OFFSET),
                    MotifLogoConstants.FONT_DEFAULT_BOLD, true);
        }
    }

    private double getAaWidth(int nPositions) {
        return (MotifLogoConstants.FULL_LOGO_WIDTH - (nPositions + 2)
                * MotifLogoConstants.SPACE_WIDTH)
                / ((double) nPositions);
    }

    private int printAminoAcid(AminoAcid aminoAcid, double aminoAcidHeight,
                               int currentHeight, double aminoAcidWidth, double xPrintOffset,
                               FontMetrics fm, boolean toggled, boolean isCentralPosition) {
        if (aminoAcidHeight >= 1) {
            String s = Character.toString(aminoAcid.getOneLetterCode());
            s = s.equals("*") ? "!" : s;
            FontRenderContext frc = image.getFontRenderContext();
            TextLayout tl = new TextLayout(s, image.getFont(), frc);
            AffineTransform transform = new AffineTransform();
            transform.setToTranslation(getXFromBaseLine(xPrintOffset),
                    getYFromBaseLine(currentHeight));
            double scaleY = aminoAcidHeight
                    / (tl.getOutline(null).getBounds().getMaxY() - tl
                    .getOutline(null).getBounds().getMinY());
            transform
                    .scale(aminoAcidWidth / (double) fm.stringWidth(s), scaleY);
            Shape shape = tl.getOutline(transform);
            aminoAcidHeight = shape.getBounds().getMaxY()
                    - shape.getBounds().getMinY();
            image.setClip(shape);
            if (toggled) {
                aaColors = GroupedAminoAcidColors.getAminoAcidColorMap();
            }
            if (/*toggled &&*/ isCentralPosition) {
                image.setColor(GroupedAminoAcidColors.getCentralPositionColor());
            } else {
                image.setColor(aaColors.get(aminoAcid));
            }
            image.fill(shape.getBounds());
            image.setClip(null);
            currentHeight += (int) aminoAcidHeight;
        }
        return currentHeight;
    }

    private int[] sort(int[] toSort, double[] basedOn) {
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < basedOn.length - 1; i++) {
                if (basedOn[i] > basedOn[i + 1]) {
                    double temp = basedOn[i];
                    basedOn[i] = basedOn[i + 1];
                    basedOn[i + 1] = temp;
                    int tempTwo = toSort[i];
                    toSort[i] = toSort[i + 1];
                    toSort[i + 1] = tempTwo;
                    swapped = true;
                }
            }
        }
        return toSort;
    }

    private void prepareData() {
        int i = 0;
        for (AminoAcid aa : motif.getAminoAcidArray()) {
            if (aa.isSingleAa()) {
                boolean displayAminoAcid = true;
                if (aa.isModifiedAa()) {
                    displayAminoAcid = modValueDiffersFromOriginal(aa);
                }
                if (displayAminoAcid) {
                    singleAas.add(aa);
                }
            }
        }
        aminoAcidHeights = new double[singleAas.size()][ScansiteConstants.WINDOW_SIZE];
        double[] rowSums = new double[ScansiteConstants.WINDOW_SIZE];
        double[] rowSumTemp = new double[ScansiteConstants.WINDOW_SIZE];
        double[][] freqData = new double[singleAas.size()][ScansiteConstants.WINDOW_SIZE];

        // init
        for (i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
            rowSums[i] = 0;
            rowSumTemp[i] = 0;
        }
        // calc sums
        for (int aa = 0; aa < singleAas.size(); ++aa) {
            for (i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
                rowSums[i] += motif.getValue(singleAas.get(aa), i);
            }
        }

        // calculate: -H(i) = sum(freq(aa,i) * log2(freq(aa,i))
        for (int aa = 0; aa < singleAas.size(); ++aa) {
            for (i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
                double mVal = motif.getValue(singleAas.get(aa), i);
                freqData[aa][i] = rowSums[i] == 0 ? 0 : mVal / rowSums[i];
                aminoAcidHeights[aa][i] = (freqData[aa][i] <= 0) ? 0
                        : freqData[aa][i]
                        * ScansiteAlgorithms.log2(freqData[aa][i]);
                rowSumTemp[i] += aminoAcidHeights[aa][i]; // H(i)
            }
        }

        // calculate: R(i) = log2(N_AA) - (-H(i)) = log2(N_AA) + H(i)
        double log2OfNAminoAcids = ScansiteAlgorithms.log2(singleAas.size());
        double maxR = 0;
        for (i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
            rowSums[i] = log2OfNAminoAcids + rowSumTemp[i]; // R(i)
            if (rowSums[i] > maxR) {
                maxR = rowSums[i];
            }
        }

        // calculate aa heights
        for (i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
            double iHeight = (rowSums[i] == 0) ? 0 : rowSums[i] / maxR
                    * (double) MotifLogoConstants.FULL_LOGO_HEIGHT; // scale
            // position-height
            // to
            // logo-height
            for (int aa = 0; aa < singleAas.size(); ++aa) {
                aminoAcidHeights[aa][i] = (freqData[aa][i] * iHeight);
            }
        }

        // printM(aminoAcidHeights);
    }

    private boolean modValueDiffersFromOriginal(AminoAcid aa) {
        AminoAcid nonModified = aa.getNonModifiedResidue();
        for (int i=0; i < ScansiteConstants.WINDOW_SIZE; i++) {
            if (motif.getValue(aa.getOneLetterCode(), i)
                    != motif.getValue(nonModified.getOneLetterCode(), i)) {
                return true;
            }
        }

        return false;
    }
    // private void printM(double[][] m) {
    // for (int aa = 0; aa < singleAas.size(); ++aa) {
    // System.out.print(singleAas.get(aa).getThreeLetterCode() + "\t");
    // }
    // System.out.println("");
    // for (int aa = 0; aa < singleAas.size(); ++aa) {
    // for (int i = 0; i < ScansiteConstants.WINDOW_SIZE; ++i) {
    // System.out.print(m[aa][i] + "\t");
    // }
    // System.out.println("");
    // }
    // System.out.println("");
    // }

    private void drawBaseLine() {
        setColor(Colors.BLACK);
        setStroke(MotifLogoConstants.BASELINE_STROKE);
        image.draw(getLine(
                getPoint(getXFromBaseLine(0), getYFromBaseLine(0)),
                getPoint(getXFromBaseLine(MotifLogoConstants.FULL_LOGO_WIDTH),
                        getYFromBaseLine(0))));

        drawString(MotifLogoConstants.TEXT_N_TERMINUS,
                getXFromBaseLine(MotifLogoConstants.N_TERM_OFFSET_X),
                getYFromBaseLine(MotifLogoConstants.N_TERM_OFFSET_Y),
                MotifLogoConstants.FONT_DEFAULT_BOLD, true);
        drawString(MotifLogoConstants.TEXT_C_TERMINUS,
                getXFromBaseLine(MotifLogoConstants.C_TERM_OFFSET_X),
                getYFromBaseLine(MotifLogoConstants.C_TERM_OFFSET_Y),
                MotifLogoConstants.FONT_DEFAULT_BOLD, true);
    }

    private void drawMotifName() {
        if (motif != null && motif.getDisplayName() != null
                && !motif.getDisplayName().isEmpty()) {
            drawString(
                    motif.getDisplayName(),
                    (double) getXFromBaseLine(MotifLogoConstants.TITLE_OFFSET_X),
                    (double) getYFromBaseLine(MotifLogoConstants.TITLE_OFFSET_Y),
                    MotifLogoConstants.FONT_DEFAULT_BOLD, false);
        }
    }

    /**
     * Prints a string in the given Font centered with the top to the given
     * coordinates.
     *
     * @param s      The string.
     * @param x      The X coordinate.
     * @param y      The Y coordinate.
     * @param center String is centered with top border to given X/Y if TRUE,
     *               otherwise left aligned to given X/Y.
     * @param font   The string's font.
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
            int xx = (int) x;
            if (center) {
                if (i == 0) {
                    xx = xx - textWidth / 2;
                    y = y + lineHeight;
                } else {
                    xx = xx - textWidth / 2;
                }
            }
            image.drawString(line, xx, (int) y + i * lineHeight);
        }
    }

    /**
     * @param x x coordinate relative to baseline.
     * @return x coordinate from bottom left of baseline.
     */
    protected int getXFromBaseLine(double x) {
        return getX(MotifLogoConstants.BASELINE_OFFSET_W
                + MotifLogoConstants.TERMINAL_WIDTH + x);
    }

    /**
     * @param y y coordinate relative to baseline.
     * @return y coordinate from baseline.
     */
    protected int getYFromBaseLine(double y) {
        return getY(MotifLogoConstants.BASELINE_OFFSET_S + y);
    }

    protected int getX(double x) {
        return (int) x;
    }

    protected int getY(double y) {
        return (int) (MotifLogoConstants.IMAGE_HEIGHT - y);
    }

    /**
     * @return The histogram as BufferedImage.
     */
    public BufferedImage getBufferedImage() {
        return bImg;
    }

}
