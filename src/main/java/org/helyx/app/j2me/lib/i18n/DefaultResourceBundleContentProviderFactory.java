package org.helyx.app.j2me.lib.i18n;

import org.helyx.app.j2me.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.text.TextUtil;

public class DefaultResourceBundleContentProviderFactory {

	private static final String CAT = "DEFAULT_RESOURCE_BUNDLE_CONTENT_PROVIDER_FACTORY";
	
	public DefaultResourceBundleContentProviderFactory() {
		super();
	}
	
	public IContentProvider getContentProviderFactory(Locale locale, String _package, String prefix) {
		
		String classpath = "/" + TextUtil.replaceAll(_package, '.', '/') + "/" + prefix + "_" + locale.getName() + ".properties";
		Log.debug(CAT, "classpath: '" + classpath + "'");
		ClasspathContentAccessor classpathContentAccessor = new ClasspathContentAccessor(classpath);
		IContentProvider stationContentProvider = new PropertiesResourceBundleContentProvider(classpathContentAccessor);
		
		return stationContentProvider;
	}

}
