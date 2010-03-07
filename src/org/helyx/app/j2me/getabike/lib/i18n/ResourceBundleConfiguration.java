/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.i18n;

import org.helyx.logging4me.Logger;


public class ResourceBundleConfiguration {

	private static final Logger logger = Logger.getLogger("THEME_CONFIGURATION");
	
	private String name;
	private String packageName;
	
	public ResourceBundleConfiguration(String packageName, String name) {
		super();
		this.packageName = packageName;
		this.name = name;
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
