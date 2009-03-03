package org.helyx.app.j2me.lib.xml;

import java.util.Enumeration;
import java.util.Hashtable;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.ui.view.support.xml.XmlCanvasException;
import org.helyx.app.j2me.lib.ui.view.support.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.lib.util.MapUtil;
import org.helyx.app.j2me.lib.xml.dom.DomNodeProcessor;
import org.helyx.app.j2me.lib.xml.dom.DomUtil;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public abstract class AbstractDomNodeProcessor implements DomNodeProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger("ABSTRACT_XML_NODE_PROCESSOR");
	
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
