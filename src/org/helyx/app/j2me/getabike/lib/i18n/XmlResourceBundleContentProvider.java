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

import org.helyx.app.j2me.getabike.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.getabike.lib.constant.EncodingConstants;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.getabike.lib.i18n.ResourceBundle;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;

import org.xmlpull.v1.XmlPullParser;


public class XmlResourceBundleContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = Logger.getLogger("XML_RESOURCE_BUNDLE_CONTENT_PROVIDER");

	private static final String ENTRY = "entry";
	
	private static final String KEY = "key";
	private static final String MESSAGE = "message";
	
	private boolean cancel = false;

	protected IContentAccessor messageContentAccessor;

	public XmlResourceBundleContentProvider(IContentAccessor messageContentAccessor) {
		super();
		this.messageContentAccessor = messageContentAccessor;
	}


	public void loadData() {
		
		logger.debug("Loading messages ...");
		
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		ResourceBundle resourceBundle = null;
		try {

			progressDispatcher.fireEvent(EventType.ON_START);
			try {
				
				cartoInputStreamProvider = messageContentAccessor.getInputStreamProvider();
				inputStream = cartoInputStreamProvider.createInputStream();
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);

				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { KEY, MESSAGE });

				resourceBundle = new ResourceBundle();
				while (XppUtil.readToNextElement(xpp, ENTRY)) {
					if (cancel) {
						progressDispatcher.fireEvent(EventType.ON_CANCEL);
						return ;
					}
					xppAttributeProcessor.processNode(xpp);

					String key = xppAttributeProcessor.getAttrValueAsString(KEY);
					String message = xppAttributeProcessor.getAttrValueAsString(MESSAGE);
					
					resourceBundle.put(key, message);
				}
						
				progressDispatcher.fireEvent(EventType.ON_CUSTOM, resourceBundle);
			}
			finally {
				cartoInputStreamProvider.dispose();
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
