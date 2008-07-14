package org.helyx.app.j2me.lib.content.accessor;

import org.helyx.app.j2me.lib.stream.IInputStreamProvider;

public interface IContentAccessor {
	
	IInputStreamProvider getInputStreamProvider() throws ContentAccessorException;

	String getPath();
}
