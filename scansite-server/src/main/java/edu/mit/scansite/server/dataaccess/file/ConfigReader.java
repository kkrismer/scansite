package edu.mit.scansite.server.dataaccess.file;

import java.util.Properties;
import java.util.Set;

/**
 * A class for reading configuration files (*.properties).
 * 
 * @author tobieh
 */
public class ConfigReader {
	private Properties config = new Properties();

	public ConfigReader(Properties dbConstantsConfig) {
		config = dbConstantsConfig;
	}

	/**
	 * @param key
	 *            The cfg-key.
	 * @return The key's value (without leading and trailing whitespace), or
	 *         NULL if there is none.
	 */
	public String get(String key) {
		String value = config.getProperty(key);
		return (value == null) ? null : value.trim();
	}

	/**
	 * @param key
	 *            The cfg-key.
	 * @return The key's value (without leading and trailing whitespace), or
	 *         NULL if there is none.
	 */
	public Integer getInt(String key) {
		String value = config.getProperty(key);
		return (value == null) ? null : Integer.valueOf(value.trim());
	}

	/**
	 * @return The keys that are defined in this file.
	 */
	public Set<String> getStringPropertyNames() {
		return config.stringPropertyNames();
	}
}
