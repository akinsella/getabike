package org.helyx.app.j2me.lib.content.accessor;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.stream.HttpConnectionInputStreamProvider;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;

public class HttpContentAccessor implements IContentAccessor {

	
	private static final Log log = LogFactory.getLog("HTTP_CONTENT_ACCESSOR");
	
	
	private String url;

	public HttpContentAccessor(String url) {
		super();
		this.url = url;
	}

	public InputStreamProvider getInputStreamProvider() throws ContentAccessorException {
		log.debug("Url: " + url);
		return new HttpConnectionInputStreamProvider(url);

	}

	public String getPath() {
		return url;
	}
	
}
