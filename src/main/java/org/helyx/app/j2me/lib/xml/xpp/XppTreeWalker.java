package org.helyx.app.j2me.lib.xml.xpp;

import java.io.IOException;
import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.ui.view.support.xml.XmlCanvasProcessingException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XppTreeWalker {

	private static final Log log = LogFactory.getLog("XPP_TREE_WALKER");
	
	private Hashtable nodeProcessorMap = new Hashtable();
	private XppNodePath xppNodePath = new XppNodePath();
	
	public XppTreeWalker() {
		super();
	}
	
	public void containsNodeProcessorByPath(String nodePath) {
		nodeProcessorMap.containsKey(nodePath);
	}
	
	public void setNodeProcessor(String nodePath, XppNodeProcessor xnp) {
		log.debug("Associating NodePath '" + nodePath + "' to XppNodeProcessor: '" + xnp + "'");
		nodeProcessorMap.put(nodePath, xnp);
	}
	
	public void removeNodeProcessor(String nodePath) {
		log.debug("Removing NodePath association '" + nodePath + "'");
		nodeProcessorMap.remove(nodePath);
	}
	
	public void removeAllNodeProcessors() {
		log.debug("Removing All NodePath associations");
		nodeProcessorMap.clear();
	}
	
	public void processNodes(XmlPullParser xpp) throws XmlPullParserException, IOException, XmlCanvasProcessingException {

		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			log.debug("Xpp eventType name: " + XmlPullParser.TYPES[eventType]);
			if (eventType == XmlPullParser.START_TAG) {
				String nodeName = xpp.getName();
				log.debug("[START_TAG] Xpp element name: " + nodeName);
				xppNodePath.push(nodeName);
				String xmlPath = xppNodePath.toXmlPath();
				log.debug("[START_TAG] Xpp element path: " + xmlPath);
				XppNodeProcessor xnp = (XppNodeProcessor)nodeProcessorMap.get(xmlPath);
				log.debug("[START_TAG] Found XppNodeProcessor: '" + xnp + "' for XmlPath: '" + xmlPath + "'");
				if (xnp != null) {
					xnp.processNodeStart(xpp);
				}
			}
			else if (eventType == XmlPullParser.END_TAG) {
				String xmlPath = xppNodePath.toXmlPath();
				log.debug("[END_TAG] Xpp element path: " + xmlPath);
				XppNodeProcessor xnp = (XppNodeProcessor)nodeProcessorMap.get(xmlPath);
				log.debug("[END_TAG] Found XppNodeProcessor: '" + xnp + "' for XmlPath: '" + xmlPath + "'");
				if (xnp != null) {
					xnp.processNodeEnd(xpp);
				}
				xppNodePath.pop();			
			}

			eventType = xpp.next();
		}
	}
	
}
