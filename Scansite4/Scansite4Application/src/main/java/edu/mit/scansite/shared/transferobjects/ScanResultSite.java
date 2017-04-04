package edu.mit.scansite.shared.transferobjects;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import edu.mit.scansite.shared.ScansiteConstants;

/**
 * A datastructure for representing a scan hit.
 *
 * @author tobieh
 */
public class ScanResultSite implements IsSerializable {

    private Motif motif;
    private LightWeightProtein protein;
    private LightWeightLocalization motifLocalization;
    private DataSource localizationDataSource;

    private int position;
    private double score;
    private double percentile = 0;

    private double surfaceAccessValue = 1;
    private List<EvidenceResource> evidence;

    public ScanResultSite() {
    }

    public static final ProvidesKey<ScanResultSite> KEY_PROVIDER = new ProvidesKey<ScanResultSite>() {
        public Object getKey(ScanResultSite item) {
            return item == null ? null : item.getMotif().getShortName()
                    + item.getProtein().getIdentifier() + item.getSite()
                    + item.getScore();
        }
    };

    public ScanResultSite(Motif motif, LightWeightProtein protein,
                          Localization motifLocalization, DataSource localizationDataSource, int position, double score) {
        this.motif = motif;
        this.protein = protein;
        this.position = position;
        this.score = score;
    }

    public ScanResultSite(Motif motif, LightWeightProtein protein,
                          Localization motifLocalization, DataSource localizationDataSource, int position, double score,
                          double surfaceAccessValue, double percentile) {
        this(motif, protein, motifLocalization, localizationDataSource, position, score);
        this.percentile = percentile;
        this.surfaceAccessValue = surfaceAccessValue;
    }

    public Motif getMotif() {
        return motif;
    }

    public void setMotif(Motif motif) {
        this.motif = motif;
    }

    public LightWeightProtein getProtein() {
        return protein;
    }

    public void setProtein(LightWeightProtein protein) {
        this.protein = protein;
    }

    public LightWeightLocalization getMotifLocalization() {
        return motifLocalization;
    }

    public void setMotifLocalization(LightWeightLocalization motifLocalization) {
        this.motifLocalization = motifLocalization;
    }

    /**
     * @return The position of the site (0 <= position < sequenceLength)
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the sites position.
     *
     * @param po The position of the site (0 <= position < sequenceLength)
     */
    public void setPosition(int position) {
        this.position = position;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    public double getPercentile() {
        return percentile;
    }

    /**
     * @return The site, eg. S123 for a Serine site at position 123 in the
     * protein's sequence.
     */
    public String getSite() {
        return protein.getSequence().charAt(position)
                + String.valueOf(position + 1);
    }

    public double getSurfaceAccessValue() {
        return surfaceAccessValue;
    }

    public void setSurfaceAccessValue(double surfaceAccessValue) {
        this.surfaceAccessValue = surfaceAccessValue;
    }

    public DataSource getLocalizationDataSource() {
        return localizationDataSource;
    }

    public void setLocalizationDataSource(DataSource localizationDataSource) {
        this.localizationDataSource = localizationDataSource;
    }

    public String getSiteSequence() {
        String seq = protein.getSequence();
        int halfWindow = ScansiteConstants.HALF_WINDOW;
        int startIdx = position - halfWindow;
        int endIdx = position + halfWindow + 1;
        seq = seq.substring(Math.max(0, startIdx),
                Math.min(seq.length(), endIdx));
        if (seq.length() < ScansiteConstants.WINDOW_SIZE) {
            if (startIdx < 0) {
                for (int i = 0; i < Math.abs(startIdx)
                        && seq.length() < ScansiteConstants.WINDOW_SIZE; ++i) {
                    seq = ScansiteConstants.SEQ_PAD_CHAR_START + seq;
                }
            }
            int wholeSeqLen = seq.length();
            if (endIdx >= wholeSeqLen) {
                for (int i = 0; i < endIdx - wholeSeqLen
                        && seq.length() < ScansiteConstants.WINDOW_SIZE; ++i) {
                    seq = seq + ScansiteConstants.SEQ_PAD_CHAR_END;
                }
            }
        }
        List<Character> targets = new ArrayList<>();
        targets.add('s');
        targets.add('t');
        targets.add('y');
        targets.add('r');
        targets.add('k');
        targets.add('l');

        String pre = "<span style=\"color: darkorange\">";
        String after = "</span>";

        String corePre = "<span style=\"color: red\"><strong>";
        String coreAfter = "</strong></span>";

        int targetPos = halfWindow;

        List<Integer> modResidues = new ArrayList<>();
        for (int i = 0; i < seq.length(); i++) {
            for (Character target : targets) {
                if (seq.charAt(i) == target) {
                    modResidues.add(i);
                }
            }
        }
        for (int i = modResidues.size() - 1; i >= 0; i--) {
            int idx = modResidues.get(i);
            if (idx < halfWindow) {
                targetPos += pre.length() + after.length();
            }
            seq = seq.substring(0, idx) + pre + seq.charAt(idx) + after + seq.substring(idx + 1);
        }

        String hitChar = String.valueOf(seq.charAt(targetPos)).toLowerCase();
        seq = seq.substring(0, targetPos) + corePre + hitChar
                + coreAfter + seq.substring(targetPos + 1);

        return seq;
    }

    @Override
    public String toString() {
        return motif.getShortName() + "-" + String.valueOf(position) + "@"
                + protein.getIdentifier();
    }

    public List<EvidenceResource> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<EvidenceResource> evidence) {
        this.evidence = evidence;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((motif == null) ? 0 : motif.hashCode());
        result = prime * result + position;
        result = prime * result + ((protein == null) ? 0 : protein.hashCode());
        long temp;
        temp = Double.doubleToLongBits(score);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScanResultSite) {
            ScanResultSite other = (ScanResultSite) obj;
            if (other.getMotif() != null && this.motif != null
                    && other.getMotif().equals(motif)) {
                if (other.getPosition() == position
                        && other.getProtein().equals(protein)) {
                    if (Double.compare(other.getScore(), score) == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
