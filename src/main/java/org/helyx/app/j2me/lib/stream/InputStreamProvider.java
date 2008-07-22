package org.helyx.app.j2me.lib.stream;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamProvider {

	InputStream createInputStream() throws IOException;
	
	void dispose() throws IOException;
	
	boolean isDisposed();
	
	boolean isCreated();
	
}
