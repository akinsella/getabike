package org.helyx.app.j2me.getabike.content.accessor;

import java.util.Hashtable;

import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.helyx4me.content.accessor.HttpContentAccessor;
import org.helyx.helyx4me.pref.PrefManager;

public class HttpGetABikeContentAccessor extends HttpContentAccessor {

	public HttpGetABikeContentAccessor(String url) {
		this(url, !PrefManager.readPrefBoolean(UtilManager.OPTIMIZED_HTTP_MODE_ENABLED));
	}

	public HttpGetABikeContentAccessor(String url, boolean forceReadBytePerByte) {
		this(url, forceReadBytePerByte, null);
	}

	public HttpGetABikeContentAccessor(String url, boolean forceReadBytePerByte, Hashtable httpHeaders) {
		super(url, forceReadBytePerByte, httpHeaders);
	}
	
	

}
