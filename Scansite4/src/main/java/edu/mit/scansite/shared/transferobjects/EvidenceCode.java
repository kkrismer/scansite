package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Konstantin Krismer
 */
public class EvidenceCode implements IsSerializable {
	private int id = -1;
	private String code;
	private String name;

	public EvidenceCode() {

	}

	public EvidenceCode(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public EvidenceCode(int id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "EvidenceCode [code=" + code + ", name=" + name + "]";
	}
}
