package edu.mit.scansite.server.dataaccess.ssh;

import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import edu.mit.scansite.server.dataaccess.file.ConfigReader;

/**
 * @author Tobieh
 */
public class SshConnector {
  private static final String CFG_KEY_SERVER_URL = "SERVER_URL";
  private static final String CFG_KEY_SERVER_PORT = "SERVER_PORT";
  private static final String CFG_KEY_SERVER_USER = "SERVER_USER";
  private static final String CFG_KEY_SERVER_PASSWORD = "SERVER_PASSWORD";
  
  private static final boolean doLog = false;
  
  private String serverUrl;
  private int serverPort;
  private String user;
  private String password;
  
  private Session session;
  
  public class SshResult {
    private int exitStatus = 0;
    private String output = "";
    public SshResult() {
    }
    public SshResult(String output, int exitStatus) {
      this.output = output;
      this.exitStatus = exitStatus;
    }
    public int getExitStatus() {
      return exitStatus;
    }
    public void setExitStatus(int exitStatus) {
      this.exitStatus = exitStatus;
    }
    public String getOutput() {
      return output;
    }
    public void setOutput(String output) {
      this.output = output;
    }
    @Override
    public String toString() {
      return output+"\nExit-Status: "+exitStatus;
    }
  }
  
  public SshConnector() {
  }

  public void init(ConfigReader reader) {
    serverUrl = reader.get(CFG_KEY_SERVER_URL);
    serverPort = reader.getInt(CFG_KEY_SERVER_PORT);
    user = reader.get(CFG_KEY_SERVER_USER);
    password = reader.get(CFG_KEY_SERVER_PASSWORD);
  }
  
  public void connect() throws SshConnectorException {
    try {
      if (session == null || !session.isConnected()) {
        JSch jsch = new JSch();
        if (doLog) {
          JSch.setLogger(new JSchLogger());
        }
       // setupSftpIdentity(jsch);
        session = jsch.getSession(user, serverUrl, serverPort);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
      }
    } catch (JSchException e) {
      throw new SshConnectorException(e);
    }
  }
  
  public void disconnect() {
    if (session != null && session.isConnected()) {
      session.disconnect();
    }
  }
  
  public boolean isConnected() {
    return (session != null && session.isConnected());
  }
  
  /**
   * Executes a command and returns its output.
   * @param command The command that will be executed.
   * @return The command's output.
   * @throws SshConnectorException Is thrown if an error occurs.
   */
  public SshResult runCommand(String command) throws SshConnectorException {
    if (isConnected()) {
      ChannelExec channel = null;
      try {
        channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        channel.setInputStream(null);
        //((ChannelExec)channel).setErrStream(System.err);
        InputStream in = channel.getInputStream();
        byte[] tmp=new byte[1024];
        channel.connect();
        StringBuilder result = new StringBuilder();
        int exitStatus = 0;
        while(true){
          try{
            while(in.available()>0){
              int i=in.read(tmp, 0, 1024);
              if(i<0)break;
              result.append(new String(tmp, 0, i));
            }
            if(channel.isClosed()){
              exitStatus = channel.getExitStatus();
              break;
            }
          } catch (IOException e) {
            try{Thread.sleep(500);} catch(Exception ee) { throw new SshConnectorException(); }
          }
        }
        return new SshResult(result.toString(), exitStatus);
      } catch (Exception e) {
        throw new SshConnectorException(e);
      } finally {
        if (channel != null) {
          channel.disconnect();
        }
      }
    } else {
      connect();
      SshResult result = runCommand(command);
      disconnect();
      return result;
    }
  }
  
  public void copyFileLocalToRemote(String localFilePath, String serverFilePath) throws SshConnectorException {
    copyFile(localFilePath, serverFilePath, true);
  }
  
  /**
   * Copies a file from fromFilePath to toFilePath dependent.
   * @param fromFilePath FilePath of existing file.
   * @param toFilePath FilePath of desired location of filecopy.
   * @param toServer If TRUE fromFilePath is the localFilePath and the file is copied to the Server, otherwise
   * vice versa.
   * @throws SshConnectorException
   */
  private void copyFile(String fromFilePath, String toFilePath, boolean toServer) throws SshConnectorException {
    if (isConnected()) {
      Channel channel = null;
      try {
        channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;

        if (toServer) {
          sftpChannel.put(fromFilePath, toFilePath);
        } else {
          sftpChannel.get(fromFilePath, toFilePath);
        }
        
        sftpChannel.exit();
        channel.disconnect(); 
      } catch (Exception e) {
        throw new SshConnectorException(e);
      } finally {
        if (channel != null) {
          channel.disconnect();
        }
      }
    } else {
      connect();
      copyFile(fromFilePath, toFilePath, toServer);
      disconnect();
    }
  }
  
  public void copyFileRemoteToLocal(String serverFilePath, String localFilePath) throws SshConnectorException {
    copyFile(serverFilePath, localFilePath, false);
  }
}
