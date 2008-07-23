package org.helyx.app.j2me.lib.xml;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.xml.dom.DomNodeProcessor;
import org.helyx.app.j2me.lib.xml.xpp.XppNodeProcessor;

public abstract class AbstractXmlNodeProcessor implements DomNodeProcessor {
	
	private static final String CAT = "ABSTRACT_XML_NODE_PROCESSOR";
	
	private Hashtable nodeProcessorMap = new Hashtable();
	
	public AbstractXmlNodeProcessor() {
		super();
	}
	
	public void containsNodeProcessorForNodeName(String nodeName) {
		nodeProcessorMap.containsKey(nodeName);
	}
	
	public void setNodeProcessor(String nodePath, XppNodeProcessor xnp) {
		Log.debug(CAT, "Associating NodePath '" + nodePath + "' to XppNodeProcessor: '" + xnp + "'");
		nodeProcessorMap.put(nodePath, xnp);
	}
	
	public void removeNodeProcessor(String nodeName) {
		Log.debug(CAT, "Removing NodePath association '" + nodeName + "'");
		nodeProcessorMap.remove(nodeName);
	}
	
	public void removeAllNodeProcessors() {
		Log.debug(CAT, "Removing All NodePath associations");
		nodeProcessorMap.clear();
	}

}
