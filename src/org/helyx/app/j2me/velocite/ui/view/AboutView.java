package org.helyx.app.j2me.velocite.ui.view;

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
import org.helyx.helyx4me.ui.widget.Command;
import org.helyx.logging4me.Logger;


public class AboutView extends AbstractView {

	private static final Logger logger = Logger.getLogger("SPLASH_SCREEN_VIEW");
	
	private Image logoImage;
	private String fallbackLogoImageStr;
	
	public AboutView(AbstractMIDlet midlet) {
		super(midlet);

		init();
	}

	private void init() {
		setFullScreenMode(true);
		setTitle("A propos");
		loadLogoImage();
		
		initActions();
	}
	
	private void initActions() {
		setSecondaryCommand(new Command("Retour", true, new IAction() {

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

	protected void onKeyPressed(int keyCode) {
		int gameAction = viewCanvas.getGameAction(keyCode);
	    if (gameAction == GameCanvas.LEFT) {
	    	fireReturnCallback();
		}
	}
	
	protected void paint(Graphics g) {
		Rectangle clientArea = computeClientArea(g);
               
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
        	g.drawString(fallbackLogoImageStr, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BASELINE);        	
        }

        g.setFont(FontUtil.SMALL);

        g.drawString("Copyright - 2008", width / 2, y + height - 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString("contact@velocite.org", width / 2, y + height - 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString("http://www.velocite.org", width / 2, y + height - 2 - FontUtil.SMALL.getHeight() * 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString("Alexis Kinsella", width / 2, y + height - 2 - FontUtil.SMALL.getHeight() * 3, Graphics.HCENTER | Graphics.BOTTOM);
	}

}