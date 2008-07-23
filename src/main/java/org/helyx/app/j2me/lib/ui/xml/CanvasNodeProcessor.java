package org.helyx.app.j2me.lib.ui.xml;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.xml.widget.XmlCanvas;
import org.helyx.app.j2me.lib.xml.AbstractDomNodeProcessor;
import org.helyx.app.j2me.lib.xml.dom.DomAttributeProcessor;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public class CanvasNodeProcessor extends AbstractDomNodeProcessor {

	private static final String CAT = "CANVAS_NODE_PROCESSOR";
	
	private static final String CANVAS = "canvas";
	private static final String TITLE = "title";
	private static final String TITLE_ENABLED = "titleEnabled";
	private static final String PAINT_BACKGROUND = "paintBackground";
	private static final String FULL_SCREEN = "fullScreen";
	private static final String MENU_ENABLED = "menuEnabled";
	
	private DomAttributeProcessor dap;
	
	public CanvasNodeProcessor() {
		super();
		init();
	}
	
	private void init() {
		dap = new DomAttributeProcessor(new String[] { TITLE, TITLE_ENABLED, PAINT_BACKGROUND, FULL_SCREEN, MENU_ENABLED });
	}

	public void processNode(Document doc, Element elt, Hashtable dataMap) throws XmlCanvasProcessingException, XmlCanvasException {
		dap.processNode(doc, elt);
				
		XmlCanvas xmlCanvas = (XmlCanvas)dataMap.get(CANVAS);
		
		if (!CANVAS.equals(elt.getName())) {
			throw new XmlCanvasProcessingException("Node name wanted: '" + elt.getName() + "', Node name fetched: '"  + CANVAS + "'");
		}
		
		Log.debug(CAT, "Processing node start: 'canvas'");
		
		xmlCanvas.title = dap.attrExists(TITLE) ? dap.getAttrValueAsString(TITLE) : null;
		xmlCanvas.titleEnabled = dap.attrExists(TITLE_ENABLED) ? dap.getAttrValueAsBoolean(TITLE_ENABLED) : true;
		xmlCanvas.paintBackground = dap.attrExists(PAINT_BACKGROUND) ? dap.getAttrValueAsBoolean(PAINT_BACKGROUND) : true;
		xmlCanvas.fullScreen = dap.attrExists(FULL_SCREEN) ? dap.getAttrValueAsBoolean(FULL_SCREEN) : true;
		xmlCanvas.menuEnabled = dap.attrExists(MENU_ENABLED) ? dap.getAttrValueAsBoolean(MENU_ENABLED) : true;

		processChildrenNodes(doc, elt, dataMap);
	}
	
}
