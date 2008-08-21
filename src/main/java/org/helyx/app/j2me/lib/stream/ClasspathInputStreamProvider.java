package org.helyx.app.j2me.lib.stream;

import java.io.IOException;
import java.io.InputStream;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class ClasspathInputStreamProvider implements InputStreamProvider {

	private static final Log log = LogFactory.getLog("CLASSPATH_INPUT_STREAM_PROVIDER");
	
	private String path;
	private InputStream inputStream;
	private boolean isCreated = false;
	private boolean isDisposed = false;
	
	public ClasspathInputStreamProvider(String path) {
		super();
		this.path = path;
	}
	
	public InputStream createInputStream() throws IOException {
	
		inputStream = StreamUtil.createsFromClassPath(path);
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
		}
	}

	public boolean isCreated() {
		return isCreated;
	}

	public boolean isDisposed() {
		return isDisposed;
	}

}
