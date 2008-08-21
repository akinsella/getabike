package org.helyx.app.j2me.lib.content.accessor;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.stream.ClasspathInputStreamProvider;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;

public class ClasspathContentAccessor implements IContentAccessor {
	
	private static final Log log = LogFactory.getLog("CLASSPATH_CONTENT_ACCESSOR");

	private String classpath;
	
	public ClasspathContentAccessor(String classpath) {
		super();
		this.classpath = classpath;
	}

	public InputStreamProvider getInputStreamProvider() throws ContentAccessorException {
		log.debug("Classpath: '" + classpath + "'");
		return new ClasspathInputStreamProvider(classpath);
	}

	public String getPath() {
		return "classpath://" + classpath;
	}
	
}
