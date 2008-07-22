package org.helyx.app.j2me.lib.ui.xml;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.reflect.RefObject;
import org.helyx.app.j2me.lib.xml.XppAttributeProcessor;
import org.xmlpull.v1.XmlPullParser;

public class CanvasNodeProcessor extends AbstractXmlCanvasNodeProcessor {

	private static final String CAT = "CANVAS_NODE_PROCESSOR";
	
	private static final String TITLE = "title";
	private static final String TITLE_ENABLED = "titleEnabled";
	private static final String PAINT_BACKGROUND = "paintBackground";
	private static final String FULL_SCREEN = "fullScreen";
	private static final String MENU_ENABLED = "menuEnabled";

	private XppAttributeProcessor xap;
	
	public CanvasNodeProcessor(RefObject canvasRef) {
		super(canvasRef);
		init();
	}

	private void init() {
		xap = new XppAttributeProcessor(new String[] { TITLE });
	}
	
	public void processNodeStart(XmlPullParser xpp) throws XmlCanvasProcessingException {
		Log.debug(CAT, "Processing node start: 'canvas'");
		xap.processNode(xpp);


		if (xap.attrExists(TITLE)) {
			String title = xap.getAttrValueAsString(TITLE);
			canvasRef.setString(TITLE, title);
		}
		
		if (xap.attrExists(TITLE_ENABLED)) {
			boolean titleEnabled = xap.getAttrValueAsBoolean(TITLE_ENABLED);
			canvasRef.setBoolean(TITLE_ENABLED, titleEnabled);
		}
		else {
			canvasRef.setBoolean(TITLE_ENABLED, true);
		}

		if (xap.attrExists(PAINT_BACKGROUND)) {
			boolean paintBackground = xap.getAttrValueAsBoolean(PAINT_BACKGROUND);
			canvasRef.setBoolean(PAINT_BACKGROUND, paintBackground);
		}
		else {
			canvasRef.setBoolean(PAINT_BACKGROUND, true);
		}

		if (xap.attrExists(FULL_SCREEN)) {
			boolean fullScreen = xap.getAttrValueAsBoolean(FULL_SCREEN);
			canvasRef.setBoolean(FULL_SCREEN, fullScreen);
		}
		else {
			canvasRef.setBoolean(FULL_SCREEN, true);
		}

		if (xap.attrExists(MENU_ENABLED)) {
			boolean menuEnabled = xap.getAttrValueAsBoolean(MENU_ENABLED);
			canvasRef.setBoolean(MENU_ENABLED, menuEnabled);
		}
		else {
			canvasRef.setBoolean(MENU_ENABLED, true);
		}

		
	}
	
}
