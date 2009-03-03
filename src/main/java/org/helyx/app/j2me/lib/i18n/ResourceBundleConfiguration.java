package org.helyx.app.j2me.lib.i18n;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class ResourceBundleConfiguration {

	private static final Logger logger = LoggerFactory.getLogger("THEME_CONFIGURATION");
	
	private String name;
	private String packageName;
	
	public ResourceBundleConfiguration(String name, String packageName) {
		super();
		this.name = name;
		this.packageName = packageName;
	}

	public String getName() {
		return name;
	}

	public String getPackageName() {
		return packageName;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[ThemeConfiguration]")
		.append(" name=").append(name)
		.append(", packageName=").append(packageName);

		return sb.toString();
	}
	
}
