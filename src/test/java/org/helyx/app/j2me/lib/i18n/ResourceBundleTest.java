package org.helyx.app.j2me.lib.i18n;

import java.util.Enumeration;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.concurrent.Future;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;

public class ResourceBundleTest extends TestCase {

	public static final String CAT = "RESOURCE_BUNDLE_TEST";
	
	public ResourceBundleTest() {
		super();
	}
	
	public void testResourceBundle() {
		ClasspathResourceBundleContentProviderFactory drbcpf = new ClasspathResourceBundleContentProviderFactory();
		IContentProvider contentProvider = drbcpf.getContentProviderFactory(new Locale("fr", "FR"), "org.helyx.app.j2me.velocite", "messages");
		
		Future future = new Future();
		Theme resourceBundle = (Theme)future.get(new ContentProviderProgressTaskAdapter(contentProvider));
		Enumeration _enum = resourceBundle.getMessageKeys();
		while (_enum.hasMoreElements()) {
			String key = (String)_enum.nextElement();
			String value = (String)resourceBundle.get(key);
			Log.info(CAT, "key='" + key + "', value='" + value + "'");
		}
	}
	
}