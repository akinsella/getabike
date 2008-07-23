package org.helyx.app.j2me.lib.xml;

import java.util.Enumeration;
import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.view.xml.XmlCanvasException;
import org.helyx.app.j2me.lib.ui.view.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.lib.util.MapUtil;
import org.helyx.app.j2me.lib.xml.dom.DomNodeProcessor;
import org.helyx.app.j2me.lib.xml.dom.DomUtil;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public abstract class AbstractDomNodeProcessor implements DomNodeProcessor {
	
	private static final String CAT = "ABSTRACT_XML_NODE_PROCESSOR";
	
	private Hashtable childNodeProcessorMap = new Hashtable();
	
	public AbstractDomNodeProcessor() {
		super();
	}
	
	public boolean containsChildNodeProcessor(String nodeName) {
		return childNodeProcessorMap.containsKey(nodeName);
	}
	
	public void putNodeProcessor(String nodePath, DomNodeProcessor dnp) {
		Log.debug(CAT, "Associating NodePath '" + nodePath + "' to XppNodeProcessor: '" + dnp + "'");
		childNodeProcessorMap.put(nodePath, dnp);
	}
	
	public void removeNodeProcessor(String nodeName) {
		Log.debug(CAT, "Removing NodePath association '" + nodeName + "'");
		childNodeProcessorMap.remove(nodeName);
	}
	
	public void removeAllNodeProcessors() {
		Log.debug(CAT, "Removing All NodePath associations");
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
