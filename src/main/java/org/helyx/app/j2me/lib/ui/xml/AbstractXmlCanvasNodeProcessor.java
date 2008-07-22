package org.helyx.app.j2me.lib.ui.xml;

import org.helyx.app.j2me.lib.reflect.RefObject;
import org.helyx.app.j2me.lib.xml.XppNodeProcessor;
import org.xmlpull.v1.XmlPullParser;

public abstract class AbstractXmlCanvasNodeProcessor implements XppNodeProcessor {

	private static final String CAT = "ABSTRACT_XML_CANVAS_NODE_PROCESSOR";
	
	protected RefObject canvasRef;
	
	public AbstractXmlCanvasNodeProcessor(RefObject canvasRef) {
		super();
		this.canvasRef = canvasRef;
	}
	
	public void processNodeStart(XmlPullParser xpp) throws XmlCanvasProcessingException {
		
	}

	public void processNodeEnd(XmlPullParser xpp) throws XmlCanvasProcessingException {
		
	}

}
