package edu.mit.scansite.shared.transferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Tobieh
 */
public class EvidenceResource implements IsSerializable {

	public static final String URI_REPLACEMENT_ACCESSION = "%P";
	public static final String URI_REPLACEMENT_SITE = "%S";

	private String resourceName;
	private String uri;
	private String proteinAccession;
	private String site;

	public EvidenceResource() {
	}

	public EvidenceResource(String resourceName, String uri,
			String proteinAccession, String site) {
		this(resourceName, proteinAccession, site);
		setUri(uri);
	}

	public EvidenceResource(String resourceName, String uri) {
		this.resourceName = resourceName;
		this.uri = uri;
	}

	public EvidenceResource(String resourceName, String proteinAccession,
			String site) {
		this.resourceName = resourceName;
		this.proteinAccession = proteinAccession;
		this.site = site;
	}

	public EvidenceResource(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setUri(String uri) {
		this.uri = uri;
		if (uri != null && proteinAccession != null) {
			this.uri = this.uri.replace(URI_REPLACEMENT_ACCESSION,
					proteinAccession);
		}
		if (uri != null && site != null) {
			this.uri = this.uri.replace(URI_REPLACEMENT_SITE, site);
		}
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getProteinAccession() {
		return proteinAccession;
	}

	public String getSite() {
		return site;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getUri() {
		return uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((resourceName == null) ? 0 : resourceName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (obj instanceof EvidenceResource) {
			return ((EvidenceResource) obj).getResourceName().equals(
					resourceName);
		}
		return false;
	}
}
