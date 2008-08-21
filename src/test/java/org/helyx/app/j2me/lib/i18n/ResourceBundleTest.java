package org.helyx.app.j2me.lib.i18n;

import java.util.Enumeration;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.concurrent.Future;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class ResourceBundleTest extends TestCase {

	public static final Log log = LogFactory.getLog("RESOURCE_BUNDLE_TEST");
	
	public ResourceBundleTest() {
		super();
	}
	
	public void testResourceBundle() {
		ClasspathResourceBundleContentProviderFactory drbcpf = new ClasspathResourceBundleContentProviderFactory(Locale.FRANCE, "org.helyx.app.j2me.velocite", "messages");
		IContentProvider contentProvider = drbcpf.getContentProviderFactory();
		
		ResourceBundle resourceBundle = (ResourceBundle)Future.get(new ContentProviderProgressTaskAdapter(contentProvider));
		Enumeration _enum = resourceBundle.getMessageKeys();
		while (_enum.hasMoreElements()) {
			String key = (String)_enum.nextElement();
			String value = (String)resourceBundle.get(key);
			log.info("key='" + key + "', value='" + value + "'");
		}
	}
	
}
