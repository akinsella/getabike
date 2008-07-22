package org.helyx.app.j2me.lib.content.accessor;

import org.helyx.app.j2me.lib.stream.InputStreamProvider;

public interface IContentAccessor {
	
	InputStreamProvider getInputStreamProvider() throws ContentAccessorException;

	String getPath();
}
