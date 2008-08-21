package org.helyx.app.j2me.lib.ui.xml;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.ui.view.support.xml.XmlCanvasException;
import org.helyx.app.j2me.lib.ui.view.support.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.lib.ui.view.support.xml.XmlView;
import org.helyx.app.j2me.lib.xml.AbstractDomNodeProcessor;
import org.helyx.app.j2me.lib.xml.dom.DomAttributeProcessor;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public class CanvasNodeProcessor extends AbstractDomNodeProcessor {

	private static final Log log = LogFactory.getLog("CANVAS_NODE_PROCESSOR");
	
	private static final String CANVAS = "canvas";
	private static final String COMMAND = "command";
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
		putNodeProcessor(COMMAND, new CommandNodeProcessor());
	}

	public void processNode(Document doc, Element elt, Hashtable dataMap) throws XmlCanvasProcessingException, XmlCanvasException {
		dap.processNode(doc, elt);
				
		XmlView xmlView = (XmlView)dataMap.get(CANVAS);
		
		if (!CANVAS.equals(elt.getName())) {
			throw new XmlCanvasProcessingException("Node name wanted: '" + elt.getName() + "', Node name fetched: '"  + CANVAS + "'");
		}
		
		log.debug("Processing node start: 'canvas'");
		
		xmlView.setTitle(dap.attrExists(TITLE) ? dap.getAttrValueAsString(TITLE) : null);
		xmlView.setTitleEnabled(dap.attrExists(TITLE_ENABLED) ? dap.getAttrValueAsBoolean(TITLE_ENABLED) : true);
		xmlView.setBackgroundEnabled(dap.attrExists(PAINT_BACKGROUND) ? dap.getAttrValueAsBoolean(PAINT_BACKGROUND) : true);
		xmlView.setFullScreenMode(dap.attrExists(FULL_SCREEN) ? dap.getAttrValueAsBoolean(FULL_SCREEN) : true);
		xmlView.setMenuEnabled(dap.attrExists(MENU_ENABLED) ? dap.getAttrValueAsBoolean(MENU_ENABLED) : true);

		processChildrenNodes(doc, elt, dataMap);
	}
	
}
