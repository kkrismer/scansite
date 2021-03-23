package edu.mit.scansite.shared.dispatch.motif;

import java.util.List;

import edu.mit.scansite.shared.transferobjects.Identifier;
import edu.mit.scansite.shared.transferobjects.LightWeightMotif;
import edu.mit.scansite.shared.transferobjects.LightWeightMotifGroup;
import edu.mit.scansite.shared.transferobjects.MotifClass;
import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class MotifGetResult implements Result {
    private boolean isSuccess = true;
    private String errorMessage = "";

    private String motifLogoUrl;
    private String toggleMotifLogoUrl;
    private LightWeightMotif motif;
    private List<Identifier> identifiers;
    private LightWeightMotifGroup motifGroup;
    private MotifClass motifClass = MotifClass.MAMMALIAN;

    public MotifGetResult() {
    }

    public MotifGetResult(String errorMessage) {
        isSuccess = false;
        this.errorMessage = errorMessage;
    }

    public MotifGetResult(LightWeightMotif motif, String motifLogoUrl, String toggleMotifLogoUrl,
                          List<Identifier> identifiers, LightWeightMotifGroup motifGroup,
                          MotifClass motifClass) {
        this.motif = motif;
        this.motifLogoUrl = motifLogoUrl;
        this.toggleMotifLogoUrl = toggleMotifLogoUrl;
        this.identifiers = identifiers;
        this.motifGroup = motifGroup;
        this.motifClass = motifClass;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMotifLogoUrl() {
        return motifLogoUrl;
    }

    public void setMotifLogoUrl(String motifLogoUrl) {
        this.motifLogoUrl = motifLogoUrl;
    }

    public String getToggleMotifLogoUrl() {
        return toggleMotifLogoUrl;
    }

    public void setToggleMotifLogoUrl(String toggleMotifLogoUrl) {
        this.toggleMotifLogoUrl = toggleMotifLogoUrl;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public LightWeightMotifGroup getMotifGroup() {
        return motifGroup;
    }

    public void setMotifGroup(LightWeightMotifGroup motifGroup) {
        this.motifGroup = motifGroup;
    }

    public void setMotifClass(MotifClass motifClass) {
        this.motifClass = motifClass;
    }

    public MotifClass getMotifClass() {
        return motifClass;
    }

    public LightWeightMotif getMotif() {
        return motif;
    }

    public void setMotif(LightWeightMotif motif) {
        this.motif = motif;
    }
}
