package edu.mit.scansite.shared.util;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A class for formatting Strings in a common way.
 * 
 * @author tobieh
 */
public class Formatter implements IsSerializable {
	public Formatter() {
	}

	/**
	 * @param username
	 *            The string that is going to be changed.
	 * @return The given parameter, lower-case and without whitespace at the
	 *         beginning and the end.
	 */
	public String formatUsername(String username) {
		username = username.trim();
		username = username.toLowerCase();
		return username;
	}

	/**
	 * @param password
	 *            The string that is going to be changed.
	 * @return All " are replaced with ' and all surrounding whitespace is
	 *         removed.
	 */
	public String formatPassword(String password) {
		password = password.trim();
		password = password.replaceAll("\"", "'");
		return password;
	}

	/**
	 * Replaces common linebreaks (\n) with HTML-linebreaks
	 * 
	 * @param text
	 *            input text
	 * @return text with HTML linebreaks.
	 */
	public String getHtml(String text) {
		return text.replaceAll("\n", "</br>");
	}

	/**
	 * Replaces so-called 'magic quotes' " in a string with regular single
	 * quotes (')
	 * 
	 * @param text
	 *            input text.
	 * @return Text with no ", but ' instead.
	 */
	public String replaceMagicQuotes(String text) {
		return (text != null) ? text.replaceAll("\"", "'") : "";
	}

	/**
	 * @param text
	 *            The input text.
	 * @return input text without any whitespace at all.
	 */
	public String trimWhitespace(String text) {
		String x = text;
		x = x.trim();
		x = x.replaceAll("[\n\r]", ""); // probably not necessary due to next
										// line --- but, just in case...
		x = x.replaceAll("\\s+", "");
		return x;
	}

	/**
	 * @param path
	 *            A file path ala /var/www
	 * @return A file path that ends with '/' and that contains slashes '/'
	 *         instead of backslashes '\'.
	 */
	public String formatFilePath(String path) {
		String newPath = path;
		if (newPath != null) {
			if (newPath.contains("\\")) {
				newPath = newPath.replaceAll("\\", "/");
			}
			if (!newPath.endsWith("/")) {
				newPath += "/";
			}
		}
		return newPath;
	}

	/**
	 * Trims the sequence and removes numbers.
	 * 
	 * @return A 'valid' protein sequence.
	 */
	public String formatSequence(String seq) {
		seq = trimWhitespace(seq);
		seq = seq.replaceAll("[^A-Za-z]", "");
		return seq.toUpperCase();
	}
}
