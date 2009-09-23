package org.helyx.app.j2me.velocite.data.app.domain;

import org.helyx.helyx4me.comparator.Comparator;

public class VersionComparator implements Comparator {

	public int compare(Object object1, Object object2) {
		
		Version version1 = (Version)object1;
		Version version2 = (Version)object2;
		
		if (version1 == null) {
			return -1;
		}
		if (version2 == null) {
			return 1;
		}
		
		if (version1 == version2) {
			return 0;
		}
		
		int versionDelta = version1.getMajor() - version2.getMajor();
		
		if (versionDelta != 0) {
			return versionDelta;
		}
		
		versionDelta = version1.getMinor() - version2.getMinor();
		
		if (versionDelta != 0) {
			return versionDelta;
		}
		
		versionDelta = version1.getRevision() - version2.getRevision();
		
		return versionDelta;
	}

}
