package org.helyx.app.j2me.velocite.view;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import org.helyx.app.j2me.lib.constant.BooleanConstants;
import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.manager.TaskManager;
import org.helyx.app.j2me.lib.pref.Pref;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ITask;
import org.helyx.app.j2me.lib.task.MultiTaskProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.displayable.AbstractCanvas;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.lib.ui.widget.ColorUtil;
import org.helyx.app.j2me.lib.ui.widget.FontUtil;
import org.helyx.app.j2me.lib.ui.widget.IAction;
import org.helyx.app.j2me.lib.ui.widget.action.item.ActionItem;
import org.helyx.app.j2me.lib.util.VectorUtil;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.city.listener.CityLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.data.city.provider.DefaultCityContentProvider;
import org.helyx.app.j2me.velocite.task.factory.ApplicationUpdateTaskFactory;
import org.helyx.app.j2me.velocite.task.factory.DataCleanUpTaskFactory;
import org.helyx.app.j2me.velocite.task.factory.EachRunTaskFactory;
import org.helyx.app.j2me.velocite.task.factory.FirstRunTaskFactory;

public class SplashScreenView extends AbstractCanvas {

	private static final String CAT = "SPLASH_SCREEN_VIEW";
	
	private Image logoImage;
	private String fallbackLogoImageStr;
	

	public SplashScreenView(MIDlet midlet) {
		super(midlet);

		init();
	}

	private void init() {
		setFullScreenMode(true);
		setTitle("VeloCite");
		loadLogoImage();
		
		setPrimaryAction(new ActionItem("Ok", true, new IAction() {
			public void run(Object data) {

				processApplicationStartupTasks();
			}

			private void processApplicationStartupTasks() {
				
				Vector tasksToRun = new Vector();

				Pref oldVersionPref = PrefManager.readPref(PrefConstants.MIDLET_VERSION);
				String oldVersion = oldVersionPref == null ? null : oldVersionPref.value;
				String newVersion = getMidlet().getAppProperty(PrefConstants.MIDLET_VERSION);
				Pref applicationDataCleanUpNeededPref = PrefManager.readPref(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED);

				if (applicationDataCleanUpNeededPref != null && applicationDataCleanUpNeededPref.value.equals(BooleanConstants.TRUE)) {
					Log.info(CAT, "Application data need to be reseted");
					VectorUtil.addElementsToVector(tasksToRun, new DataCleanUpTaskFactory().getTasks());
				}
								
				VectorUtil.addElementsToVector(tasksToRun, new EachRunTaskFactory(getCanvas()).getTasks());

				if (oldVersion == null) {
					Log.info(CAT, "No previous version found.");
					VectorUtil.addElementsToVector(tasksToRun, new FirstRunTaskFactory().getTasks());
				}
				else if (!newVersion.equals(oldVersion)) {
					Log.info(CAT, "Old version is different from new Version");
					VectorUtil.addElementsToVector(tasksToRun, new ApplicationUpdateTaskFactory(getCanvas(), oldVersion, newVersion).getTasks());
				}
				else {
					Log.info(CAT, "Application version is the same one since last run");
				}
				
				int taskToRunCount = tasksToRun.size();
				ITask[] initTasks = new ITask[taskToRunCount];
				tasksToRun.copyInto(initTasks);
				
				MultiTaskProgressTask multiTaskProgressTask = new MultiTaskProgressTask(initTasks);
				
				multiTaskProgressTask.addProgressListener(new ProgressAdapter(CAT + "[" + multiTaskProgressTask.getDescription() + "]") {
					
					public void onSuccess(String eventMessage, Object eventData) {
						String newVersion = getMidlet().getAppProperty(PrefConstants.MIDLET_VERSION);
						Log.info(getCat(), "Writing new version to prefs: '" + newVersion + "'");
						PrefManager.writePref(PrefConstants.MIDLET_VERSION, newVersion);
						if (CityManager.countCities() <= 0) {
							IProgressTask progressTask = CityManager.refreshDataWithDefaults();
							progressTask.addProgressListener(new CityLoaderProgressListener(progressTask.getProgressDispatcher()));
							progressTask.addProgressListener(new ProgressAdapter(SplashScreenView.CAT) {

								public void onSuccess(String eventMessage, Object eventData) {
									showDisplayable(new MenuView(getMidlet()));
								}
								
							});
							TaskManager.runLoadTaskView("Chargement des villes ...", progressTask, getMidlet(), SplashScreenView.this);
						}
						else {
							showDisplayable(new MenuView(getMidlet()));
						}
					}
					
					public void onError(String eventMessage, Object eventData) {
						Log.info(getCat(), "Writing reset demand to prefs");
						PrefManager.writePref(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
					}
					
					public void onCancel(String eventMessage, Object eventData) {
						Log.info(getCat(), "Writing reset demand to prefs");
						PrefManager.writePref(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
					}
					
				});
								
				TaskManager.runLoadTaskView("Chargement de l'application...", multiTaskProgressTask, getMidlet(), SplashScreenView.this);
			}
			
			
		}));
//		
//		secondaryAction = new ActionItem("Quitter", true, new IAction() {
//			public void run(Object data) {
//				getMidlet().notifyDestroyed();
//			}
//		});
	}
	
	private void loadLogoImage() {
		try {
			logoImage = ImageUtil.createImageFromClassPath("/VeloCite.png");
		}
		catch(Throwable t) {
			fallbackLogoImageStr = t.getMessage();
			if (fallbackLogoImageStr == null) {
				fallbackLogoImageStr = t.toString();
			}
			Log.warn(CAT, t);
		}
	}

	protected void paint(Graphics g) {
		Rectangle clientArea = computeClientArea(g);
         
		int x = clientArea.location.x;
		int y = clientArea.location.y;
		int width = clientArea.size.width;
        int height = clientArea.size.height;
               
        g.setColor(ColorUtil.WIDGET_SPLASH_FONT);
        g.setFont(FontUtil.SMALL);
        
        if (logoImage != null) {
        	g.drawImage(logoImage, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.VCENTER);
        }
        else if (fallbackLogoImageStr != null) {
        	Log.info(CAT, fallbackLogoImageStr);
        	g.drawString(fallbackLogoImageStr, x + width / 2, y + height / 2 - FontUtil.SMALL.getHeight(), Graphics.HCENTER | Graphics.BASELINE);        	
        }
        else {
        	Log.info(CAT, "fallbackLogoImageStr error");
        }
        
        g.drawString("Copyright - 2008", width / 2, clientArea.location.y + clientArea.size.height - 2, Graphics.HCENTER | Graphics.BOTTOM);
        g.drawString("http://www.velocite.org", width / 2, clientArea.location.y + clientArea.size.height - FontUtil.SMALL.getHeight() - 2, Graphics.HCENTER | Graphics.BOTTOM);
	}

}