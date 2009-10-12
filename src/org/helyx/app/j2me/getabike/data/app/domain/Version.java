package org.helyx.app.j2me.getabike.data.app.domain;

import org.helyx.app.j2me.getabike.data.app.util.VersionHelper;

public class Version {

	private int major;
	
	private int minor;
	
	private int revision;

	public Version(String versionStr) {
		super();
		Version version = VersionHelper.parseVersion(versionStr);
		major = version.major;
		minor = version.minor;
		revision = version.revision;
	}
	public Version(int major, int minor, int revision) {
		super();
		this.major = major;
		this.minor = minor;
		this.revision = revision;
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getRevision() {
		return revision;
	}
	
	public String getVersion() {
		return major + "." + minor + "." + revision;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[Version]")
		.append(" major=").append(major)
		.append(" minor=").append(minor)
		.append(" revision=").append(revision);
		
		return sb.toString();
	}
	
	
}
