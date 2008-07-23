package org.helyx.app.j2me.lib.ui.xml;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.xml.widget.XmlCanvas;
import org.helyx.app.j2me.lib.ui.xml.widget.Command;
import org.helyx.app.j2me.lib.xml.AbstractDomNodeProcessor;
import org.helyx.app.j2me.lib.xml.dom.DomAttributeProcessor;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public class CommandNodeProcessor extends AbstractDomNodeProcessor {

	private static final String CAT = "COMMAND_NODE_PROCESSOR";
	
	private static final String CANVAS = "canvas";
	
	private static final String TEXT = "text";
	private static final String ENABLED = "enabled";
	private static final String POSITION = "position";
	
	
	private DomAttributeProcessor xap;

	public CommandNodeProcessor() {
		super();
		init();
	}

	private void init() {
		xap = new DomAttributeProcessor(new String[] { TEXT, ENABLED, POSITION });
	}

	public void processNode(Document doc, Element elt, Hashtable dataMap) throws XmlCanvasProcessingException, XmlCanvasException {
		Log.debug(CAT, "Processing node start: 'command'");
		xap.processNode(doc, elt);
		
		XmlCanvas xmlCanvas = (XmlCanvas)dataMap.get(CANVAS);
		
		Command command = new Command();
		command.text = xap.attrExists(TEXT) ? xap.getAttrValueAsString(TEXT) : "";
		command.enabled = xap.attrExists(ENABLED) ? xap.getAttrValueAsBoolean(ENABLED) : true;

		if (!xap.attrExists(POSITION)) {
			throw new XmlCanvasProcessingException("Command type is not provided");
		}
		
		String position = xap.getAttrValueAsString(POSITION);

		command.position = Command.convertPositionToInt(position);
		
		switch (command.position) {
			case Command.PRIMARY:
				xmlCanvas.primaryAction = command;
				break;
			case Command.SECONDARY:
				xmlCanvas.secondaryAction = command;
				break;
			case Command.THIRD:
				xmlCanvas.thirdAction = command;
				break;
		}
	}
	
}
