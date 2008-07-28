package org.helyx.app.j2me.lib.theme;

import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.i18n.ClasspathResourceBundleContentProviderFactory;
import org.helyx.app.j2me.lib.i18n.Locale;

public class ClasspathThemeContentProviderFactory extends ClasspathResourceBundleContentProviderFactory {

	private static final String CAT = "DEFAULT_THEME_CONTENT_PROVIDER_FACTORY";
	
	public ClasspathThemeContentProviderFactory() {
		super();
	}
	
	public IContentProvider getContentProviderFactory(Locale locale, String _package, String prefix) {
		return super.getContentProviderFactory(locale, _package, prefix);
	}

}
