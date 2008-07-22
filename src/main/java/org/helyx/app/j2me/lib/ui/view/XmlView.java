package org.helyx.app.j2me.lib.ui.view;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.reflect.RefObject;
import org.helyx.app.j2me.lib.ui.displayable.AbstractCanvas;
import org.helyx.app.j2me.lib.ui.xml.CanvasNodeProcessor;
import org.helyx.app.j2me.lib.ui.xml.CommandNodeProcessor;
import org.helyx.app.j2me.lib.ui.xml.XmlCanvasProcessingException;
import org.helyx.app.j2me.lib.xml.XppNodePath;
import org.helyx.app.j2me.lib.xml.XppTreeWalker;
import org.xmlpull.v1.XmlPullParser;
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
	
	private RefObject canvasRef;

	public XmlView(MIDlet midlet) {
		super(midlet);
		init();
	}
	
	private void init() {
		canvasRef = new RefObject();
	}
	
	public RefObject getCanvasRef() {
		return canvasRef;
	}

	public void setCanvasRef(RefObject canvasRef) {
		this.canvasRef = canvasRef;
	}
	
	public void configure(XmlPullParser xpp) throws XmlPullParserException, IOException, XmlCanvasProcessingException {
		clear();
		
		XppTreeWalker xppTreeWalker = new XppTreeWalker();
		xppTreeWalker.setNodeProcessor(XppNodePath.SEPARATOR + CANVAS, new CanvasNodeProcessor(canvasRef));
		xppTreeWalker.setNodeProcessor(XppNodePath.SEPARATOR + CANVAS + XppNodePath.SEPARATOR + COMMAND, new CommandNodeProcessor(canvasRef));
		xppTreeWalker.processNodes(xpp);
		
		applyConfiguration();
	}

	private void applyConfiguration() {
		if (canvasRef.containsKey(TITLE)) {
			setTitle(canvasRef.getString(TITLE));
		}
		
		setPaintBackgroundColor(canvasRef.containsKey(PAINT_BACKGROUND) ? true : canvasRef.getBoolean(PAINT_BACKGROUND));
		setFullScreenMode(canvasRef.containsKey(FULL_SCREEN) ? true : canvasRef.getBoolean(FULL_SCREEN));
		setTitleEnabled(canvasRef.containsKey(TITLE_ENABLED) ? true : canvasRef.getBoolean(TITLE_ENABLED));
		setMenuEnabled(canvasRef.containsKey(MENU_ENABLED) ? true : canvasRef.getBoolean(MENU_ENABLED));
		
		canvas.repaint();
	}

	private void clear() {
		canvasRef = new RefObject();
	}

	protected void paint(Graphics graphics) {
		Log.debug(CAT, "paint");
	}

}
