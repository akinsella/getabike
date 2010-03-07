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
package org.helyx.app.j2me.getabike.lib.xml.xpp;

import java.io.IOException;
import java.util.Hashtable;

import org.helyx.app.j2me.getabike.lib.ui.view.support.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.getabike.lib.xml.xpp.XppNodePath;
import org.helyx.app.j2me.getabike.lib.xml.xpp.XppNodeProcessor;
import org.helyx.logging4me.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XppTreeWalker {

	private static final Logger logger = Logger.getLogger("XPP_TREE_WALKER");
	
	private Hashtable nodeProcessorMap = new Hashtable();
	private XppNodePath xppNodePath = new XppNodePath();
	
	public XppTreeWalker() {
		super();
	}
	
	public void containsNodeProcessorByPath(String nodePath) {
		nodeProcessorMap.containsKey(nodePath);
	}
	
	public void setNodeProcessor(String nodePath, XppNodeProcessor xnp) {
		logger.debug("Associating NodePath '" + nodePath + "' to XppNodeProcessor: '" + xnp + "'");
		nodeProcessorMap.put(nodePath, xnp);
	}
	
	public void removeNodeProcessor(String nodePath) {
		logger.debug("Removing NodePath association '" + nodePath + "'");
		nodeProcessorMap.remove(nodePath);
	}
	
	public void removeAllNodeProcessors() {
		logger.debug("Removing All NodePath associations");
		nodeProcessorMap.clear();
	}
	
	public void processNodes(XmlPullParser xpp) throws XmlPullParserException, IOException, XmlCanvasProcessingException {

		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			logger.debug("Xpp eventType name: " + XmlPullParser.TYPES[eventType]);
			if (eventType == XmlPullParser.START_TAG) {
				String nodeName = xpp.getName();
				logger.debug("[START_TAG] Xpp element name: " + nodeName);
				xppNodePath.push(nodeName);
				String xmlPath = xppNodePath.toXmlPath();
				logger.debug("[START_TAG] Xpp element path: " + xmlPath);
				XppNodeProcessor xnp = (XppNodeProcessor)nodeProcessorMap.get(xmlPath);
				logger.debug("[START_TAG] Found XppNodeProcessor: '" + xnp + "' for XmlPath: '" + xmlPath + "'");
				if (xnp != null) {
					xnp.processNodeStart(xpp);
				}
			}
			else if (eventType == XmlPullParser.END_TAG) {
				String xmlPath = xppNodePath.toXmlPath();
				logger.debug("[END_TAG] Xpp element path: " + xmlPath);
				XppNodeProcessor xnp = (XppNodeProcessor)nodeProcessorMap.get(xmlPath);
				logger.debug("[END_TAG] Found XppNodeProcessor: '" + xnp + "' for XmlPath: '" + xmlPath + "'");
				if (xnp != null) {
					xnp.processNodeEnd(xpp);
				}
				xppNodePath.pop();			
			}

			eventType = xpp.next();
		}
	}
	
}
