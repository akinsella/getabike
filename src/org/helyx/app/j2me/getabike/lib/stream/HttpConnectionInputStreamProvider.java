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
package org.helyx.app.j2me.getabike.lib.stream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.helyx.basics4me.io.BufferedInputStream;
import org.helyx.app.j2me.getabike.lib.stream.BytePerByteInputStream;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.stream.exception.HttpAccessException;
import org.helyx.logging4me.Logger;


public class HttpConnectionInputStreamProvider implements InputStreamProvider {
	
	private static final String HTTP_HEADER_FIELD_USER_AGENT = "User-Agent";
	private static final String HTTP_HEADER_FIELD_LOCATION = "Location";

	private static final String DEFAULT_USER_AGENT = "Profile/MIDP-2.0 Configuration/CLDC-1.1";
	
	private static final Logger logger = Logger.getLogger("HTTP_CONNECTION_INPUT_STREAM_PROVIDER");

	private String url;
	private HttpConnection httpConnection;
	private InputStream inputStream;
	private boolean isCreated = false;
	private boolean isDisposed = false;
	private boolean forceReadBytePerByte = false;
	private Hashtable httpHeaders;
	
	public HttpConnectionInputStreamProvider(String url, boolean forceReadBytePerByte) {
		this(url, forceReadBytePerByte, true, null);
	}
	
	public HttpConnectionInputStreamProvider(String url, boolean forceReadBytePerByte, boolean throwExceptionOnBadReturnCode) {
		this(url, forceReadBytePerByte, throwExceptionOnBadReturnCode, null);
	}
	
	public HttpConnectionInputStreamProvider(String url, boolean forceReadBytePerByte, Hashtable httpHeaders) {
		this(url, forceReadBytePerByte, true, httpHeaders);
	}
	
	public HttpConnectionInputStreamProvider(String url, boolean forceReadBytePerByte, boolean throwExceptionOnBadReturnCode, Hashtable httpHeaders) {
		super();
		this.url = url;
		this.forceReadBytePerByte = forceReadBytePerByte;
		this.httpHeaders = httpHeaders;
	}

	public boolean isForceReadBytePerByte() {
		return forceReadBytePerByte;
	}

	public InputStream createInputStream() throws IOException {
		return createInputStream(false, null);
	}
	
	public InputStream createInputStream(Hashtable headers) throws IOException {
		return createInputStream(false, headers);
	}
	
	public InputStream createInputStream(boolean buffered) throws IOException {
		return createInputStream(buffered, null);
	}

	public InputStream createInputStream(boolean buffered, Hashtable httpHeaders) throws IOException {

		if (httpConnection != null) {
			throw new RuntimeException("InputStream already Opened");
		}
		Hashtable tmpHttpHeaders = mergeHttpHeaders(httpHeaders);
		httpConnection = createAndPrepareConnection(url, tmpHttpHeaders, HttpConnection.GET);
        
        logger.debug("About to open connection");
        
        int redirectCount = 0;
        int httpResponseCode = httpConnection.getResponseCode();
        while ( httpResponseCode == HttpConnection.HTTP_MOVED_PERM ||
				httpResponseCode == HttpConnection.HTTP_MOVED_TEMP ||
				httpResponseCode == HttpConnection.HTTP_SEE_OTHER ) {
			redirectCount++;
			if (redirectCount > 3) {
				String urlRedirection = httpConnection.getHeaderField(HTTP_HEADER_FIELD_LOCATION);
				httpConnection.close();
				throw new HttpAccessException(httpResponseCode, "3rd try - Http Response Code: '" + httpResponseCode + "', Actual URL redirection: '" + urlRedirection + "'");
			}
			String urlRedirection = httpConnection.getHeaderField(HTTP_HEADER_FIELD_LOCATION);
			if (logger.isDebugEnabled()) {
				logger.debug(httpResponseCode + " - Redirecting connection to following url: '" + urlRedirection + "'");
			}
			httpConnection.close();
			httpConnection = createAndPrepareConnection(urlRedirection, tmpHttpHeaders, HttpConnection.GET);

	        httpResponseCode = httpConnection.getResponseCode();
	 	}	
		
		httpResponseCode = httpConnection.getResponseCode();
		
		if (httpResponseCode != HttpConnection.HTTP_OK) {
			String httpResponseMessage = httpConnection.getResponseMessage();
			String errorMessage = httpResponseCode + " - "+ httpResponseMessage;
			logger.debug("Closing connection: "  + errorMessage);
		    throw new HttpAccessException(httpResponseCode, errorMessage);	 
		}

		inputStream = httpConnection.openInputStream();
		
		if (forceReadBytePerByte) {
			inputStream = new BytePerByteInputStream(inputStream);
		}
		
		if (buffered) {
			inputStream = new BufferedInputStream(inputStream);
		}
		
		isCreated = true;
		
		return inputStream;
	}

	private Hashtable mergeHttpHeaders(Hashtable httpHeaders) {
		Hashtable tmpHttpHeaders = new Hashtable();
		if (this.httpHeaders != null) {
            Enumeration _enum = this.httpHeaders.keys();
            while(_enum.hasMoreElements()) {
            	String key = (String)_enum.nextElement();
            	String value = (String)this.httpHeaders.get(key);
            	tmpHttpHeaders.put(key, value);
            }
		}
		if (httpHeaders != null) {
            Enumeration _enum = httpHeaders.keys();
            while(_enum.hasMoreElements()) {
            	String key = (String)_enum.nextElement();
            	String value = (String)httpHeaders.get(key);
            	tmpHttpHeaders.put(key, value);
            }
		}
		
		return tmpHttpHeaders;
	}

	private HttpConnection createAndPrepareConnection(String url, Hashtable headers, String type) throws IOException {
		HttpConnection httpConnection = (HttpConnection)Connector.open(url);
        httpConnection.setRequestMethod(type);
        setHeaders(httpConnection, headers);
        
        return httpConnection;
	}

	private void setHeaders(HttpConnection httpConnection, Hashtable headers) throws IOException {
        if (headers != null) {
            Enumeration _enum = headers.keys();
            while(_enum.hasMoreElements()) {
            	String key = (String)_enum.nextElement();
            	String value = (String)headers.get(key);
            	httpConnection.setRequestProperty(key, value);
            }
        }

        if (headers == null || !headers.contains(HTTP_HEADER_FIELD_USER_AGENT)) {
        	httpConnection.setRequestProperty(HTTP_HEADER_FIELD_USER_AGENT, DEFAULT_USER_AGENT);
        }
    }

	public void dispose() throws IOException {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		finally {
			isDisposed = true;
			if (httpConnection != null) {
				httpConnection.close();
			}
		}
	}

	public boolean isCreated() {
		return isCreated;
	}

	public boolean isDisposed() {
		return isDisposed;
	}

}
