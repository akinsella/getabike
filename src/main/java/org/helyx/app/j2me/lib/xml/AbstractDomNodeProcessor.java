package org.helyx.app.j2me.lib.xml;

import java.util.Enumeration;
import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.ui.view.support.xml.XmlCanvasException;
import org.helyx.app.j2me.lib.ui.view.support.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.lib.util.MapUtil;
import org.helyx.app.j2me.lib.xml.dom.DomNodeProcessor;
import org.helyx.app.j2me.lib.xml.dom.DomUtil;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public abstract class AbstractDomNodeProcessor implements DomNodeProcessor {
	
	private static final Log log = LogFactory.getLog("ABSTRACT_XML_NODE_PROCESSOR");
	
	private Hashtable childNodeProcessorMap = new Hashtable();
	
	public AbstractDomNodeProcessor() {
		super();
	}
	
	public boolean containsChildNodeProcessor(String nodeName) {
		return childNodeProcessorMap.containsKey(nodeName);
	}
	
	public void putNodeProcessor(String nodePath, DomNodeProcessor dnp) {
		log.debug("Associating NodePath '" + nodePath + "' to XppNodeProcessor: '" + dnp + "'");
		childNodeProcessorMap.put(nodePath, dnp);
	}
	
	public void removeNodeProcessor(String nodeName) {
		log.debug("Removing NodePath association '" + nodeName + "'");
		childNodeProcessorMap.remove(nodeName);
	}
	
	public void removeAllNodeProcessors() {
		log.debug("Removing All NodePath associations");
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
