package edu.mit.scansite.server.domains;

import java.lang.reflect.Constructor;

import edu.mit.scansite.server.ServiceLocator;
import edu.mit.scansite.server.dataaccess.file.ConfigReader;
import edu.mit.scansite.shared.DataAccessException;

/**
 * @author Tobieh
 */
public class DomainLocatorFactory {
  
  private static final String CFG_KEY_CLASS = "DOMAIN_LOCATOR_CLASS";
  
    
  public static DomainLocator getDomainLocator() throws DataAccessException, DomainLocatorException {
    ConfigReader cfg = new ConfigReader(ServiceLocator.getDomainLocatorConfigProperties());
    String domainLocatorClass = cfg.get(CFG_KEY_CLASS);
    try {
      Class<? extends DomainLocator> clazz = Class.forName(domainLocatorClass).asSubclass(DomainLocator.class);
      Constructor<? extends DomainLocator> constructor = clazz.getConstructor(ConfigReader.class);
      return constructor.newInstance(cfg);
    } catch (ClassNotFoundException e) {
      throw new DomainLocatorException("Error instantiating domain locator. Class not found!", e);
    } catch (Exception e) {
      throw new DomainLocatorException("Error instantiating domain locator.", e);
    }
  }
  
}
