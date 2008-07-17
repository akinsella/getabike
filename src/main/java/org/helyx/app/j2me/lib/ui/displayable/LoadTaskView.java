package org.helyx.app.j2me.lib.ui.displayable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.lib.ui.widget.ColorUtil;
import org.helyx.app.j2me.lib.ui.widget.FontUtil;
import org.helyx.app.j2me.lib.ui.widget.IAction;
import org.helyx.app.j2me.lib.ui.widget.action.ActionItem;

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

	public LoadTaskView(MIDlet midlet, String title, IProgressTask progressTask) {
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
			secondaryAction = new ActionItem("Retour", true, new IAction() {
				public void run(Object data) {
					if (progressTask.isCancellable()) {
						progressTask.cancel();
					}
				}
			});

		}
		
		initListeners();
	}

	private void initGraphics() {
		int IMAGE_COUNT = 8;
		images = new Image[IMAGE_COUNT];
		
		for (int i = 0 ; i < IMAGE_COUNT ; i++) {
			try {
				images[i] = ImageUtil.createImageFromClassPath("/images/wheel/wheel_" + (i + 1) + ".png");
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

           	g.setColor(ColorUtil.WIDGET_WAITING_TITLE_FONT);
    		g.setFont(FontUtil.SMALL_BOLD);
    		g.drawString(label, x + width / 2, y + textYPos, Graphics.HCENTER | Graphics.BASELINE);
       	}

       	g.setColor(ColorUtil.WIDGET_WAITING_MESSAGE_FONT);
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
