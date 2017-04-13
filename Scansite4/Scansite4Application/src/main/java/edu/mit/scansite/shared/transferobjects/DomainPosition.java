package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A class that saves a domain's position within a sequence.
 * @author tobieh
 */
public class DomainPosition implements IsSerializable, Comparable<DomainPosition> {
  
  private int from;
  private int to;
  private String name;
  private String method;
  private String domainId;
  private String IPRCode;
  private String alternativeName;
  private int colorR = -1;
  private int colorG = -1;
  private int colorB = -1;
  
  public DomainPosition() {
  }
  
  public DomainPosition(int from, int to, String name, String method,
                        String id, String IPRCode, String alternativeName) {
    this.from = from;
    this.to = to;
    this.name = name;
    this.method = method;
    this.domainId = id;
    this.IPRCode = IPRCode;
    this.alternativeName = alternativeName;
  }
  
  public int getFrom() {
    return from;
  }
  
  public void setFrom(int from) {
    this.from = from;
  }
  
  public int getTo() {
    return to;
  }
  
  public void setTo(int to) {
    this.to = to;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }
  
  public String getDomainId() {
    return domainId;
  }
  
  public void setDomainId(String domainId) {
    this.domainId = domainId;
  }

  public String getIPRCode() {
    return IPRCode;
  }

  public void setIPRCode(String IPRCode) {
    this.IPRCode = IPRCode;
  }

  public String getAlternativeName() {
    return alternativeName;
  }

  public void setAlternativeName(String alternativeName) {
    this.alternativeName = alternativeName;
  }

  public int getColorR() {
    return colorR;
  }

  public void setColorR(int colorR) {
    this.colorR = colorR;
  }

  public int getColorG() {
    return colorG;
  }

  public void setColorG(int colorG) {
    this.colorG = colorG;
  }

  public int getColorB() {
    return colorB;
  }

  public void setColorB(int colorB) {
    this.colorB = colorB;
  }

  @Override
  public int compareTo(DomainPosition o) {
    return (o != null) ? Integer.valueOf(from).compareTo(Integer.valueOf(o.getFrom())) : 1;
  }

  public boolean isColorSet() {
    return colorR >= 0 && colorG >= 0 & colorB >= 0;
  }
}
