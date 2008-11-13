package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;

public class SplashScreenView extends AbstractView {

	private static final Log log = LogFactory.getLog("SPLASH_SCREEN_VIEW");
	
	private String label;
	private Image logoImage;
	private String fallbackLogoImageStr;
	

	public SplashScreenView(AbstractMIDlet midlet) {
		super(midlet);

		init();
	}

	private void init() {
		setFullScreenMode(true);
		setTitle("VeloCite");
		loadLogoImage();
	}
	
	private void loadLogoImage() {
		try {
			logoImage = ImageUtil.createImageFromClassPath(getTheme().getString(ThemeConstants.WIDGET_SPLASH_IMAGE));
		}
		catch(Throwable t) {
			fallbackLogoImageStr = t.getMessage();
			if (fallbackLogoImageStr == null) {
				fallbackLogoImageStr = t.toString();
			}
			log.warn(t);
		}
	}

	protected void paint(Graphics g) {
		Rectangle clientArea = computeClientArea(g);
         
		int x = clientArea.location.x;
		int y = clientArea.location.y;
		int width = clientArea.size.width;
        int height = clientArea.size.height;
               
        Color splashFontColor = getTheme().getColor(ThemeConstants.WIDGET_SPLASH_FONT);
        g.setColor(splashFontColor.intValue());
        g.setFont(FontUtil.SMALL);
        
        if (logoImage != null) {
        	g.drawImage(logoImage, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.VCENTER);
        }
        else if (fallbackLogoImageStr != null) {
        	log.info(fallbackLogoImageStr);
        	g.drawString(fallbackLogoImageStr, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BASELINE);        	
        }
        else {
        	log.info("fallbackLogoImageStr error");
        }
        
        g.drawString("Copyright - 2008", width / 2, clientArea.location.y + clientArea.size.height - 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString("http://www.velocite.org", width / 2, clientArea.location.y + clientArea.size.height - FontUtil.SMALL.getHeight() - 2, Graphics.HCENTER | Graphics.BOTTOM);

        g.drawString(label != null ? label : "Veuillez patienter ...", width / 2, clientArea.location.y + clientArea.size.height - (FontUtil.SMALL.getHeight() - 2) * 2 - 10, Graphics.HCENTER | Graphics.BOTTOM);
	}

	public void followProgressTask(IProgressTask progressTask) {
		progressTask.addProgressListener(new ProgressAdapter() {

			public void onStart(String eventMessage, Object eventData) {
				viewCanvas.repaint();
			}
			
			public void onProgress(String eventMessage, Object eventData) {
				if (eventData != null) {
					getLog().debug(eventData.toString());
				}
				label = eventMessage;
				repaint();
			}

			public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
				repaint();
			}

		});

	}

}