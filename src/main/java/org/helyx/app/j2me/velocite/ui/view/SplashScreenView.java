package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger("SPLASH_SCREEN_VIEW");
	
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
			String widgetSplashImage = getTheme().getString(ThemeConstants.WIDGET_SPLASH_IMAGE);
			if (logger.isDebugEnabled()) {
				logger.debug("widgetSplashImage: " + widgetSplashImage);
			}
			logoImage = ImageUtil.createImageFromClassPath(getTheme().getString(ThemeConstants.WIDGET_SPLASH_IMAGE));
		}
		catch(Throwable t) {
			fallbackLogoImageStr = t.getMessage();
			if (fallbackLogoImageStr == null) {
				fallbackLogoImageStr = t.toString();
			}
			logger.warn(t);
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
        	logger.info(fallbackLogoImageStr);
        	g.drawString(fallbackLogoImageStr, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BASELINE);        	
        }
        else {
        	logger.info("fallbackLogoImageStr error");
        }
             
        g.drawString("Copyright - 2008", width / 2, y + height - 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString("http://www.velocite.org", width / 2, y + height - 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BOTTOM);

        int center = y + height / 2 + (logoImage != null ? logoImage.getHeight() : FontUtil.SMALL.getHeight()) / 2;
        int bottom = y + height - 2 - FontUtil.SMALL.getHeight() * 2;
       
        int center2 = center + (bottom - center) / 2;
        
        g.setColor(getTheme().getColor(ThemeConstants.WIDGET_SPLASH_FONT_LOAD).intValue());
        g.setFont(FontUtil.SMALL_BOLD);
        g.drawString(label != null ? label : "Veuillez patienter ...", width / 2, center2, Graphics.HCENTER | Graphics.BASELINE);
	}

	public void followProgressTask(IProgressTask progressTask) {
		progressTask.addProgressListener(new ProgressAdapter(logger.getCategory()) {

			public void onStart(String eventMessage, Object eventData) {
				viewCanvas.repaint();
			}
			
			public void onProgress(String eventMessage, Object eventData) {
				if (eventData != null) {
					getLogger().debug(eventData.toString());
				}
				label = eventMessage;
				repaint();
			}

			public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
				repaint();
			}

		});

		progressTask.start();
	}

}