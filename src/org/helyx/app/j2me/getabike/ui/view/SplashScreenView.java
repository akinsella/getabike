package org.helyx.app.j2me.velocite.ui.view;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.geometry.Rectangle;
import org.helyx.helyx4me.ui.graphics.Color;
import org.helyx.helyx4me.ui.theme.ThemeConstants;
import org.helyx.helyx4me.ui.util.FontUtil;
import org.helyx.helyx4me.ui.util.ImageUtil;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.logging4me.Logger;


public class SplashScreenView extends AbstractView {

	private static final Logger logger = Logger.getLogger("SPLASH_SCREEN_VIEW");
	
	private String label;
	private Image logoImage;
	private String fallbackLogoImageStr;
	

	public SplashScreenView(AbstractMIDlet midlet) {
		super(midlet);
		init();
	}

	private void init() {
		setFullScreenMode(true);
		setTitle("view.splash.title");
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
             
        g.drawString(getMessage("view.splash.copyright"), width / 2, y + height - 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString(getMessage("view.splash.website"), width / 2, y + height - 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BOTTOM);

        int center = y + height / 2 + (logoImage != null ? logoImage.getHeight() : FontUtil.SMALL.getHeight()) / 2;
        int bottom = y + height - 2 - FontUtil.SMALL.getHeight() * 2;
       
        int center2 = center + (bottom - center) / 2;
        
        g.setColor(getTheme().getColor(ThemeConstants.WIDGET_SPLASH_FONT_LOAD).intValue());
        g.setFont(FontUtil.SMALL_BOLD);
        g.drawString(label != null ? label : getMessage("view.splash.wait"), width / 2, center2, Graphics.HCENTER | Graphics.BASELINE);
	}

	public void followProgressTask(IProgressTask progressTask) {
		progressTask.addProgressListener(new ProgressAdapter(logger.getCategory().getName()) {

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

		progressTask.execute();
	}

}