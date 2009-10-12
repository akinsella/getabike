package org.helyx.app.j2me.getabike.content.accessor;

import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.helyx4me.content.accessor.HttpContentAccessor;
import org.helyx.helyx4me.pref.PrefManager;

public class HttpGetABikeContentAccessor extends HttpContentAccessor {

	public HttpGetABikeContentAccessor(String url, boolean forceReadBytePerByte) {
		super(url, forceReadBytePerByte);
	}

	public HttpGetABikeContentAccessor(String url) {
		this(url, !PrefManager.readPrefBoolean(UtilManager.OPTIMIZED_HTTP_MODE_ENABLED));
	}

}
