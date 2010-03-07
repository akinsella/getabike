package org.helyx.app.j2me.getabike.lib.json;

import java.io.InputStream;

public class StreamInputSource implements InputSource {

	private InputStream is;
	
	public StreamInputSource(InputStream is) {
		super();
		this.is = is;
	}

	public InputStream getInputStream() {
		return is;
	}

}
