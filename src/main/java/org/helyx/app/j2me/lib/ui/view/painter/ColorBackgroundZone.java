package org.helyx.app.j2me.lib.ui.view.painter;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.view.AbstractView;

public class ColorBackgroundZone extends AbstractViewCanvasZone {

	private static final Log log = LogFactory.getLog("COLOR_BACKGROUND_ZONE");
	
	private int backgroundColor;

	public ColorBackgroundZone(int backgroundColor) {
		super();
		this.backgroundColor = backgroundColor;
	}

	public Rectangle computeArea(AbstractView view, Graphics graphics, Object data) {
		return new Rectangle(0, 0, view.getViewCanvas().getWidth(), view.getViewCanvas().getHeight());
	}

	public void paintArea(AbstractView view, Graphics graphics, Rectangle rect, Object data) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, view.getViewCanvas().getWidth(), view.getViewCanvas().getHeight());
	}
	
}
