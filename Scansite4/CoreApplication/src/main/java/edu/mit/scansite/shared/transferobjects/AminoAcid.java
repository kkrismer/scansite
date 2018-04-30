package edu.mit.scansite.shared.transferobjects;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public enum AminoAcid implements IsSerializable {
    A("Alanine",        "Ala", 'A', 71.09310, 0.49, true),
    R("Arginine",       "Arg", 'R', 156.2018, 0.95, true),
    N("Asparagine",     "Asn", 'N', 114.1181, 0.81, true),
    D("Aspartic acid",  "Asp", 'D', 115.1029, 0.81, true),
    C("Cysteine",       "Cys", 'C', 103.1530, 0.26, true),
    E("Glutamic acid",  "Glu", 'E', 129.1298, 0.84, true),
    Q("Glutamine",      "Gln", 'Q', 128.1450, 0.84, true),
    G("Glycine",        "Gly", 'G', 57.06615, 0.48, true),
    H("Histidine",      "His", 'H', 137.1554, 0.66, true),
    I("Isoleucine",     "Ile", 'I', 113.1737, 0.34, true),
    L("Leucine",        "Leu", 'L', 113.1737, 0.40, true),
    K("Lysine",         "Lys", 'K', 128.1884, 0.97, true),
    M("Methionine",     "Met", 'M', 131.2069, 0.48, true),
    F("Phenylalanine",  "Phe", 'F', 147.1909, 0.42, true),
    P("Proline",        "Pro", 'P', 97.13095, 0.75, true),
    S("Serine",         "Ser", 'S', 87.09245, 0.65, true),
    T("Threonine",      "Thr", 'T', 101.1194, 0.70, true),
    W("Tryptophan",     "Trp", 'W', 186.2275, 0.51, true),
    Y("Tyrosine",       "Tyr", 'Y', 163.1903, 0.76, true),
    V("Valine",         "Val", 'V', 99.14685, 0.36, true),
    U("Selenocysteine", "Sec", 'U', 150.0490, 0.26, true),
    O("Pyrrolysine",    "Pyl", 'O', 237.3090, 0.97, true), // not in scansite2

    pS("Phospho Serine",         "pSer", 's', 167.0732, 0.65, true), // phosphorylation +79.9799 //values from unimod.org
    pT("Phospho Threonine",      "pThr", 't', 181.0993, 0.70, true), // phosphorylation +79.9799
    pY("Phospho Tyrosine",       "pTyr", 'y', 243.1702, 0.76, true), // phosphorylation +79.9799
    mR("Methylated Arginine",    "mArg", 'r', 170.2284, 0.95, true), // methylation +14.0266
    aK("Acetylated Lysine",      "aLys", 'k', 170.2251, 0.97, true), // acetylation +42.0367
    mK("Methylated Lysine",      "mLys", 'l', 129.1295, 0.81, true), // methylation +14.0266

    _N("N Terminus", "$N$", '$', 15.9994, 1.00, true),
    _C("C Terminus", "*C*", '*', 2.00159, 1.00, true),
    X("Any", "Xaa", 'X', 125.7303411, 0.62, false), // mw is mean of all

    B("Asparagine or aspartic acid", "Asx", 'B', 114.6105, 0.81, false), // mw is mean of N and D
    Z("Glutamine or glutamic acid", "Glx", 'Z', 128.6374, 0.84, false), // mw is mean of Q and E
    J("Leucine or Isoleucine", "Xle", 'J', 113.1737, 0.37, false); // not in scansite2 // mw is mean of I and L

    final char oneLetterCode;
    final String threeLetterCode;
    final String fullName;
    final boolean isSingleAa;
    final double mw;
    final double surfaceAccessValue;

    /**
     * @param fullName          The amino acid's full name.
     * @param threeLetterCode   It's IUPAC 3-letter-code.
     * @param oneLetterCode     It's IUPAC 1-letter-code.
     * @param mw                It's molecular weight (when in bonding!!!).
     * @param sa                It's surface access factor.
     * @param isSingleAminoAcid TRUE, if it is a single amino acid, FALSE if it is a wildcard.
     */
    AminoAcid(String fullName, String threeLetterCode, char oneLetterCode, double mw, double sa, boolean isSingleAminoAcid) {
        this.oneLetterCode = oneLetterCode;
        this.threeLetterCode = threeLetterCode;
        this.fullName = fullName;
        this.mw = mw;
        this.surfaceAccessValue = sa;
        this.isSingleAa = isSingleAminoAcid;
    }

    public static boolean isAa(char oneLetterCode) {
        return (oneLetterCode == 'A' || oneLetterCode == 'R' || oneLetterCode == 'N' || oneLetterCode == 'D'
                || oneLetterCode == 'C' || oneLetterCode == 'E' || oneLetterCode == 'Q' || oneLetterCode == 'G'
                || oneLetterCode == 'H' || oneLetterCode == 'I' || oneLetterCode == 'L' || oneLetterCode == 'K'
                || oneLetterCode == 'M' || oneLetterCode == 'F' || oneLetterCode == 'P' || oneLetterCode == 'S'
                || oneLetterCode == 'T' || oneLetterCode == 'W' || oneLetterCode == 'Y' || oneLetterCode == 'V'
                || oneLetterCode == 'U' || oneLetterCode == 'O' || oneLetterCode == '$' || oneLetterCode == '*'
                || oneLetterCode == 'X' || oneLetterCode == 'B' || oneLetterCode == 'Z' || oneLetterCode == 'J'
                || oneLetterCode == 's' || oneLetterCode == 't' || oneLetterCode == 'y' || oneLetterCode == 'r'
                || oneLetterCode == 'k' || oneLetterCode == 'd' || oneLetterCode == 'h');
    }

    @Override
    public String toString() {
        return String.valueOf(oneLetterCode);
    }

    /**
     * @return The amino acid's molecular weight.
     */
    public double getMolecularWeight() {
        return mw;
    }

    /**
     * @return The aa's full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @return The aa's three-letter-code.
     */
    public String getThreeLetterCode() {
        return threeLetterCode;
    }

    /**
     * @return The aa's one-letter-code.
     */
    public char getOneLetterCode() {
        return oneLetterCode;
    }

    /**
     * @return TRUE, if the amino acid is not a RegEx-AminoAcid, ie, an amino acid object that describes
     * only one amino acid.
     */
    public boolean isSingleAa() {
        return isSingleAa;
    }

    public boolean isModifiedAa() {
        if (this.getOneLetterCode() == pS.getOneLetterCode()) {
            return true;
        } else if (this.getOneLetterCode() == pT.getOneLetterCode()) {
            return true;
        } else if (this.getOneLetterCode() == pY.getOneLetterCode()) {
            return true;
        } else if (this.getOneLetterCode() == mR.getOneLetterCode()) {
            return true;
        } else if (this.getOneLetterCode() == aK.getOneLetterCode()) {
            return true;
        } else if (this.getOneLetterCode() == mK.getOneLetterCode()) {
            return true;
        }
        return false;
    }


    public AminoAcid getNonModifiedResidue() {
        if (this.getOneLetterCode() == pS.getOneLetterCode()) {
            return S;
        } else if (this.getOneLetterCode() == pT.getOneLetterCode()) {
            return T;
        } else if (this.getOneLetterCode() == pY.getOneLetterCode()) {
            return Y;
        } else if (this.getOneLetterCode() == mR.getOneLetterCode()) {
            return R;
        } else if (this.getOneLetterCode() == aK.getOneLetterCode()) {
            return K;
        } else if (this.getOneLetterCode() == mK.getOneLetterCode()) {
            return K;
        }
        return null;
    }

    /**
     * @param aa An AminoAcid.
     * @return TRUE, if the given AminoAcid is one that is ignored for scoring sequences/sites.
     */
    public static boolean isIgnoredForScoring(AminoAcid aa) {
        return aa == B || aa == Z || aa == X || aa == U || aa == O || aa == J;
    }

    /**
     * @param aa
     * @return TRUE if the given AA is a terminal AminoAcid-Symbol (* or $).
     */
    public static boolean isTerminal(AminoAcid aa) {
        return aa == _C || aa == _N;
    }

    /**
     * @param s A string that describes an Amino Acid. This can either be a full name, a three- or a one-letter code.
     * @return An AminoAcid object, or NULL, if no AminoAcid was found.
     */
    public static AminoAcid getValue(String s) {
        for (AminoAcid aa : AminoAcid.values()) {
            if (s.equals(aa.getFullName()) || s.equals(aa.getThreeLetterCode()) || s.equals(String.valueOf(aa.getOneLetterCode()))) {
                return aa;
            }
            if (s.equals("pT")) {
                return AminoAcid.pT;
            } else if (s.equals("pS")) {
                return AminoAcid.pS;
            } else if (s.equals("pY")) {
                return AminoAcid.pY;
            } else if (s.equals("mR")) {
                return AminoAcid.mR;
            } else if (s.equals("aK")) {
                return AminoAcid.aK;
            } else if (s.equals("mK")) {
                return AminoAcid.mK;
            }
        }
        return null;
    }

    /**
     * @return A map that maps the amino acid's one letter code to the actual AminoAcid object.
     */
    public static Map<Character, AminoAcid> getOneLetterCodeMap() {
        HashMap<Character, AminoAcid> map = new HashMap<Character, AminoAcid>();
        for (AminoAcid aa : AminoAcid.values()) {
            map.put(aa.getOneLetterCode(), aa);
        }
        return map;
    }

    /**
     * Gets the queried amino acid. If many amino acids have to be accessed by their one letter code, use
     * the map from getOneLetterMap instead of this method (performance!).
     *
     * @param oneLetterCode The amino acid's one letter code.
     * @return The AminoAcid that is described by the given OneLetterCode, or null if no such AA exists.
     */
    public static AminoAcid getValue(char oneLetterCode) {
        for (AminoAcid aa : AminoAcid.values()) {
            if (oneLetterCode == aa.getOneLetterCode()) {
                return aa;
            }
        }
        return null;
    }

    /**
     * @return The amino acid's surface accessibility factor.
     */
    public double getSurfaceAccessValue() {
        return surfaceAccessValue;
    }
}
