package org.helyx.app.j2me.velocite.content.accessor;

import org.helyx.app.j2me.velocite.util.UtilManager;
import org.helyx.helyx4me.content.accessor.HttpContentAccessor;
import org.helyx.helyx4me.pref.PrefManager;

public class HttpVelociteContentAccessor extends HttpContentAccessor {

	public HttpVelociteContentAccessor(String url, boolean forceReadBytePerByte) {
		super(url, forceReadBytePerByte);
	}

	public HttpVelociteContentAccessor(String url) {
		this(url, !PrefManager.readPrefBoolean(UtilManager.OPTIMIZED_HTTP_MODE_ENABLED));
	}

}
