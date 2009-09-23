package org.helyx.app.j2me.velocite.data.app.domain;

import java.util.Hashtable;

public class VersionMetaData {

	private Hashtable versionDataMap;

	public VersionMetaData(Hashtable versionDataMap) {
		super();
		this.versionDataMap = versionDataMap;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[VersionMetaData]")
		.append(" dataMap=").append(versionDataMap);
		
		return sb.toString();
	}

}
