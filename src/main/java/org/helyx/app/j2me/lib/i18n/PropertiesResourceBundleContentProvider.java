package org.helyx.app.j2me.lib.i18n;

import java.io.InputStream;

import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.properties.Properties;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;


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
		
		Theme theme = null;
		try {

			progressDispatcher.fireEvent(ProgressEventType.ON_START);
			try {
				
				cartoInputStreamProvider = messageContentAccessor.getInputStreamProvider();
				inputStream = cartoInputStreamProvider.createInputStream();
				Properties properties = new Properties();
				properties.load(inputStream);
				theme = new Theme();
				theme.putAll(properties);
						
				progressDispatcher.fireEvent(I18nConstants.ON_MESSAGES_LOADED, theme);
			}
			finally {
				cartoInputStreamProvider.dispose();
			}
			progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS, theme);
		}
		catch (Throwable t) {
    		Log.warn(CAT, t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}
	}

	public void cancel() {
		cancel = true;
	}

	protected String getCat() {
		return CAT;
	}
	
	public String getDescription() {
		return "Fetchs messages from path: '" + messageContentAccessor.getPath() + "'";
	}


}
