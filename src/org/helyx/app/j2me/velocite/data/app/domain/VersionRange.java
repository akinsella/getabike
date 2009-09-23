package org.helyx.app.j2me.velocite.data.app.domain;


public class VersionRange {

	private String url;

	private Version minVersion;
	
	private Version maxVersion;

	public VersionRange(Version minVersion, Version maxVersion, String url) {
		super();
		this.minVersion = minVersion;
		this.maxVersion = maxVersion;
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public Version getMinVersion() {
		return minVersion;
	}
	
	public Version getMaxVersion() {
		return maxVersion;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[VersionData]")
		
		.append("minVersion=").append(minVersion)
		.append(", maxVersion=").append(maxVersion)
		.append(", url=").append(url);
	
		return sb.toString();
	}
	
	
}
