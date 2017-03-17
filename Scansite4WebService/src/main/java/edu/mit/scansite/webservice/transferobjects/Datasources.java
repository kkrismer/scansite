package edu.mit.scansite.webservice.transferobjects;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Datasources implements Serializable {
  private static final long serialVersionUID = 3317284759277045902L;
  private DataSource[] dataSource;
  
  public Datasources() {
  }

  public Datasources(DataSource[] dataSource) {
    super();
    this.dataSource = dataSource;
  }

  public DataSource[] getDataSource() {
    return dataSource;
  }
  
  public void setDataSource(DataSource[] dataSource) {
    this.dataSource = dataSource;
  }
}
