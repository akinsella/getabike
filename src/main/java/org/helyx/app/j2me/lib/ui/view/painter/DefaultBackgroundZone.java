package org.helyx.app.j2me.lib.ui.view.painter;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.view.AbstractView;

public class DefaultBackgroundZone extends AbstractViewCanvasZone {

	private static final Log log = LogFactory.getLog("BACKGROUND_ZONE");
	
	public DefaultBackgroundZone() {
		super();
	}

	public Rectangle computeArea(AbstractView view, Graphics graphics, Object data) {
		return new Rectangle(0, 0, view.getViewCanvas().getWidth(), view.getViewCanvas().getHeight());
	}

	public void paintArea(AbstractView view, Graphics graphics, Rectangle rect, Object data) {
		Color bgColor = view.getTheme().getColor(ThemeConstants.WIDGET_BACKGROUND);

        graphics.setColor(bgColor.intValue());
        graphics.fillRect(0, 0, view.getViewCanvas().getWidth(), view.getViewCanvas().getHeight());
	}
	
}
