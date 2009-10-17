package org.helyx.app.j2me.getabike.content.accessor;

import java.util.Hashtable;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.util.UtilManager;
import org.helyx.helyx4me.content.accessor.HttpContentAccessor;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.text.TextUtil;

public class HttpGetABikeContentAccessor extends HttpContentAccessor {

	public HttpGetABikeContentAccessor(String url) {
		this(url, !PrefManager.readPrefBoolean(UtilManager.OPTIMIZED_HTTP_MODE_ENABLED));
	}

	public HttpGetABikeContentAccessor(String url, boolean forceReadBytePerByte) {
		this(url, forceReadBytePerByte, null);
	}

	public HttpGetABikeContentAccessor(String url, boolean forceReadBytePerByte, Hashtable httpHeaders) {
		super(completeUrlWithAppUuid(url), forceReadBytePerByte, httpHeaders);
	}
	
	public static String completeUrlWithAppUuid(String url) {
		String newUrl = url;

		String appUuid = PrefManager.readPrefString(PrefConstants.APP_UUID);
		if (appUuid != null && appUuid.trim().length() > 0) {
			newUrl = TextUtil.replaceAll(url, "${app.uuid}", appUuid);
		}

		String appVersion = PrefManager.readPrefString(PrefConstants.APP_VERSION);
		if (appVersion != null && appVersion.trim().length() > 0) {
			newUrl = TextUtil.replaceAll(url, "${app.version}", appVersion);
		}

		String appName = PrefManager.readPrefString(PrefConstants.APP_KEY);
		if (appName != null && appName.trim().length() > 0) {
			newUrl = TextUtil.replaceAll(url, "${app.name}", appName);
		}
		
		return newUrl;
	}

}
