package org.helyx.app.j2me.lib.ui.xml;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.view.xml.XmlCanvasException;
import org.helyx.app.j2me.lib.ui.view.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.lib.ui.view.xml.XmlView;
import org.helyx.app.j2me.lib.ui.widget.Command;
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
	private static final String ACTION = "action";

	private static final String PRIMARY_STR = "PRIMARY";
	private static final String SECONDARY_STR = "SECONDARY";
	private static final String THIRD_STR = "THIRD";
	
	private static final String EMPTY_ACTION = "empty.action";
	
	private DomAttributeProcessor xap;

	public CommandNodeProcessor() {
		super();
		init();
	}

	private void init() {
		xap = new DomAttributeProcessor(new String[] { TEXT, ENABLED, POSITION, ACTION });
	}

	public void processNode(Document doc, Element elt, Hashtable dataMap) throws XmlCanvasProcessingException, XmlCanvasException {
		Log.debug(CAT, "Processing node start: 'command'");
		xap.processNode(doc, elt);
		
		XmlView xmlView = (XmlView)dataMap.get(CANVAS);
		
		Command command = new Command();
		command.setText(xap.attrExists(TEXT) ? xap.getAttrValueAsString(TEXT) : "");
		command.setEnabled(xap.attrExists(ENABLED) ? xap.getAttrValueAsBoolean(ENABLED) : true);
		command.setAction(xmlView.getActionRegistry().get(xap.attrExists(ACTION) ? xap.getAttrValueAsString(ACTION) : EMPTY_ACTION));
		
		Log.debug("Command: " + command.toString());

		if (!xap.attrExists(POSITION)) {
			throw new XmlCanvasProcessingException("Command position is not provided");
		}
		
		String position = xap.getAttrValueAsString(POSITION);
		
		if (PRIMARY_STR.equals(position)) {
			xmlView.setPrimaryAction(command);
		}
		else if (SECONDARY_STR.equals(position)) {
			xmlView.setSecondaryAction(command);
		}
		else if (THIRD_STR.equals(position)) {
			xmlView.setThirdAction(command);
		}
		else {
			throw new XmlCanvasException("Command position not supported: '" + position + "'");
		}
	}
	
}