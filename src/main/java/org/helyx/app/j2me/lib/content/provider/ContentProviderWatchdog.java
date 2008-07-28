package org.helyx.app.j2me.lib.content.provider;

import org.helyx.app.j2me.lib.concurrent.Future;



public class ContentProviderWatchdog {

	public static Object get(IContentProvider contentProvider) {
		ContentProviderProgressTaskAdapter cppt = new ContentProviderProgressTaskAdapter(contentProvider);
		return Future.get(cppt);
	}
	
}
