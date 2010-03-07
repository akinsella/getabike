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
package org.helyx.app.j2me.getabike.lib.test.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.logging4me.Logger;


public class AboutView extends AbstractView {

	private static final Logger logger = Logger.getLogger("ABOUT_VIEW");
	
	private Image logoImage;
	private String fallbackLogoImageStr;
	
	public AboutView(AbstractMIDlet midlet) {
		super(midlet);

		init();
	}

	private void init() {
		setFullScreenMode(true);
		setTitle("view.about.title");
		setCommandEnabled(false);
		setPaintScrollBar(true);
		setScreenBackgroundEnabled(true);
		setClientBackgroundEnabled(true);
		setScreenDragging(true);
		loadLogoImage();
		
		initActions();
	}
	
	private void initActions() {
		setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {

			public void run(Object data) {
				fireReturnCallback();
			}
			
		}));
	}
	
	private void loadLogoImage() {
		try {
			String aboutImageStr = getTheme().getString(ThemeConstants.WIDGET_ABOUT_IMAGE);
			logoImage = ImageUtil.createImageFromClassPath(aboutImageStr);
		}
		catch(Throwable t) {
			fallbackLogoImageStr = t.getMessage();
			if (fallbackLogoImageStr == null) {
				fallbackLogoImageStr = t.toString();
			}
			logger.warn(t);
		}
	}

	public void onKeyPressed(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
	    if (gameAction == GameCanvas.LEFT) {
	    	fireReturnCallback();
		}
	}
	
	protected int getViewHeight() {
		return computeClientArea(false).size.height * 2;
	}	

	protected void paint(Graphics g) {
		Rectangle clientArea = computeClientArea();
		
		logger.info("Client area: " + clientArea);

		int x = clientArea.location.x;
		int y = clientArea.location.y;
		int width = clientArea.size.width;
        int height = clientArea.size.height;
        
//        Color backgroundColor = getTheme().getColor(ThemeConstants.WIDGET_BACKGROUND);
//        g.setColor(backgroundColor.intValue());
//        g.fillRect(clientArea.location.x, clientArea.location.y, clientArea.size.width, clientArea.size.height);
        
        Color aboutColor = getTheme().getColor(ThemeConstants.WIDGET_ABOUT_FONT);
        g.setColor(aboutColor.intValue());

        if (logoImage != null) {
        	g.drawImage(logoImage, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.VCENTER);
        }
        else if (fallbackLogoImageStr != null){
        	g.drawString(fallbackLogoImageStr, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BASELINE);        	
        }

        g.setFont(FontUtil.SMALL);

        g.drawString(getMessage("view.about.copyright"), width / 2, y + height - 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString(getMessage("view.about.mail"), width / 2, y + height - 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString(getMessage("view.about.website"), width / 2, y + height - 2 - FontUtil.SMALL.getHeight() * 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString(getMessage("view.about.author"), width / 2, y + height - 2 - FontUtil.SMALL.getHeight() * 3, Graphics.HCENTER | Graphics.BOTTOM);
	}

}
