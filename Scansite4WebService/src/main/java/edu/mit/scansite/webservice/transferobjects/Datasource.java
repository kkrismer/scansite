package edu.mit.scansite.webservice.transferobjects;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Datasource implements Serializable {
  private static final long serialVersionUID = 2379785356802199930L;

  private String name;
  private String nickName;
  private String version;
  private String description;
  private Integer numberOfProteinEntries;
  
  public Datasource() {
  }
  
  public Datasource(String name, String nickName, String version, String description, Integer numberOfProteinEntries) {
    this.name = name;
    this.nickName = nickName;
    this.version = version;
    this.description = description;
    this.setNumberOfProteinEntries(numberOfProteinEntries);
  }

  public String getName() {
    return name;
  }
  
  public String getNickName() {
    return nickName;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getNumberOfProteinEntries() {
    return numberOfProteinEntries;
  }

  public void setNumberOfProteinEntries(Integer numberOfProteinEntries) {
    this.numberOfProteinEntries = numberOfProteinEntries;
  }
}
