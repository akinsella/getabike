package org.helyx.app.j2me.velocite.ui.view;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.lib.action.IAction;
import org.helyx.app.j2me.lib.constant.BooleanConstants;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.manager.TaskManager;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.Pref;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.BasicTask;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ITask;
import org.helyx.app.j2me.lib.task.MultiTaskProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.geometry.Rectangle;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.app.j2me.lib.ui.theme.ThemeConstants;
import org.helyx.app.j2me.lib.ui.util.FontUtil;
import org.helyx.app.j2me.lib.ui.util.ImageUtil;
import org.helyx.app.j2me.lib.ui.view.AbstractView;
import org.helyx.app.j2me.lib.ui.view.support.dialog.AbstractDialogResultCallback;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.lib.ui.widget.Command;
import org.helyx.app.j2me.lib.util.VectorUtil;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.city.listener.CityLoaderProgressListener;
import org.helyx.app.j2me.velocite.data.city.manager.CityManager;
import org.helyx.app.j2me.velocite.task.factory.ApplicationUpdateTaskFactory;
import org.helyx.app.j2me.velocite.task.factory.DataCleanUpTaskFactory;
import org.helyx.app.j2me.velocite.task.factory.EachRunTaskFactory;
import org.helyx.app.j2me.velocite.task.factory.FirstRunTaskFactory;

public class SplashScreenView extends AbstractView {

	private static final Log log = LogFactory.getLog("SPLASH_SCREEN_VIEW");
	
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
		
		setPrimaryCommand(new Command("Ok", true, new IAction() {
			public void run(Object data) {

				processApplicationStartupTasks();
			}

			private void processApplicationStartupTasks() {
				
				Vector tasksToRun = new Vector();

				Pref oldVersionPref = PrefManager.readPref(PrefConstants.MIDLET_VERSION);
				String oldVersion = oldVersionPref == null ? null : oldVersionPref.value;
				String newVersion = getMidlet().getAppProperty(PrefConstants.MIDLET_VERSION);
				boolean applicationDataCleanUpNeeded = PrefManager.readPrefBoolean(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED);
				boolean cityDataCleanUpNeeded = PrefManager.readPrefBoolean(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED);

				if (applicationDataCleanUpNeeded) {
					log.info("Application data need to be reseted");
					VectorUtil.addElementsToVector(tasksToRun, new DataCleanUpTaskFactory().getTasks());
				}
				
				if (cityDataCleanUpNeeded) {
					log.info("City data need to be reseted");
					tasksToRun.addElement(new BasicTask("Cleaning up cities related data") {
						public void execute() {
							CityManager.cleanUpData();
							PrefManager.removePref(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED);
						}
					});
				}
								
				VectorUtil.addElementsToVector(tasksToRun, new EachRunTaskFactory(getMidlet(), getViewCanvas()).getTasks());

				if (oldVersion == null) {
					log.info("No previous version found.");
					VectorUtil.addElementsToVector(tasksToRun, new FirstRunTaskFactory().getTasks());
				}
				else if (!newVersion.equals(oldVersion)) {
					log.info("Old version is different from new Version");
					VectorUtil.addElementsToVector(tasksToRun, new ApplicationUpdateTaskFactory(getViewCanvas(), oldVersion, newVersion).getTasks());
				}
				else {
					log.info("Application version is the same one since last run");
				}
				
				int taskToRunCount = tasksToRun.size();
				ITask[] initTasks = new ITask[taskToRunCount];
				tasksToRun.copyInto(initTasks);
				
				MultiTaskProgressTask multiTaskProgressTask = new MultiTaskProgressTask(initTasks);
				
				multiTaskProgressTask.addProgressListener(new ProgressAdapter() {
					
					public void onSuccess(String eventMessage, Object eventData) {
						String newVersion = getMidlet().getAppProperty(PrefConstants.MIDLET_VERSION);
						getLog().info("Writing new version to prefs: '" + newVersion + "'");
						PrefManager.writePref(PrefConstants.MIDLET_VERSION, newVersion);
						if (CityManager.countCities() <= 0) {
							IProgressTask progressTask = CityManager.refreshDataWithDefaults();
							progressTask.addProgressListener(new CityLoaderProgressListener(progressTask.getProgressDispatcher()));
							progressTask.addProgressListener(new ProgressAdapter() {

								public void onSuccess(String eventMessage, Object eventData) {
									showDisplayable(new MenuView(getMidlet()));
								}
								
								public void onError(String eventMessage, Object eventData) {
									Throwable t = (Throwable)eventData;
									SplashScreenView.this.log.info(eventMessage);
									String errorMessage = t.getMessage() == null ? "Erreur de chargement des villes" : t.getMessage();
									DialogUtil.showMessageDialog(
											SplashScreenView.this, 
											"Erreur", 
											"L'application doit être redémarée: " + errorMessage, 
											new AbstractDialogResultCallback() {
												public void onResult(DialogView dialogView, Object data) {
													getLog().info(SplashScreenView.log.getCategory(), "Writing reset demand to prefs");
													PrefManager.writePref(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
													getMidlet().exit();								
												}
									});
								}
								
							});
							TaskManager.runLoadTaskView("Chargement des villes ...", progressTask, getMidlet(), SplashScreenView.this);
						}
						else {
							showDisplayable(new MenuView(getMidlet()));
						}
					}
					
					public void onError(String eventMessage, Object eventData) {
						getLog().info(SplashScreenView.log.getCategory(), "Writing reset demand to prefs");
						PrefManager.writePref(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
					}
					
					public void onCancel(String eventMessage, Object eventData) {
						getLog().info(SplashScreenView.log.getCategory(), "Writing reset demand to prefs");
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
	}

}