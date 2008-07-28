package org.helyx.app.j2me.lib.theme;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.i18n.XmlResourceBundleContentProvider;
import org.helyx.app.j2me.lib.log.Log;


public class XmlThemeContentProvider extends XmlResourceBundleContentProvider {
	
	private static final String CAT = "XML_THEME_CONTENT_PROVIDER";

	public XmlThemeContentProvider(IContentAccessor messageContentAccessor) {
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
