package org.helyx.app.j2me.getabike.data.app.util;

import org.helyx.app.j2me.getabike.data.app.domain.Version;
import org.helyx.helyx4me.text.TextUtil;
import org.helyx.logging4me.Logger;

public class VersionHelper {

	private static final Logger logger = Logger.getLogger("VERSION_HELPER");
	
	private VersionHelper() {
		super();
	}
	
	public static Version parseVersion(String versionStr) {
		if (versionStr == null) {
			throw new NullPointerException("Version cannot be null");
		}
		String[] versionParts = TextUtil.split(versionStr, '.');

		if (logger.isDebugEnabled()) {
			logger.debug("versionParts count: " + versionParts.length);
		}
		
		if (versionParts.length != 3) {
			throw new IllegalArgumentException("Version pattern expected is: 'x.y.z' - Received is: '" + versionStr + "'");
		}
		
		int major = 0;
		int minor = 0;
		int revision = 0;
		
		try {
			major = Integer.parseInt(versionParts[0]);
		}
		catch(NumberFormatException nfe) {
			logger.warn("Major has to be integer: '" + versionParts[0] + "'");
		}
		
		try {
			minor = Integer.parseInt(versionParts[1]);
		}
		catch(NumberFormatException nfe) {
			logger.warn("Minor has to be integer: '" + versionParts[1] + "'");
		}
		
		try {
			revision = Integer.parseInt(versionParts[2]);
		}
		catch(NumberFormatException nfe) {
			logger.warn("Revision is not integer: '" + versionParts[2] + "'");
		}
		
		Version version = new Version(major, minor, revision);
		
		return version;
	}
	
}
