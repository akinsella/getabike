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

import org.helyx.app.j2me.getabike.lib.xml.AbstractXmlAttributeProcessor;
import org.helyx.logging4me.Logger;

import org.xmlpull.v1.XmlPullParser;

public class XppAttributeProcessor extends AbstractXmlAttributeProcessor {
	
	private static final Logger logger = Logger.getLogger("XPP_ATTRIBUTE_PROCESSOR");
		
	public XppAttributeProcessor() {
		super();
	}
	
	public XppAttributeProcessor(String attributeName) {
		super(attributeName);
	}
	
	public XppAttributeProcessor(String[] attributeNames) {
		super(attributeNames);
	}

	public void processNode(XmlPullParser xpp) {
		attributeMap.clear();
		int attrCount = xpp.getAttributeCount();
		logger.debug("Element '" + xpp.getName() + "' attribute count: " + attrCount);
		for (int i = 0 ; i < attrCount ; i++) {
			String attributeName = xpp.getAttributeName(i);
			if (attributeList.contains(attributeName)) {
				logger.debug("Attribute[" + i + "] for Element[" + xpp.getName() + "] name: '" + attributeName + "'");
				String attributeValue = xpp.getAttributeValue(i);
				logger.debug("Associating value: '" +  attributeValue + "' to attribute name='" + attributeName + "'");
				attributeMap.put(attributeName, attributeValue);
			}
		}
	}

}
