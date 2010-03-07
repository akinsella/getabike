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
package org.helyx.app.j2me.getabike.lib.xml;

import java.util.Enumeration;
import java.util.Hashtable;

import org.helyx.app.j2me.getabike.lib.ui.view.support.xml.XmlCanvasException;
import org.helyx.app.j2me.getabike.lib.ui.view.support.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.getabike.lib.util.MapUtil;
import org.helyx.app.j2me.getabike.lib.xml.dom.DomNodeProcessor;
import org.helyx.app.j2me.getabike.lib.xml.dom.DomUtil;
import org.helyx.logging4me.Logger;

import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public abstract class AbstractDomNodeProcessor implements DomNodeProcessor {
	
	private static final Logger logger = Logger.getLogger("ABSTRACT_XML_NODE_PROCESSOR");
	
	private Hashtable childNodeProcessorMap = new Hashtable();
	
	public AbstractDomNodeProcessor() {
		super();
	}
	
	public boolean containsChildNodeProcessor(String nodeName) {
		return childNodeProcessorMap.containsKey(nodeName);
	}
	
	public void putNodeProcessor(String nodePath, DomNodeProcessor dnp) {
		logger.debug("Associating NodePath '" + nodePath + "' to XppNodeProcessor: '" + dnp + "'");
		childNodeProcessorMap.put(nodePath, dnp);
	}
	
	public void removeNodeProcessor(String nodeName) {
		logger.debug("Removing NodePath association '" + nodeName + "'");
		childNodeProcessorMap.remove(nodeName);
	}
	
	public void removeAllNodeProcessors() {
		logger.debug("Removing All NodePath associations");
		childNodeProcessorMap.clear();
	}
	
	public void processChildrenNodes(Document doc, Element parentElt, Hashtable dataMap) throws XmlCanvasProcessingException, XmlCanvasException {
		Enumeration _enum = DomUtil.elementIterator(parentElt);
		
		while (_enum.hasMoreElements()) {
			Element childElt = (Element)_enum.nextElement();
			String childEltname = childElt.getName();
			if (containsChildNodeProcessor(childEltname)) {
				Hashtable clonedDataMap = MapUtil.duplicate(dataMap);
				DomNodeProcessor dnp = (DomNodeProcessor)childNodeProcessorMap.get(childEltname);
				dnp.processNode(doc, childElt, clonedDataMap);
			}
		}
	}

}
