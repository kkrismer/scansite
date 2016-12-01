package edu.mit.scansite.shared.util;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.mit.scansite.shared.transferobjects.AminoAcid;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class Validator implements Serializable {

	private static final long serialVersionUID = -5281370510604530102L;

	public static final int MIN_EMAIL_LENGTH = 7;
	public static final int MIN_DOMAIN_LENGTH = 4; // in email, after @
	public static final int MIN_PASSWORD_LENGTH = 6;

	public Validator() {
	}

	public boolean validateEmail(String email) {
		if (email == null || email.length() <= MIN_EMAIL_LENGTH
				|| !email.contains("@") || !email.contains(".")) {
			return false;
		}
		String[] parts = email.split("@");
		if (parts.length != 2 || !parts[1].contains(".")
				|| parts[1].length() <= MIN_DOMAIN_LENGTH) {
			return false;
		}
		return true;
	}

	public boolean validatePassword(String password) {
		if (password == null) {
			return false;
		}
		return password.length() >= MIN_PASSWORD_LENGTH;
	}

	/**
	 * @param text
	 *            The string to be validated
	 * @return TRUE if the string is not null and not empty (trimmed).
	 */
	public boolean validateGivenString(String txt) {
		if (txt == null || txt.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * @param text
	 *            The string to be validated.
	 * @return TRUE if the string is not null and does not contain spaces.
	 */
	public boolean validateNoSpaces(String text) {
		if (text == null || text.isEmpty() || text.contains(" ")) {
			return false;
		}
		return true;
	}

	public boolean validateProteinSequence(String seq) {
		if (seq == null || seq.isEmpty()) {
			return false;
		}
		Set<Character> aas = new HashSet<Character>();
		for (AminoAcid aa : AminoAcid.values()) {
			if (!aa.equals(AminoAcid._C) && !aa.equals(AminoAcid._N)) {
				aas.add(aa.getOneLetterCode());
			}
		}
		for (int i = 0; i < seq.length(); ++i) {
			if (!aas.contains(seq.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
