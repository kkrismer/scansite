package edu.mit.scansite.shared.dispatch.user;

import net.customware.gwt.dispatch.shared.Result;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class UserPrivilegesResult implements Result {
  private boolean isAdmin = false;
  private boolean isSuperAdmin = false;

  public UserPrivilegesResult() {
  }
  
  public UserPrivilegesResult(boolean isAdmin, boolean isSuperAdmin) {
    this.setAdmin(isAdmin);
    this.setSuperAdmin(isSuperAdmin);
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public boolean isSuperAdmin() {
    return isSuperAdmin;
  }

  public void setSuperAdmin(boolean isSuperAdmin) {
    this.isSuperAdmin = isSuperAdmin;
  }
  
}

