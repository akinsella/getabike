/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.ui.xml;

import java.util.Hashtable;

import org.helyx.app.j2me.getabike.lib.xml.AbstractDomNodeProcessor;
import org.helyx.app.j2me.getabike.lib.ui.view.support.xml.XmlCanvasException;
import org.helyx.app.j2me.getabike.lib.ui.view.support.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.getabike.lib.ui.view.support.xml.XmlView;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.app.j2me.getabike.lib.xml.dom.DomAttributeProcessor;
import org.helyx.logging4me.Logger;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public class CommandNodeProcessor extends AbstractDomNodeProcessor {

	private static final Logger logger = Logger.getLogger("COMMAND_NODE_PROCESSOR");
	
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
		logger.debug("Processing node start: 'command'");
		xap.processNode(doc, elt);
		
		XmlView xmlView = (XmlView)dataMap.get(CANVAS);
		
		Command command = new Command();
		command.setText(xap.attrExists(TEXT) ? xap.getAttrValueAsString(TEXT) : "");
		command.setInteractive(xap.attrExists(ENABLED) ? xap.getAttrValueAsBoolean(ENABLED) : true);
		command.setAction(xmlView.getActionRegistry().get(xap.attrExists(ACTION) ? xap.getAttrValueAsString(ACTION) : EMPTY_ACTION));
		
		logger.debug("Command: " + command.toString());

		if (!xap.attrExists(POSITION)) {
			throw new XmlCanvasProcessingException("Command position is not provided");
		}
		
		String position = xap.getAttrValueAsString(POSITION);
		
		if (PRIMARY_STR.equals(position)) {
			xmlView.setPrimaryCommand(command);
		}
		else if (SECONDARY_STR.equals(position)) {
			xmlView.setSecondaryCommand(command);
		}
		else if (THIRD_STR.equals(position)) {
			xmlView.setThirdCommand(command);
		}
		else {
			throw new XmlCanvasException("Command position not supported: '" + position + "'");
		}
	}
	
}
