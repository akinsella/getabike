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
package org.helyx.app.j2me.getabike.lib.ui.view.support.xml;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.xml.CanvasNodeProcessor;
import org.helyx.app.j2me.getabike.lib.action.ActionRegistry;
import org.helyx.app.j2me.getabike.lib.action.EmptyAction;
import org.helyx.app.j2me.getabike.lib.ui.view.support.xml.XmlCanvasException;
import org.helyx.app.j2me.getabike.lib.ui.view.support.xml.XmlCanvasProcessingException;
import org.helyx.logging4me.Logger;

import org.kxml2.kdom.Document;
import org.xmlpull.v1.XmlPullParserException;

public class XmlView extends AbstractView {

	private static final Logger logger = Logger.getLogger("XML_VIEW");
	
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
		if (logger.isDebugEnabled()) {
			logger.debug("paint");
		}
	}

}
