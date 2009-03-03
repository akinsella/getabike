package org.helyx.app.j2me.lib.content.accessor;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.stream.ClasspathInputStreamProvider;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;

public class ClasspathContentAccessor implements IContentAccessor {
	
	private static final Logger logger = LoggerFactory.getLogger("CLASSPATH_CONTENT_ACCESSOR");

	private String classpath;
	
	public ClasspathContentAccessor(String classpath) {
		super();
		this.classpath = classpath;
	}

	public InputStreamProvider getInputStreamProvider() throws ContentAccessorException {
		logger.debug("Classpath: '" + classpath + "'");
		return new ClasspathInputStreamProvider(classpath);
	}

	public String getPath() {
		return "classpath://" + classpath;
	}
	
}
