package edu.mit.scansite.server.dataaccess.ssh;

import com.jcraft.jsch.Logger;

/**
 * @author Tobieh
 */
public class JSchLogger implements Logger {

   public boolean isEnabled(int arg0) {
    return true;
   }
   
   public void log(int arg0, String arg1) {
    System.out.println(String.format("[SFTP/SSH -> %s]", arg1));
   }
}
