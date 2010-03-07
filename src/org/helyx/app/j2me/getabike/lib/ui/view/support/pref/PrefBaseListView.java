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
package org.helyx.app.j2me.getabike.lib.ui.view.support.pref;

import java.io.IOException;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.view.support.menu.MenuListView;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.getabike.lib.ui.widget.menu.MenuItem;
import org.helyx.logging4me.Logger;



public abstract class PrefBaseListView extends MenuListView {

	private static final Logger logger = Logger.getLogger("PREF_LIST_VIEW");
	
	protected static final String CHEVRON = ">>";
	protected static final String PREF_VALUE = "PREF_VALUE";
	protected static final String PREF_VALUE_DETAILS = "PREF_VALUE_DETAILS";
	
	private Image chevronImage;

	public PrefBaseListView(AbstractMIDlet midlet, String title) {
		super(midlet, title, false);
		init();
	}
	
	private void init() {
		initImages();
		onInit();
	}
	
	private void initImages() {
        try {
			chevronImage = ImageUtil.createImageFromClassPath(getTheme().getString(ThemeConstants.WIDGET_MENU_IMAGE_CHEVRON));
		}
        catch (IOException ioe) {
			logger.warn(ioe);
		}
	}

	protected void onInit() {
		
	}

	protected void paintItem(Graphics g, int offset, Rectangle itemClientArea, Object itemObject) {
		super.paintItem(g, offset, itemClientArea, itemObject);
		
		MenuItem menuItem = getMenu().getMenuItem(offset);

		Image targetImage = null;
		String targetValue = null;
		Color menuListFontColor = null;
		if (menuItem.isParentMenu()) {
			targetImage = chevronImage;
			targetValue = CHEVRON;
			if (isItemSelected(offset)) {
	    		menuListFontColor = getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT_SELECTED);
	    	}
	    	else {
	    		menuListFontColor = getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT);
	    	}
		}
		else {
			String prefValue = (String)menuItem.getData(PREF_VALUE);
			if (prefValue == null) {
				return;
			}
			targetValue = prefValue;
			if (isItemSelected(offset)) {
	    		menuListFontColor = getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT_SECOND_SELECTED);
	    	}
	    	else {
	    		menuListFontColor = getTheme().getColor(ThemeConstants.WIDGET_MENU_LIST_FONT_SECOND);
	    	}
  		}
  		g.setColor(menuListFontColor.intValue());
	
        g.setFont(FontUtil.MEDIUM_BOLD);
        int targetValueWidth = g.getFont().stringWidth(targetValue);
        
        if (targetImage != null) {
        	int targetImageWidth = targetImage.getWidth();
        	int targetImageHeight = targetImage.getHeight();
            g.drawImage(targetImage, itemClientArea.location.x + itemClientArea.size.width - 5 - targetImageWidth, itemClientArea.location.y + (itemClientArea.size.height - targetImageHeight) / 2, Graphics.LEFT | Graphics.TOP);     	
        }
        else {
            g.drawString(targetValue, itemClientArea.location.x + itemClientArea.size.width - 5 - targetValueWidth, itemClientArea.location.y + (itemClientArea.size.height - FontUtil.MEDIUM_BOLD.getHeight()) / 2, Graphics.LEFT | Graphics.TOP);       	
        }
 	}

}
