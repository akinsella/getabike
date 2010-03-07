/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.i18n;

import java.io.InputStream;

import org.helyx.basics4me.util.Properties;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.getabike.lib.i18n.I18nConstants;
import org.helyx.app.j2me.getabike.lib.i18n.ResourceBundle;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.logging4me.Logger;



public class PropertiesResourceBundleContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = Logger.getLogger("PROPERTIES_RESOURCE_BUNDLE_CONTENT_PROVIDER");

	
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
