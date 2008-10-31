package org.helyx.app.j2me.lib.i18n;

import java.io.InputStream;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.basics4me.util.Properties;


public class PropertiesResourceBundleContentProvider extends AbstractContentProvider {
	
	private static final Log log = LogFactory.getLog("PROPERTIES_RESOURCE_BUNDLE_CONTENT_PROVIDER");

	
	private boolean cancel = false;

	protected IContentAccessor messageContentAccessor;

	public PropertiesResourceBundleContentProvider(IContentAccessor messageContentAccessor) {
		super();
		this.messageContentAccessor = messageContentAccessor;
	}


	public void loadData() {
		
		log.debug("Loading messages ...");
		
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		ResourceBundle resourceBundle = null;
		try {

			progressDispatcher.fireEvent(EventType.ON_START);
			try {
				
				cartoInputStreamProvider = messageContentAccessor.getInputStreamProvider();
				inputStream = cartoInputStreamProvider.createInputStream();
				Properties properties = new Properties();
				properties.load(inputStream);
				resourceBundle = new ResourceBundle();
				resourceBundle.putAll(properties);
						
				progressDispatcher.fireEvent(I18nConstants.ON_MESSAGES_LOADED, resourceBundle);
			}
			finally {
				cartoInputStreamProvider.dispose();
			}
			progressDispatcher.fireEvent(EventType.ON_SUCCESS, resourceBundle);
		}
		catch (Throwable t) {
    		log.warn(t);
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
