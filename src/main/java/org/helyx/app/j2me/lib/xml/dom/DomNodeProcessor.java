package org.helyx.app.j2me.lib.xml.dom;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.ui.xml.XmlCanvasException;
import org.helyx.app.j2me.lib.ui.xml.XmlCanvasProcessingException;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public interface DomNodeProcessor {

	void processNode(Document doc, Element elt, Hashtable dataMap) throws XmlCanvasProcessingException, XmlCanvasException;
	
}
