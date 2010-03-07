/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.content.accessor;

import java.util.Hashtable;

import org.helyx.app.j2me.getabike.lib.stream.HttpConnectionInputStreamProvider;
import org.helyx.app.j2me.getabike.lib.content.accessor.ContentAccessorException;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.logging4me.Logger;


public class HttpContentAccessor implements IContentAccessor {

	
	private static final Logger logger = Logger.getLogger("HTTP_CONTENT_ACCESSOR");
	
	private String url;
	private boolean forceReadBytePerByte = true;
	private Hashtable httpHeaders;

	public HttpContentAccessor(String url) {
		this(url, true, null);
	}

	public HttpContentAccessor(String url, boolean forceReadBytePerByte) {
		this(url, forceReadBytePerByte, null);
	}

	public HttpContentAccessor(String url, boolean forceReadBytePerByte, Hashtable httpHeaders) {
		super();
		this.url = url;
		this.forceReadBytePerByte = forceReadBytePerByte;
		this.httpHeaders = httpHeaders;
	}

	public InputStreamProvider getInputStreamProvider() throws ContentAccessorException {
		logger.debug("Url: " + url);
		return new HttpConnectionInputStreamProvider(url, forceReadBytePerByte, httpHeaders);
	}

	public String getPath() {
		return url;
	}
	
	public void addHeader(String key, String value) {
		if (httpHeaders == null) {
			httpHeaders = new Hashtable();
		}
		httpHeaders.put(key, value);
	}
	
	public void removeHeader(String key) {
		if (httpHeaders != null) {
			httpHeaders.remove(key);
		}
	}
	
	public void removeAllHeaders() {
		if (httpHeaders != null) {
			httpHeaders.clear();
		}
	}
	
}
