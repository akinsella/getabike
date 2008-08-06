package org.helyx.app.j2me.lib.ui.view;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.text.StringFormat;
import org.helyx.app.j2me.lib.theme.Theme;
import org.helyx.app.j2me.lib.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.displayable.AbstractCanvas;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.lib.ui.widget.Command;

public class LoadTaskView extends AbstractCanvas {
	
	private static final String CAT = "LOAD_TASK_VIEW";
	
	private IProgressTask progressTask;
	private Timer timer;
	private int counter;
	
	private String catName;
	
	private String label;
	
	private Image[] images;
	
	private String title;
	
	private boolean hasStarted = false;

	public LoadTaskView(AbstractMIDlet midlet, String title, IProgressTask progressTask) {
		super(midlet);
	    this.title = title;
		this.progressTask = progressTask;
		init();
	}

	private void init() {
		setTitle(title);

		timer = new Timer();

		initGraphics();
		
		if (progressTask.isCancellable()) {
			setSecondaryAction(new Command("Retour", true, new IAction() {
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
		Theme theme = getMidlet().getTheme();
		int themeImageCount = theme.getInt(ThemeConstants.LOAD_TASK_VIEW_IMAGE_COUNT);
		String themeImagePath = theme.getString(ThemeConstants.LOAD_TASK_VIEW_IMAGE_PATH);
		StringFormat stringFormat = new StringFormat(themeImagePath);
		
		images = new Image[themeImageCount];
		
		for (int i = 0 ; i < themeImageCount ; i++) {
			try {
				String imagePath = stringFormat.format(i + 1);
				Log.debug("imagePath: " + imagePath);
				Log.info(CAT, "imagePath: '" + imagePath + "'");
				images[i] = ImageUtil.createImageFromClassPath(imagePath);
			}
			catch (IOException e) { 
				Log.warn(getCat(), e);
			}
		}
	}
	
	protected String getCat() {
		if (catName == null) {
			catName = CAT + "[" + progressTask.getDescription() + "]";
		}
		return catName;
	}
	
	private void initListeners() {
		
		progressTask.addProgressListener(new ProgressAdapter(CAT + "[" + progressTask.getDescription() + "]") {

			public void onStart(String eventMessage, Object eventData) {
				hasStarted = true;
				canvas.repaint();
				timer.scheduleAtFixedRate(repaintTimerTask, 0, 100);
			}
			
			public void onProgress(String eventMessage, Object eventData) {
				if (eventData != null) {
					Log.info(this.getCat(), eventData.toString());
				}
				label = eventMessage;
				canvas.repaint();
			}

			public void onAfterCompletion(int eventType, String eventMessage, Object eventData) {
				repaintTimerTask.cancel();
				timer.cancel();
				canvas.repaint();
			}

		});
	}

	public void startTask() {
		progressTask.start();
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
       		Color waitingTitleFontColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_WAITING_TITLE_FONT);
           	g.setColor(waitingTitleFontColor.intValue());
    		g.setFont(FontUtil.SMALL_BOLD);
    		g.drawString(label, x + width / 2, y + textYPos, Graphics.HCENTER | Graphics.BASELINE);
       	}

   		Color waitingMessageFontColor = getMidlet().getTheme().getColor(ThemeConstants.WIDGET_WAITING_MESSAGE_FONT);
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
			canvas.repaint();
		}
		
	};

}
