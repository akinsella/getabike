package org.helyx.app.j2me.lib.content.accessor;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.stream.HttpConnectionInputStreamProvider;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;

public class HttpContentAccessor implements IContentAccessor {

	
	private static final String CAT = "HTTP_CONTENT_ACCESSOR";
	
	private String url;

	public HttpContentAccessor(String url) {
		super();
		this.url = url;
	}

	public InputStreamProvider getInputStreamProvider() throws ContentAccessorException {
		Log.debug(CAT, "Url: " + url);
		return new HttpConnectionInputStreamProvider(url);
	}

	public String getPath() {
		return url;
	}
	
}
