package org.helyx.app.j2me.velocite.midlet;

import org.helyx.app.j2me.lib.constant.BooleanConstants;
import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.IProgressTask;
import org.helyx.app.j2me.lib.task.ProgressAdapter;
import org.helyx.app.j2me.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.lib.ui.view.AbstractView;
import org.helyx.app.j2me.lib.ui.view.support.dialog.AbstractDialogResultCallback;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.task.AppStartProgressTask;
import org.helyx.app.j2me.velocite.ui.view.MenuView;
import org.helyx.app.j2me.velocite.ui.view.SplashScreenView;


public class VelociteMIDlet extends AbstractMIDlet {

	private static final Log log = LogFactory.getLog("VELOCITE_MIDLET");

	private static final String V_1_0_83 = "1.0.83";
	private static final String V_1_0_82 = "1.0.82";

	public VelociteMIDlet() {
		super();
	}

	protected void onStart() {
		Log.setThresholdLevel(Log.INFO);
		setThemeConfiguration("default", "org.helyx.app.j2me.velocite.theme");
		setI18nConfiguration("messages", "org.helyx.app.j2me.velocite.i18n");
		setLocale(Locale.FRANCE);
		
		final SplashScreenView splashScreenView = new SplashScreenView(this);
		splashScreenView.show();
		
		IProgressTask appStartProgressTask = new AppStartProgressTask(splashScreenView);
		
		appStartProgressTask.addProgressListener(new ProgressAdapter() {

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
		view.showDisplayable(new MenuView(VelociteMIDlet.this));
	}

	private void onStartError(AbstractView view, String message, Throwable t) {
		log.info(message);
		String errorMessage = t.getMessage() == null ? "Erreur de chargement des villes" : t.getMessage();
		DialogUtil.showMessageDialog(
				view, 
				"Erreur", 
				"L'application doit être redémarée: " + errorMessage, 
				new AbstractDialogResultCallback() {
					public void onResult(DialogView dialogView, Object data) {
						VelociteMIDlet.this.log.info("Writing reset demand to prefs");
						PrefManager.writePref(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
						VelociteMIDlet.this.exit();								
					}
		});
	}

}
