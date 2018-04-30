package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class Parameter implements IsSerializable {
	private String name;
	private String value;
	private boolean isLongParameter = false;
	private boolean isInactiveParameter = false;
	private String group = null;
	private boolean isGroupBegin = false;
	private boolean isGroupEnd = false;

	public Parameter() {

	}

	public Parameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public Parameter(String name, double value) {
		this.name = name;
		this.value = NumberFormat.getFormat("#0.000").format(value);
	}

	public Parameter(String group, boolean isGroupBegin) {
		this.name = null;
		this.value = null;
		this.group = group;
		this.isGroupBegin = isGroupBegin;
		this.isGroupEnd = !isGroupBegin;
	}

	public Parameter(String name, String value, boolean isLongParameter,
			boolean isInactiveParameter) {
		this.name = name;
		this.value = value;
		this.isLongParameter = isLongParameter;
		this.isInactiveParameter = isInactiveParameter;
	}

	public Parameter(String name, int value) {
		this.name = name;
		this.value = NumberFormat.getFormat("#,###").format(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isLongParameter() {
		return isLongParameter;
	}

	public void setLongParameter(boolean isLongParameter) {
		this.isLongParameter = isLongParameter;
	}

	public boolean isInactiveParameter() {
		return isInactiveParameter;
	}

	public void setInactiveParameter(boolean isInactiveParameter) {
		this.isInactiveParameter = isInactiveParameter;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isGroupBegin() {
		return isGroupBegin;
	}

	public boolean isGroupEnd() {
		return isGroupEnd;
	}

	@Override
	public String toString() {
		return "Parameter [name=" + name + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
