package org.helyx.app.j2me.lib.content.provider;

import org.helyx.app.j2me.velocite.data.city.domain.City;

public interface IContentProviderFactory {

	IContentProvider getContentProviderFactory(City city);
	
}
