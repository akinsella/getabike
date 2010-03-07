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
import org.helyx.app.j2me.getabike.lib.ui.xml.CommandNodeProcessor;
import org.helyx.app.j2me.getabike.lib.xml.dom.DomAttributeProcessor;
import org.helyx.logging4me.Logger;

import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public class CanvasNodeProcessor extends AbstractDomNodeProcessor {

	private static final Logger logger = Logger.getLogger("CANVAS_NODE_PROCESSOR");
	
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
		
		logger.debug("Processing node start: 'canvas'");
		
		xmlView.setTitle(dap.attrExists(TITLE) ? dap.getAttrValueAsString(TITLE) : null);
		xmlView.setTitleEnabled(dap.attrExists(TITLE_ENABLED) ? dap.getAttrValueAsBoolean(TITLE_ENABLED) : true);
		xmlView.setClientBackgroundEnabled(dap.attrExists(PAINT_BACKGROUND) ? dap.getAttrValueAsBoolean(PAINT_BACKGROUND) : true);
		xmlView.setFullScreenMode(dap.attrExists(FULL_SCREEN) ? dap.getAttrValueAsBoolean(FULL_SCREEN) : true);
		xmlView.setCommandEnabled(dap.attrExists(MENU_ENABLED) ? dap.getAttrValueAsBoolean(MENU_ENABLED) : true);

		processChildrenNodes(doc, elt, dataMap);
	}
	
}
