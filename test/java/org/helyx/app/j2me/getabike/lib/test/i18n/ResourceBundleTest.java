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
package org.helyx.app.j2me.getabike.lib.test.i18n;

import java.util.Enumeration;

import junit.framework.TestCase;

import org.helyx.app.j2me.getabike.lib.concurrent.Future;
import org.helyx.app.j2me.getabike.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.getabike.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.getabike.lib.i18n.ClasspathResourceBundleContentProviderFactory;
import org.helyx.app.j2me.getabike.lib.i18n.Locale;
import org.helyx.app.j2me.getabike.lib.i18n.ResourceBundle;
import org.helyx.logging4me.Logger;


public class ResourceBundleTest extends TestCase {

	public static final Logger logger = Logger.getLogger("RESOURCE_BUNDLE_TEST");
	
	public ResourceBundleTest() {
		super();
	}
	
	public void testResourceBundle() {
		ClasspathResourceBundleContentProviderFactory drbcpf = new ClasspathResourceBundleContentProviderFactory(Locale.ENGLAND, "org.helyx.app.j2me.getabike.lib.i18n", "messages");
		IContentProvider contentProvider = drbcpf.getContentProviderFactory();
		
		ResourceBundle resourceBundle = (ResourceBundle)Future.get(new ContentProviderProgressTaskAdapter(contentProvider));
		Enumeration _enum = resourceBundle.getMessageKeys();
		while (_enum.hasMoreElements()) {
			String key = (String)_enum.nextElement();
			String value = (String)resourceBundle.get(key);
			logger.info("key='" + key + "', value='" + value + "'");
		}
	}
	
}
