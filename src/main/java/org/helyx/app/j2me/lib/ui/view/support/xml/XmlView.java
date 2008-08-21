package org.helyx.app.j2me.lib.ui.view.support.xml;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.action.ActionRegistry;
import org.helyx.app.j2me.lib.action.EmptyAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.view.AbstractView;
import org.helyx.app.j2me.lib.ui.xml.CanvasNodeProcessor;
import org.kxml2.kdom.Document;
import org.xmlpull.v1.XmlPullParserException;

public class XmlView extends AbstractView {

	private static final Log log = LogFactory.getLog("XML_VIEW");
	
	private static final String CANVAS = "canvas";
	
	private static final String EMPTY_ACTION = "empty.action";
	
	private ActionRegistry actionRegistry; 
	
	public XmlView(AbstractMIDlet midlet) {
		super(midlet);
		init();
	}
	
	private void init() {
		actionRegistry = new ActionRegistry();
		actionRegistry.put(EMPTY_ACTION, new EmptyAction());
	}

	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	public void configure(Document doc) throws XmlPullParserException, IOException, XmlCanvasProcessingException, XmlCanvasException {
		CanvasNodeProcessor canvasNodeProcessor = new CanvasNodeProcessor();
		Hashtable data = new Hashtable();
		
		data.put(CANVAS, this);
		canvasNodeProcessor.processNode(doc, doc.getRootElement(), data);
	}

	protected void paint(Graphics graphics) {
		log.debug("paint");
	}

}
