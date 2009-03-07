package org.helyx.app.j2me.lib.ui.view.support;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.text.StringFormat;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.Theme;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;
import org.helyx.app.j2me.lib.ui.widget.Command;

public class LoadTaskView extends AbstractView {
	
	private static final Logger logger = LoggerFactory.getLogger("LOAD_TASK_VIEW");
	
	private IProgressTask progressTask;
	private Timer timer;
	private int counter;
	
	private String label;
	
	private Image[] images;
	
	private boolean hasStarted = false;

	public LoadTaskView(AbstractMIDlet midlet, String title, IProgressTask progressTask) {
		super(midlet);
		setTitle(title);
		this.progressTask = progressTask;
		init();
	}

	private void init() {
		setFullScreenMode(true);

		timer = new Timer();

		initGraphics();
		
		if (progressTask.isCancellable()) {
			setSecondaryCommand(new Command("Retour", true, new IAction() {
				public void run(Object data) {
					if (progressTask.isCancellable()) {
						progressTask.cancel();
					}
				}
			}));

		}
		
		initListeners();
	}

	private void initGraphics() {
		Theme theme = getTheme();
		int themeImageCount = theme.getInt(ThemeConstants.LOAD_TASK_VIEW_IMAGE_COUNT);
		String themeImagePath = theme.getString(ThemeConstants.LOAD_TASK_VIEW_IMAGE_PATH);
		StringFormat stringFormat = new StringFormat(themeImagePath);
		
		images = new Image[themeImageCount];
		
		for (int i = 0 ; i < themeImageCount ; i++) {
			try {
				String imagePath = stringFormat.format(i + 1);
				logger.debug("imagePath: '" + imagePath + "'");
				images[i] = ImageUtil.createImageFromClassPath(imagePath);
			}
			catch (IOException e) { 
				logger.warn(e);
			}
		}
	}
	
	private void initListeners() {
		
		progressTask.addProgressListener(new ProgressAdapter(LoadTaskView.logger.getCategory()) {

			public void onStart(String eventMessage, Object eventData) {
				hasStarted = true;
				viewCanvas.repaint();
				timer.scheduleAtFixedRate(repaintTimerTask, 0, 100);
			}
			
			public void onProgress(String eventMessage, Object eventData) {
				if (eventData != null) {
					getLogger().debug(eventData.toString());
				}
				label = eventMessage;
				viewCanvas.repaint();
			}

			public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
				repaintTimerTask.cancel();
				timer.cancel();
				viewCanvas.repaint();
			}

		});
	}

	public void startTask() {
		progressTask.execute();
	}

	protected void paint(Graphics g) {
		Rectangle clientArea = computeClientArea(g);
        
		int x = clientArea.location.x;
		int y = clientArea.location.y;
		int width = clientArea.size.width;
        int height = clientArea.size.height;

		Image image = images[counter];
		
       	if (label != null) {
       		int  textYPos = height / 2;
       		if (image != null) {
       			textYPos += image.getHeight() / 2 + FontUtil.SMALL_BOLD.getBaselinePosition();
       		}
       		Color waitingTitleFontColor = getTheme().getColor(ThemeConstants.WIDGET_WAITING_TITLE_FONT);
           	g.setColor(waitingTitleFontColor.intValue());
    		g.setFont(FontUtil.SMALL_BOLD);
    		g.drawString(label, x + width / 2, y + textYPos, Graphics.HCENTER | Graphics.BASELINE);
       	}

   		Color waitingMessageFontColor = getTheme().getColor(ThemeConstants.WIDGET_WAITING_MESSAGE_FONT);
       	g.setColor(waitingMessageFontColor.intValue());
        g.setFont(FontUtil.MEDIUM_BOLD);
   		int  textYPos = height / 2;
   		if (image != null) {
   			textYPos -= image.getHeight() / 2 + FontUtil.MEDIUM_BOLD.getHeight();
   		}
   		g.drawString("Veuillez patienter ...", x + width / 2, y + textYPos, Graphics.HCENTER | Graphics.BASELINE);

		if (hasStarted && image != null) {
	       	g.drawImage(image, x + width / 2, y + height / 2 - 5, Graphics.HCENTER | Graphics.VCENTER);
		}
	}

	private TimerTask repaintTimerTask = new TimerTask() {
		
		public void run() {
			counter++;
			if (counter + 1 >= 8) {
				counter = 0;
			}
			viewCanvas.repaint();
		}
		
	};

}
