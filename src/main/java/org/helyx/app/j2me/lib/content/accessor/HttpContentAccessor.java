package org.helyx.app.j2me.lib.content.accessor;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.stream.HttpConnectionInputStreamProvider;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;

public class HttpContentAccessor implements IContentAccessor {

	
	private static final Logger logger = LoggerFactory.getLogger("HTTP_CONTENT_ACCESSOR");
	
	
	private String url;

	public HttpContentAccessor(String url) {
		super();
		this.url = url;
	}

	public InputStreamProvider getInputStreamProvider() throws ContentAccessorException {
		logger.debug("Url: " + url);
		return new HttpConnectionInputStreamProvider(url);

	}

	public String getPath() {
		return url;
	}
	
}
