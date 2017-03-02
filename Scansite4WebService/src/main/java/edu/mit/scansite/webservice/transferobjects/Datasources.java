package edu.mit.scansite.webservice.transferobjects;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Datasources implements Serializable {
  private static final long serialVersionUID = 3317284759277045902L;
  private Datasource[] datasource;
  
  public Datasources() {
  }

  public Datasources(Datasource[] datasource) {
    super();
    this.datasource = datasource;
  }

  public Datasource[] getDatasource() {
    return datasource;
  }
  
  public void setDatasource(Datasource[] datasource) {
    this.datasource = datasource;
  }
}
