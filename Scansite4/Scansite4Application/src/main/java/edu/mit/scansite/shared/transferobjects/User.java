package edu.mit.scansite.shared.transferobjects;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.mit.scansite.shared.util.Formatter;

/**
 * @author Tobieh
 * @author Konstantin Krismer
 */
public class User implements IsSerializable, Comparable<User> {

	private Formatter formatter = new Formatter();

	private String email = "";
	private String firstName = "";
	private String lastName = "";
	private String password = "";
	private boolean isAdmin = false;
	private boolean isSuperAdmin = false;

	public User() {
	}

	public User(String email, String firstName, String lastName,
			String password, boolean isAdmin, boolean isSuperAdmin) {

		setEmail(email);
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.setAdmin(isAdmin);
		this.setSuperAdmin(isSuperAdmin);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = formatter.formatUsername(email);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = ((firstName == null) ? "" : firstName).trim();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = ((lastName == null) ? "" : lastName).trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password.trim();
	}

	public String getSessionId() {
		String id = email + "scansiteSalt";
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(id.getBytes(), 0, id.length());
			id = (new BigInteger(1, m.digest()).toString(16)).toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return id;
	}

	@Override
	public int compareTo(User o) {
		String i = this.firstName;
		if (o == null) {
			return 1;
		}
		String u = o.getFirstName();
		if (i != null && u != null) {
			int compareFirstName = i.compareToIgnoreCase(u);
			if (compareFirstName == 0 && this.lastName != null
					&& o.lastName != null) {
				return this.lastName.compareToIgnoreCase(o.lastName);
			} else {
				return compareFirstName;
			}
		} else {
			if (i == null && u == null) {
				return 0;
			} else if (i == null) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((email == null) ? 0 : email.toLowerCase().hashCode());
		result = prime
				* result
				+ ((firstName == null) ? 0 : firstName.toLowerCase().hashCode());
		result = prime * result + (isAdmin ? 1231 : 1237);
		result = prime * result + (isSuperAdmin ? 1231 : 1237);
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.toLowerCase().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equalsIgnoreCase(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equalsIgnoreCase(other.firstName))
			return false;
		if (isAdmin != other.isAdmin)
			return false;
		if (isSuperAdmin != other.isSuperAdmin)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equalsIgnoreCase(other.lastName))
			return false;
		return true;
	}
}
