package org.helyx.app.j2me.lib.i18n;

import java.io.InputStream;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.basics4me.util.Properties;


public class PropertiesResourceBundleContentProvider extends AbstractContentProvider {
	
	private static final String CAT = "PROPERTIES_RESOURCE_BUNDLE_CONTENT_PROVIDER";

	
	private boolean cancel = false;

	protected IContentAccessor messageContentAccessor;

	public PropertiesResourceBundleContentProvider(IContentAccessor messageContentAccessor) {
		super();
		this.messageContentAccessor = messageContentAccessor;
	}


	public void loadData() {
		
		Log.debug(CAT, "Loading messages ...");
		
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		ResourceBundle resourceBundle = null;
		try {

			progressDispatcher.fireEvent(ProgressEventType.ON_START);
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
			progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS, resourceBundle);
		}
		catch (Throwable t) {
    		Log.warn(CAT, t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}
	}

	public void cancel() {
		cancel = true;
	}

	public String getCat() {
		return CAT;
	}
	
	public String getDescription() {
		return "Fetchs messages from path: '" + messageContentAccessor.getPath() + "'";
	}


}
