package org.helyx.app.j2me.lib.ui.view.painter;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.view.AbstractView;

public interface ViewCanvasZone {

	Rectangle computeArea(AbstractView view, Graphics graphics);

	Rectangle computeArea(AbstractView view, Graphics graphics, Object data);

	void paintArea(AbstractView view, Graphics graphics, Rectangle rect);

	void paintArea(AbstractView view, Graphics graphics, Rectangle rect, Object data);
	
}
