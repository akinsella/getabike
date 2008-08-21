package org.helyx.app.j2me.lib.i18n;

import org.helyx.app.j2me.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.text.TextUtil;

public class ClasspathResourceBundleContentProviderFactory {

	private static final Log log = LogFactory.getLog("DEFAULT_RESOURCE_BUNDLE_CONTENT_PROVIDER_FACTORY");
	
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
		
		log.debug("classpath: '" + classpath + "'");
		ClasspathContentAccessor classpathContentAccessor = new ClasspathContentAccessor(classpath);
		IContentProvider resourceBundleContentProvider = new PropertiesResourceBundleContentProvider(classpathContentAccessor);
		
		return resourceBundleContentProvider;
	}

}
