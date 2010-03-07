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
package org.helyx.app.j2me.getabike.lib.ui.view.support.task;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.app.j2me.getabike.lib.task.IProgressTask;
import org.helyx.app.j2me.getabike.lib.task.ProgressAdapter;
import org.helyx.app.j2me.getabike.lib.text.StringFormat;
import org.helyx.app.j2me.getabike.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.app.j2me.getabike.lib.ui.theme.Theme;
import org.helyx.app.j2me.getabike.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.getabike.lib.ui.util.FontUtil;
import org.helyx.app.j2me.getabike.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.task.LoadTaskView;
import org.helyx.app.j2me.getabike.lib.ui.widget.command.Command;
import org.helyx.logging4me.Logger;


public class LoadTaskView extends AbstractView {
	
	private static final Logger logger = Logger.getLogger("LOAD_TASK_VIEW");
	
	private IProgressTask progressTask;
	private Timer timer;
	private int counter;
	
	private String label;
	
	private Image[] images;
	
	private boolean hasStarted = false;

	public LoadTaskView(AbstractMIDlet midlet, String title, IProgressTask progressTask) {
		super(midlet);
		setTitle(title);
//	    setPaintScrollBar(true);
//	    setScreenDragging(true);
	    this.progressTask = progressTask;
		init();
	}

	private void init() {
		setFullScreenMode(true);

		timer = new Timer();

		initGraphics();
		
		if (progressTask.isCancellable()) {
			setSecondaryCommand(new Command("command.back", true, getMidlet().getI18NTextRenderer(), new IAction() {
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
				if (logger.isDebugEnabled()) {
					logger.debug("imagePath: '" + imagePath + "'");
				}
				images[i] = ImageUtil.createImageFromClassPath(imagePath);
			}
			catch (IOException e) { 
				logger.warn(e);
			}
		}
	}
	
	private void initListeners() {
		
		progressTask.addProgressListener(new ProgressAdapter(LoadTaskView.logger.getCategory().getName()) {

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
		Rectangle clientArea = computeClientArea();
        
		int x = clientArea.location.x;
		int y = clientArea.location.y;
		int width = clientArea.size.width;
        int height = clientArea.size.height;

		Image image = images[counter];
		
       	if (label != null) {
       		int  textYPos = height / 2 + FontUtil.MEDIUM_BOLD.getHeight() / 2 + 4;
       		if (image != null) {
       			textYPos = height / 2 + image.getHeight() / 2 + 4;
       		}
       		Color waitingTitleFontColor = getTheme().getColor(ThemeConstants.WIDGET_WAITING_TITLE_FONT);
           	g.setColor(waitingTitleFontColor.intValue());
    		g.setFont(FontUtil.SMALL_BOLD);
    		g.drawString(label, x + width / 2, y + textYPos, Graphics.HCENTER | Graphics.TOP);
       	}

   		Color waitingMessageFontColor = getTheme().getColor(ThemeConstants.WIDGET_WAITING_MESSAGE_FONT);
       	g.setColor(waitingMessageFontColor.intValue());
        g.setFont(FontUtil.MEDIUM_BOLD);
   		int  textYPos = height / 2 - FontUtil.MEDIUM_BOLD.getHeight() / 2;
   		if (image != null) {
   			textYPos = height / 2 - image.getHeight() / 2 - FontUtil.MEDIUM_BOLD.getHeight() - 4;
   		}
   		
   		g.drawString(getMessage("view.task.load.wait"), x + width / 2, y + textYPos, Graphics.HCENTER | Graphics.TOP);

		if (hasStarted && image != null) {
	       	g.drawImage(image, x + width / 2, y + height / 2, Graphics.HCENTER | Graphics.VCENTER);
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
