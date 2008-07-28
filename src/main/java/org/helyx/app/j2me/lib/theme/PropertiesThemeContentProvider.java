package org.helyx.app.j2me.lib.theme;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.i18n.PropertiesResourceBundleContentProvider;
import org.helyx.app.j2me.lib.log.Log;


public class PropertiesThemeContentProvider extends PropertiesResourceBundleContentProvider {
	
	private static final String CAT = "PROPERTIES_THEME_CONTENT_PROVIDER";

	public PropertiesThemeContentProvider(IContentAccessor messageContentAccessor) {
		super(messageContentAccessor);
	}


	public void loadData() {
		
		Log.debug(CAT, "Loading properties ...");
		super.loadData();
	}

	protected String getCat() {
		return CAT;
	}
	
	public String getDescription() {
		return "Fetchs properties from path: '" + messageContentAccessor.getPath() + "'";
	}


}
