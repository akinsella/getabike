package org.helyx.app.j2me.velocite.midlet;

import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.task.AppStartProgressTask;
import org.helyx.app.j2me.velocite.ui.view.MenuView;
import org.helyx.app.j2me.velocite.ui.view.SplashScreenView;
import org.helyx.app.j2me.velocite.util.UtilManager;
import org.helyx.helyx4me.constant.BooleanConstants;
import org.helyx.helyx4me.i18n.Locale;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.IProgressTask;
import org.helyx.helyx4me.task.ProgressAdapter;
import org.helyx.helyx4me.ui.view.AbstractView;
import org.helyx.helyx4me.ui.view.support.dialog.DialogUtil;
import org.helyx.helyx4me.ui.view.support.dialog.DialogView;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.helyx4me.ui.view.support.dialog.result.callback.YesNoResultCallback;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.LoggerManager;
import org.helyx.logging4me.appender.FileAppender;
import org.helyx.logging4me.config.LoggerConfigurer;
import org.helyx.logging4me.config.XmlConfigurer;
import org.helyx.logging4me.layout.pattern.PatternLayout;


public class VelociteMIDlet extends AbstractMIDlet {

	private static final Logger logger = Logger.getLogger("VELOCITE_MIDLET");
	
	private FileAppender fileAppender = null;

	public VelociteMIDlet() {
		super();
	}

	protected void onStart() {
//		LoggerManager.setThresholdLevel(Logger.DEBUG);
//		
//		ConsoleAppender consoleAppender = new ConsoleAppender();
//		consoleAppender.setLayout(new PatternLayout("| %T | %L | %C | %D{yyyy/MM/dd, HH:mm:ss.ZZ} | "));
//		consoleAppender.setThresholdLevel(Logger.DEBUG);
//		
//		LoggerManager.registerAppender(consoleAppender);
//		
//		LoggerManager.getRootCategory().addAppender(consoleAppender);
//
//		LoggerManager.addCategory("org.helyx.app.j2me.lib", Logger.INFO);
//		LoggerManager.addCategory("org.helyx.app.j2me.velocite", Logger.DEBUG);

		LoggerConfigurer loggerConfigurer = new XmlConfigurer("/org/helyx/app/j2me/velocite/logging4me.xml", true);
		loggerConfigurer.configure();
		
		setThemeConfiguration("default", "org.helyx.app.j2me.velocite.theme");
		setI18nConfiguration("messages", "org.helyx.app.j2me.velocite.i18n");
		setLocale(Locale.FRANCE);
		
		final SplashScreenView splashScreenView = new SplashScreenView(this);
		splashScreenView.show();
		
		IProgressTask appStartProgressTask = new AppStartProgressTask(splashScreenView);
		
		appStartProgressTask.addProgressListener(new ProgressAdapter(VelociteMIDlet.logger.getCategory().getName()) {

			public void onSuccess(String eventMessage, Object eventData) {
				onStartSuccess(splashScreenView);
			}
			
			public void onError(String eventMessage, Object eventData) {
				Throwable t = (Throwable)eventData;
				onStartError(splashScreenView, eventMessage, t);
			}
			
		});
		
		splashScreenView.followProgressTask(appStartProgressTask);
	}

	private void onStartSuccess(AbstractView view) {
		MenuView menuView = new MenuView(VelociteMIDlet.this);
		view.showDisplayable(menuView);
		checkMapModeEnabled(menuView);
	}
	
	private void checkMapModeEnabled(final AbstractView view) {
		if (PrefManager.readPref(UtilManager.MAP_MODE_ENABLED) == null) {
			DialogUtil.showYesNoDialog(
				view, 
				"Google Maps", 
				"Activer la visualisation des stations avec Google Maps ?\n\nAttention: Un forfait data illimité est  fortement recommandé.",
				new YesNoResultCallback() {
					public void onYes(DialogView dialogView, Object data) {
						PrefManager.writePrefBoolean(UtilManager.MAP_MODE_ENABLED, true);
						dialogView.showDisplayable(view);
					}
	
					public void onNo(DialogView dialogView, Object data) {
						PrefManager.writePrefBoolean(UtilManager.MAP_MODE_ENABLED, false);
						dialogView.showDisplayable(view);
					}
				});
		}

	}

	private void onStartError(AbstractView view, String message, Throwable t) {
		logger.info(message);
		logger.warn(t);
		String errorMessage = t.getMessage() == null ? "Erreur d'initialisation de l'application" : t.getMessage();
		DialogUtil.showMessageDialog(
				view, 
				"Erreur", 
				"L'application doit être redémarée: " + errorMessage, 
				new OkResultCallback() {
					public void onOk(DialogView dialogView, Object data) {
						VelociteMIDlet.this.logger.info("Writing reset demand to prefs");
						PrefManager.writePref(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
						VelociteMIDlet.this.exit();								
					}
				});
	}

	protected void onDestroy(boolean unconditional) throws MIDletStateChangeException {
		super.onDestroy(unconditional);
	}

	protected void onPause() {
		super.onPause();
	}

	private void openFileAppender() {
		if (fileAppender == null) {
			try {
				fileAppender = new FileAppender("VeloCite.log");
				fileAppender.setLayout(new PatternLayout("| %T | %L | %C | %D{yyyy/MM/dd, HH:mm:ss.ZZ} | "));
				fileAppender.open();
				LoggerManager.registerAppender(fileAppender);
				LoggerManager.getRootCategory().addAppender(fileAppender);
			}
			catch(Throwable t) {
				logger.warn(t);
				fileAppender = null;
			}
		}

	}
	
	private void closeFileAppender() {
		if (fileAppender != null) {
			try {
				LoggerManager.unregisterAppender(fileAppender);
			}
			finally {
				try {
					fileAppender.close();
				}
				catch (Exception e) {
					logger.warn(e);
				}
				finally {
					fileAppender = null;
				}
			}
		}
	}


}
