package org.helyx.app.j2me.lib.content.accessor;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.stream.HttpConnectionInputStreamProvider;
import org.helyx.app.j2me.lib.stream.IInputStreamProvider;

public class SingleParamHttpContentAccessor implements IContentAccessor {

	private static final String CAT = "SINGLE_PARAM_HTTP_CONTENT_ACCESSOR";

	private String url;
	private String param;
	private String targetUrl;

	public SingleParamHttpContentAccessor(String url, String param) {
		super();
		this.url = url;
		this.param = param;
		init();
	}
	
	private void init() {
		targetUrl = url + param;
	}

	public IInputStreamProvider getInputStreamProvider() throws ContentAccessorException {
		Log.debug(CAT, "Url: " + targetUrl);
		return new HttpConnectionInputStreamProvider(targetUrl);
	}

	public String getPath() {
		return targetUrl;
	}
	
}
