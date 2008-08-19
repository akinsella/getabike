package org.helyx.app.j2me.lib.ui.view.painter;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.graphics.Shade;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.GraphicsUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;

public class SmallMenuZone extends AbstractViewCanvasZone {

	private static final String CAT = "SMALL_MENU_ZONE";
	
	public SmallMenuZone() {
		super();
	}

	public Rectangle computeArea(AbstractView view, Graphics graphics, Object data) {
		return new Rectangle(0, view.getViewCanvas().getHeight() - (view.shouldPaintMenu() ? FontUtil.SMALL_BOLD.getHeight() : 0) - 1, view.getViewCanvas().getWidth(), (view.shouldPaintMenu() ? FontUtil.SMALL_BOLD.getHeight() : 0) + 1);
	}

	public void paintArea(AbstractView view, Graphics graphics, Rectangle rect, Object data) {
		Color shadeColor1 = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_DARK);
		Color shadeColor2 = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_BG_SHADE_LIGHT);
		GraphicsUtil.fillShade(graphics, rect, new Shade(shadeColor1.intValue(), shadeColor2.intValue()), false);

		Color titleFontColor = view.getTheme().getColor(ThemeConstants.WIDGET_TITLE_FONT);
		graphics.setColor(titleFontColor.intValue());
		graphics.setFont(FontUtil.SMALL_BOLD);
		graphics.drawLine(rect.location.x, rect.location.y, rect.location.x + rect.size.width, rect.location.y);

		if (view.getThirdCommand() != null) {
			graphics.drawString(view.getThirdCommand().getText(), rect.location.x, rect.location.y + (rect.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.LEFT | Graphics.TOP);
		}

		if (view.getPrimaryCommand() != null) {
			graphics.drawString(view.getPrimaryCommand().getText(), rect.location.x + rect.size.width / 2, rect.location.y + (rect.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.HCENTER | Graphics.TOP);
		}

		if (view.getSecondaryCommand() != null) {
			graphics.drawString(view.getSecondaryCommand().getText(), rect.location.x + rect.size.width, rect.location.y + (rect.size.height - FontUtil.SMALL_BOLD.getHeight()) / 2 + 1, Graphics.RIGHT | Graphics.TOP);
		}

	}
	
}
