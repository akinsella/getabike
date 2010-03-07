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

import org.helyx.app.j2me.getabike.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.getabike.lib.i18n.Locale;
import org.helyx.app.j2me.getabike.lib.i18n.PropertiesResourceBundleContentProvider;
import org.helyx.app.j2me.getabike.lib.text.TextUtil;
import org.helyx.logging4me.Logger;


public class ClasspathResourceBundleContentProviderFactory {

	private static final Logger logger = Logger.getLogger("DEFAULT_RESOURCE_BUNDLE_CONTENT_PROVIDER_FACTORY");
	
	private Locale locale;
	private String _package;
	private String rbName;
	
	public ClasspathResourceBundleContentProviderFactory(Locale locale, String _package, String rbName) {
		super();
		this.locale = locale;
		this._package = _package;
		this.rbName = rbName;
	}
	
	public IContentProvider getContentProviderFactory() {
		String classpath = new StringBuffer()
			.append("/")
			.append(TextUtil.replaceAll(_package, '.', '/'))
			.append("/").append(rbName).append("_")
			.append(locale.getName())
			.append(".properties")
			.toString();
		
		logger.debug("classpath: '" + classpath + "'");
		ClasspathContentAccessor classpathContentAccessor = new ClasspathContentAccessor(classpath);
		IContentProvider resourceBundleContentProvider = new PropertiesResourceBundleContentProvider(classpathContentAccessor);
		
		return resourceBundleContentProvider;
	}

}
