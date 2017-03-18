package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tobieh
 */
public enum MotifClass implements IsSerializable {
  MAMMALIAN ("Mammalian", "MAMMALIAN"),
  YEAST ("Yeast", "YEAST"),
  OTHER ("Other", "OTHER");

  private String name;
  private String databaseEntry;

  MotifClass (String name, String databaseEntry) {
    this.name = name;
    this.databaseEntry = databaseEntry;
  }

  public String getDatabaseEntry() {
    return databaseEntry;
  }
  
  public String getName() {
    return name;
  }

  public static MotifClass getDbValue(String classAsString) {
    if (classAsString != null) {
      if (MAMMALIAN.getDatabaseEntry().equalsIgnoreCase(classAsString)) {
        return MAMMALIAN;
      } else if (YEAST.getDatabaseEntry().equalsIgnoreCase(classAsString)) {
        return YEAST;
      } else {
        return OTHER;
      }
    }
    return OTHER;
  }
  
}