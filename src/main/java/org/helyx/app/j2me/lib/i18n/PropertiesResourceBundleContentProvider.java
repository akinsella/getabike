package org.helyx.app.j2me.lib.i18n;

import java.io.InputStream;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.basics4me.util.Properties;


public class PropertiesResourceBundleContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = LoggerFactory.getLogger("PROPERTIES_RESOURCE_BUNDLE_CONTENT_PROVIDER");

	
	private boolean cancel = false;

	protected IContentAccessor messageContentAccessor;

	public PropertiesResourceBundleContentProvider(IContentAccessor messageContentAccessor) {
		super();
		this.messageContentAccessor = messageContentAccessor;
	}


	public void loadData() {
		
		logger.debug("Loading messages ...");
		
		InputStream inputStream = null;
		InputStreamProvider messageInputStreamProvider = null;
		
		ResourceBundle resourceBundle = null;
		try {

			progressDispatcher.fireEvent(EventType.ON_START);
			try {
				
				messageInputStreamProvider = messageContentAccessor.getInputStreamProvider();
				inputStream = messageInputStreamProvider.createInputStream();
				Properties properties = new Properties();
				properties.load(inputStream);
				resourceBundle = new ResourceBundle();
				resourceBundle.putAll(properties);
						
				progressDispatcher.fireEvent(I18nConstants.ON_MESSAGES_LOADED, resourceBundle);
			}
			finally {
				messageInputStreamProvider.dispose();
			}
			progressDispatcher.fireEvent(EventType.ON_SUCCESS, resourceBundle);
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
		return "Fetchs messages from path: '" + messageContentAccessor.getPath() + "'";
	}


}
