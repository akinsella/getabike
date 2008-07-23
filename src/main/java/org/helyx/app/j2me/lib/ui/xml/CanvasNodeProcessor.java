package org.helyx.app.j2me.lib.ui.xml;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.xml.widget.XmlCanvas;
import org.helyx.app.j2me.lib.xml.AbstractXmlNodeProcessor;
import org.helyx.app.j2me.lib.xml.dom.DomAttributeProcessor;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public class CanvasNodeProcessor extends AbstractXmlNodeProcessor {

	private static final String CAT = "CANVAS_NODE_PROCESSOR";
	
	private static final String CANVAS = "canvas";
	private static final String TITLE = "title";
	private static final String TITLE_ENABLED = "titleEnabled";
	private static final String PAINT_BACKGROUND = "paintBackground";
	private static final String FULL_SCREEN = "fullScreen";
	private static final String MENU_ENABLED = "menuEnabled";
	
	private DomAttributeProcessor dap;
	private XmlCanvas xmlCanvas;
	
	public CanvasNodeProcessor() {
		super();
		init();
	}
	
	private void init() {
		dap = new DomAttributeProcessor(new String[] { TITLE, TITLE_ENABLED, PAINT_BACKGROUND, FULL_SCREEN, MENU_ENABLED });
	}

	public void processNode(Document doc, Element elt, Hashtable data) throws XmlCanvasProcessingException {
		dap.processNode(doc, elt);
				
		if (!CANVAS.equals(elt.getName())) {
			throw new XmlCanvasProcessingException("Node name wanted: '" + elt.getName() + "', Node name fetched: '"  + CANVAS + "'");
		}
		
		Log.debug(CAT, "Processing node start: 'canvas'");

		if (dap.attrExists(TITLE)) {
			String title = dap.getAttrValueAsString(TITLE);
			xmlCanvas.title = title;
		}
		
		if (dap.attrExists(TITLE_ENABLED)) {
			boolean titleEnabled = dap.getAttrValueAsBoolean(TITLE_ENABLED);
			xmlCanvas.titleEnabled = titleEnabled;
		}
		else {
			xmlCanvas.titleEnabled = true;
		}

		if (dap.attrExists(PAINT_BACKGROUND)) {
			boolean paintBackground = dap.getAttrValueAsBoolean(PAINT_BACKGROUND);
			xmlCanvas.paintBackground = paintBackground;
		}
		else {
			xmlCanvas.paintBackground = true;
		}

		if (dap.attrExists(FULL_SCREEN)) {
			boolean fullScreen = dap.getAttrValueAsBoolean(FULL_SCREEN);
			xmlCanvas.fullScreen = fullScreen;
		}
		else {
			xmlCanvas.fullScreen = true;
		}

		if (dap.attrExists(MENU_ENABLED)) {
			boolean menuEnabled = dap.getAttrValueAsBoolean(MENU_ENABLED);
			xmlCanvas.menuEnabled = menuEnabled;
		}
		else {
			xmlCanvas.menuEnabled = true;
		}
	}
	
}
