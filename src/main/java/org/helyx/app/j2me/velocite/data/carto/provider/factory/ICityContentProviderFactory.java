package org.helyx.app.j2me.velocite.data.carto.provider.factory;

import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public interface ICityContentProviderFactory {

	IContentProvider getContentProviderFactory(City city);
	
}
