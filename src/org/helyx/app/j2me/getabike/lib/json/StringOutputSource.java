package org.helyx.app.j2me.getabike.lib.json;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class StringOutputSource implements OutputSource {

	public ByteArrayOutputStream bos;
	
	public StringOutputSource() {
		super();
		bos = new ByteArrayOutputStream();
	}

	public OutputStream getOutputStream() {
		return bos;
	}

	public String getContent() {
		return new String(bos.toByteArray());
	}
}
