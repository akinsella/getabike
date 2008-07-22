package org.helyx.app.j2me.lib.ui.xml;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.reflect.RefObject;
import org.helyx.app.j2me.lib.xml.XppAttributeProcessor;
import org.xmlpull.v1.XmlPullParser;

public class CommandNodeProcessor extends AbstractXmlCanvasNodeProcessor {

	private static final String CAT = "COMMAND_NODE_PROCESSOR";
	
	private static final String TEXT = "text";
	private static final String ENABLED = "enabled";
	private static final String TYPE = "type";
	
	private static final String PRIMARY = "PRIMARY";
	private static final String SECONDARY = "SECONDARY";
	private static final String THIRD = "THIRD";
	
	private XppAttributeProcessor xap;
	
	public CommandNodeProcessor(RefObject canvasRef) {
		super(canvasRef);
		init();
	}

	private void init() {
		xap = new XppAttributeProcessor(new String[] { TEXT, ENABLED, TYPE });
	}
	
	public void processNodeStart(XmlPullParser xpp) throws XmlCanvasProcessingException {
		Log.debug(CAT, "Processing node start: 'command'");
		xap.processNode(xpp);
		
		RefObject commandRef = new RefObject();
		
		commandRef.setString(TEXT, xap.attrExists(TEXT) ? xap.getAttrValueAsString(TEXT) : "");
		commandRef.setBoolean(ENABLED, xap.attrExists(ENABLED) ? xap.getAttrValueAsBoolean(ENABLED) : true);

		if (!xap.attrExists(TYPE)) {
			throw new XmlCanvasProcessingException("Command type is not provided");
		}
		
		String type = xap.getAttrValueAsString(TYPE);
		if (!type.equals(PRIMARY) && 
				!type.equals(SECONDARY) && 
				!type.equals(THIRD)) {
			throw new XmlCanvasProcessingException("Command type is not supported: '" + type + "'");			
		}
		commandRef.setString(TYPE, type);
	
	}
	
}
