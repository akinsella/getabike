package org.helyx.app.j2me.lib.ui.view;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.ui.displayable.AbstractCanvas;
import org.helyx.app.j2me.lib.ui.xml.CanvasNodeProcessor;
import org.helyx.app.j2me.lib.ui.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.lib.ui.xml.widget.XmlCanvas;
import org.kxml2.kdom.Document;
import org.xmlpull.v1.XmlPullParserException;

public class XmlView extends AbstractCanvas {

	private static final String CAT = "XML_VIEW";
	
	private static final String CANVAS = "canvas";
	
	private static final String COMMAND = "command";

	private static final String TITLE = "title";
	private static final String TITLE_ENABLED = "titleEnabled";
	private static final String PAINT_BACKGROUND = "paintBackground";
	private static final String FULL_SCREEN = "fullScreen";
	private static final String MENU_ENABLED = "menuEnabled";

	private XmlCanvas xmlCanvas;
	
	public XmlView(MIDlet midlet) {
		super(midlet);
		init();
	}

	public XmlCanvas getXmlCanvas() {
		return xmlCanvas;
	}

	public void setXmlCanvas(XmlCanvas xmlCanvas) {
		this.xmlCanvas = xmlCanvas;
	}
	
	private void init() {
	}

	public void configure(Document doc) throws XmlPullParserException, IOException, XmlCanvasProcessingException {
		clear();
		
		CanvasNodeProcessor canvasNodeProcessor = new CanvasNodeProcessor();
		Hashtable data = new Hashtable();
		
		data.put(CANVAS, xmlCanvas);
		canvasNodeProcessor.processNode(doc, doc.getRootElement(), data);
		
		applyConfiguration();
	}

	private void applyConfiguration() {
		if (xmlCanvas.title != null && xmlCanvas.titleEnabled) {
			setTitle(xmlCanvas.title);
		}
		
		setPaintBackgroundColor(xmlCanvas.paintBackground);
		setFullScreenMode(xmlCanvas.fullScreen);
		setTitleEnabled(xmlCanvas.titleEnabled);
		setMenuEnabled(xmlCanvas.menuEnabled);
		
		canvas.repaint();
	}

	private void clear() {
		xmlCanvas = new XmlCanvas();
	}

	protected void paint(Graphics graphics) {
		Log.debug(CAT, "paint");
	}

}
