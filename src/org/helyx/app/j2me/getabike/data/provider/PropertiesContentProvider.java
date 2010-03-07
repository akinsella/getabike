package org.helyx.app.j2me.getabike.data.provider;

import java.io.InputStream;

import org.helyx.basics4me.util.Properties;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.logging4me.Logger;

public class PropertiesContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = Logger.getLogger("DEFAULT_CITY_CONTENT_PROVIDER");
	
	private boolean cancel = false;

	private IContentAccessor propertiesContentAccessor;

	public PropertiesContentProvider() {
		super();
	}

	public PropertiesContentProvider(IContentAccessor propertiesContentAccessor) {
		super();
		this.propertiesContentAccessor = propertiesContentAccessor;
	}


	public void loadData() {
		if (logger.isDebugEnabled()) {
			logger.debug("Loading properties ...");
		}
		
		InputStreamProvider propertiesInputStreamProvider = null;
		
		Properties properties = new Properties();
		try {
			progressDispatcher.fireEvent(EventType.ON_START);
			try {			
				propertiesInputStreamProvider = propertiesContentAccessor.getInputStreamProvider();
				InputStream inputStream = propertiesInputStreamProvider.createInputStream(true);
				properties.load(inputStream);
			}
			finally {
				propertiesInputStreamProvider.dispose();
			}			
			progressDispatcher.fireEvent(EventType.ON_SUCCESS, properties);
		}
		catch (Throwable t) {
    		logger.warn(t);
			progressDispatcher.fireEvent(EventType.ON_ERROR, t);
		}
	}

	public void cancel() {
		cancel = true;
	}

	public String getDescription() {
		return "Fetchs properties from path: '" + propertiesContentAccessor.getPath() + "'";
	}

}
