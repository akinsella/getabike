package org.helyx.app.j2me.lib.content.provider;

import org.helyx.app.j2me.lib.content.provider.exception.ContentProviderFactoryException;

public interface IContentProviderFactory {

	IContentProvider createContentProvider() throws ContentProviderFactoryException;
	
}
