package org.helyx.app.j2me.lib.ui.view.support;

import javax.microedition.lcdui.Graphics;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.ui.displayable.callback.IReturnCallback;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.widget.menu.MenuItem;


public abstract class PrefBaseListView extends MenuListView {

	private static final Log log = LogFactory.getLog("PREF_LIST_VIEW");
	
	protected static final String PREF_VALUE = "PREF_VALUE";

	public PrefBaseListView(AbstractMIDlet midlet, String title) {
		super(midlet, title, false);
	}
	
	public PrefBaseListView(AbstractMIDlet midlet, String title, IReturnCallback returnCallback) {
		super(midlet, title, false, returnCallback);
	}

	protected void paintItem(Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
		super.paintItem(g, offset, itemClientArea, itemObject);
		
		MenuItem menuItem = getMenu().getMenuItem(offset);
		String prefValue = (String)menuItem.getData(PREF_VALUE);
		if (prefValue == null) {
			return;
		}
		
		if (isItemSelected(offset)) {
    		Color menuListFontSelectedColor = getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT_SECOND_SELECTED);
    		g.setColor(menuListFontSelectedColor.intValue());
    	}
    	else {
    		Color menuListFontColor = getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT_SECOND);
    		g.setColor(menuListFontColor.intValue());
    	}

        g.setFont(FontUtil.MEDIUM_BOLD);
        int prefValueWidth = g.getFont().stringWidth(prefValue);
        g.drawString(prefValue, itemClientArea.location.x + itemClientArea.size.width - 5 - prefValueWidth, itemClientArea.location.y + (itemClientArea.size.height - FontUtil.MEDIUM_BOLD.getHeight()) / 2, Graphics.LEFT | Graphics.TOP);
	}

}
