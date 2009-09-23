package org.helyx.app.j2me.velocite.data.app.domain;

public class VersionData {

	private Version version;
	
	private String url;
	
	private String type;

	public VersionData(String type, Version version, String url) {
		super();
		this.type = type;
		this.version = version;
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public Version getVersion() {
		return version;
	}

	public String getUrl() {
		return url;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[Data]")
		.append(" type=").append(type)
		.append(", version=").append(version)
		.append(", url=").append(url);		
		
		return sb.toString();
	}
	
}
