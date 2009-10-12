package org.helyx.app.j2me.getabike.data.app.domain;

import java.util.Vector;

public class ConfigurationMetadata {
	
	private String uid;

	private Vector versionRangeList = new Vector();
	
	public ConfigurationMetadata(String uid) {
		super();
		this.uid = uid;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void addVersionRange(VersionRange versionRange) {
		versionRangeList.addElement(versionRange);
	}

	public Vector getVersionRangeList() {
		return versionRangeList;
	}
	
}
