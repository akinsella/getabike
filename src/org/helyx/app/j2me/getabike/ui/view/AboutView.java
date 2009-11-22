package org.helyx.app.j2me.getabike.ui.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;

import org.helyx.helyx4me.action.IAction;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.graphics.Color;
import org.helyx.helyx4me.ui.theme.ThemeConstants;
import org.helyx.helyx4me.ui.util.FontUtil;
import org.helyx.helyx4me.ui.util.ImageUtil;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.helyx4me.ui.widget.command.Command;
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
//		setPaintScrollBar(true);
//		setScreenDragging(true);
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
	
	protected void paint(Graphics g) {
		Rectangle clientArea = computeClientArea();
               
		int x = clientArea.location.x;
		int y = clientArea.location.y;
		int width = clientArea.size.width;
        int height = clientArea.size.height;
        
        Color aboutColor = getTheme().getColor(ThemeConstants.WIDGET_ABOUT_FONT);
        g.setColor(aboutColor.intValue());

        if (logoImage != null) {
        	g.drawImage(logoImage, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.VCENTER);
        }
        else if (fallbackLogoImageStr != null){
        	g.drawString(fallbackLogoImageStr, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.TOP);        	
        }

        g.setFont(FontUtil.SMALL);

        g.drawString(getMessage("view.about.copyright"), width / 2, y + height - 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString(getMessage("view.about.mail"), width / 2, y + height - 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString(getMessage("view.about.website"), width / 2, y + height - 2 - FontUtil.SMALL.getHeight() * 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString(getMessage("view.about.author"), width / 2, y + height - 2 - FontUtil.SMALL.getHeight() * 3, Graphics.HCENTER | Graphics.BOTTOM);
	}

}
