package org.helyx.app.j2me.lib.ui.view.painter;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.graphics.Shade;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;

public class SmallTitleZone extends AbstractViewCanvasZone {

	private static final Log log = LogFactory.getLog("SMALL_TITLE_ZONE");
	
	public SmallTitleZone() {
		super();
	}


	public Rectangle computeArea(AbstractView view, Graphics graphics, Object data) {
		return new Rectangle(0, 0, view.getViewCanvas().getWidth(), view.shouldPaintTitle() ? FontUtil.SMALL_BOLD.getHeight() : 0);
	}


	public void paintArea(AbstractView view, Graphics graphics, Rectangle rect, Object data) {

		Rectangle titleArea = rect;
		Color shadeColor1 = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_LIGHT);
		Color shadeColor2 = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_DARK);
		GraphicsUtil.fillShade(graphics, titleArea, new Shade(shadeColor1.intValue(), shadeColor2.intValue()), false);

		Color titleFontColor = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_FONT);
		graphics.setColor(titleFontColor.intValue());
		graphics.setFont(FontUtil.SMALL_BOLD);
		graphics.drawLine(titleArea.location.x, titleArea.location.y + titleArea.size.height, titleArea.location.x + titleArea.size.width, titleArea.location.y + titleArea.size.height);
		graphics.drawString(view.getTitle(), titleArea.location.x + titleArea.size.width / 2, titleArea.location.y + (titleArea.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2, Graphics.HCENTER | Graphics.TOP);
	}
	
}
