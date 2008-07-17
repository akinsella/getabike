package org.helyx.app.j2me.lib.stream;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.helyx.app.j2me.lib.log.Log;

public class HttpConnectionInputStreamProvider implements IInputStreamProvider {

	private static final String CAT = "HTTP_CONNECTION_INPUT_STREAM_PROVIDER";
	
	private String url;
	private HttpConnection httpConnection;
	private InputStream inputStream;
	private boolean isCreated = false;
	private boolean isDisposed = false;
	
	public HttpConnectionInputStreamProvider(String url) {
		super();
		this.url = url;
	}
	
	public InputStream createInputStream() throws IOException {
		if (httpConnection != null) {
			throw new RuntimeException("InputStream already Opened");
		}
		httpConnection = (HttpConnection)Connector.open(url);
        httpConnection.setRequestMethod(HttpConnection.GET);
        httpConnection.setRequestProperty("User-Agent", "Profile/MIDP-1.0 Configuration/CLDC-1.0");
        
        Log.debug(CAT, "About to open connection");
        
		if (httpConnection.getResponseCode() != HttpConnection.HTTP_OK) {
			Log.debug(CAT, "Closing connection: " + httpConnection.getResponseMessage());
		    throw new IOException(httpConnection.getResponseMessage());		 
		}		

		inputStream = httpConnection.openInputStream();
		isCreated = true;
		
		return inputStream;
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
