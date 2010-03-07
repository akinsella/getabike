package org.helyx.app.j2me.getabike.content.accessor;

import java.util.Hashtable;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.getabike.lib.pref.PrefManager;
import org.helyx.app.j2me.getabike.lib.text.TextUtil;

public class HttpGetABikeContentAccessor extends HttpContentAccessor {

	public HttpGetABikeContentAccessor(String url) {
		this(url, !PrefManager.readPrefBoolean(PrefConstants.OPTIMIZED_HTTP_MODE_ENABLED));
	}

	public HttpGetABikeContentAccessor(String url, boolean forceReadBytePerByte) {
		this(url, forceReadBytePerByte, null);
	}

	public HttpGetABikeContentAccessor(String url, boolean forceReadBytePerByte, Hashtable httpHeaders) {
		super(completeUrlWithAppUuid(url), forceReadBytePerByte, httpHeaders);
		addHeader("Connection", "close");
	}
	
	public static String completeUrlWithAppUuid(String url) {
		String newUrl = url;

		String appKey = PrefManager.readPrefString(PrefConstants.APP_KEY);
		if (appKey != null && appKey.trim().length() > 0) {
			newUrl = TextUtil.replaceAll(newUrl, "${app.key}", appKey);
		}

		String appVersion = PrefManager.readPrefString(PrefConstants.APP_VERSION);
		if (appVersion != null && appVersion.trim().length() > 0) {
			newUrl = TextUtil.replaceAll(newUrl, "${app.version}", appVersion);
		}

		String appUuid = PrefManager.readPrefString(PrefConstants.APP_UUID);
		if (appUuid != null && appUuid.trim().length() > 0) {
			newUrl = TextUtil.replaceAll(newUrl, "${app.uuid}", appUuid);
		}
		
		return newUrl;
	}

}
