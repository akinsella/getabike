package org.helyx.app.j2me.lib.ui.view.painter;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.view.AbstractView;

public abstract class AbstractViewCanvasZone implements ViewCanvasZone {

	private static final String CAT = "ABSTRACT_VIEW_CANVAS_ZONE";
	
	public AbstractViewCanvasZone() {
		super();
	}

	public Rectangle computeArea(AbstractView view, Graphics graphics) {
		return computeArea(view, graphics, null);
	}

	public abstract Rectangle computeArea(AbstractView view, Graphics graphics, Object data);

	public void paintArea(AbstractView view, Graphics graphics,  Rectangle rect) {
		paintArea(view, graphics, rect, null);
	}
	
	public abstract void paintArea(AbstractView view, Graphics graphics, Rectangle rect, Object data);
	
}
