package edu.mit.scansite.server.images.motifs;

import edu.mit.scansite.server.images.Colors;
import edu.mit.scansite.shared.transferobjects.AminoAcid;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 3/31/2017.
 *
 */
public enum GroupedAminoAcidColors {
    P (AminoAcid.P, Colors.GREEN_DARK),  // small

    D (AminoAcid.D, Colors.RED_DARK), // negative
    E (AminoAcid.E, Colors.RED_DARK), // negative

    H (AminoAcid.H, Colors.BLUE_DARK),  // positive
    K (AminoAcid.K, Colors.BLUE_DARK),  // positive
    O (AminoAcid.O, Colors.BLUE_DARK), // positive
    R (AminoAcid.R, Colors.BLUE_DARK), // positive

    G (AminoAcid.G, Colors.ORANGE),  // small
    A (AminoAcid.A, Colors.ORANGE),  // small
    V (AminoAcid.V, Colors.ORANGE), // aliphatic
    I (AminoAcid.I, Colors.ORANGE), // aliphatic
    L (AminoAcid.L, Colors.ORANGE), // aliphatic
    M (AminoAcid.M, Colors.ORANGE),
    F (AminoAcid.F, Colors.ORANGE), // aromatic
    W (AminoAcid.W, Colors.ORANGE), // aromatic

    S (AminoAcid.S, Colors.PURPLE), // small
    T (AminoAcid.T, Colors.PURPLE),  // small
    C (AminoAcid.C, Colors.PURPLE), // small
    U (AminoAcid.U, Colors.PURPLE),  // small
    N (AminoAcid.N, Colors.PURPLE),  // small
    Q (AminoAcid.Q, Colors.PURPLE),
    Y (AminoAcid.Y, Colors.PURPLE), // aromatic


    _N (AminoAcid._N, Colors.MARPLE),
    _C (AminoAcid._C, Colors.MARPLE),
    X (AminoAcid.X, Colors.BLACK),

    B (AminoAcid.B, Colors.BLACK),
    Z (AminoAcid.Z, Colors.BLACK),
    J (AminoAcid.J, Colors.BLACK);

    final AminoAcid aa;
    final Color color;

    GroupedAminoAcidColors(AminoAcid aa, Color color) {
        this.aa = aa;
        this.color = color;
    }

    /**
     * @return a mapping of all amino acids and their colors (including wildcard-amino acids).
     */
    public static Map<AminoAcid, Color> getAminoAcidColorMap() {
        Map<AminoAcid, Color> map = new HashMap<AminoAcid, Color>();
        for (GroupedAminoAcidColors gaac : GroupedAminoAcidColors.values()) {
            map.put(gaac.aa, gaac.color);
        }
        return map;
    }

    public static Color getCentralPositionColor() {
        return  Colors.BLACK;
    }

}
