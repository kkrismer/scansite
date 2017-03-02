package edu.mit.scansite.webservice.transferobjects;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MotifDefinition implements Serializable {
  private static final long serialVersionUID = -2150800498753234736L;
  
  private String motifGroupName;
  private String motifName;
  private String motifNickName;
  private String motifClass;
  
  public MotifDefinition() {
  }

  public MotifDefinition(String motifGroupName, String motifName, String motifNickName, String motifClass) {
    super();
    this.motifGroupName = motifGroupName;
    this.motifName = motifName;
    this.motifNickName = motifNickName;
    this.motifClass = motifClass;
  }

  public String getMotifGroupName() {
    return motifGroupName;
  }

  public void setMotifGroupName(String motifGroupName) {
    this.motifGroupName = motifGroupName;
  }

  public String getMotifName() {
    return motifName;
  }

  public void setMotifName(String motifName) {
    this.motifName = motifName;
  }

  public String getMotifNickName() {
    return motifNickName;
  }

  public void setMotifNickName(String motifNickName) {
    this.motifNickName = motifNickName;
  }
  
  public String getMotifClass() {
    return motifClass;
  }
  
  public void setMotifClass(String motifClass) {
    this.motifClass = motifClass;
  }
}
