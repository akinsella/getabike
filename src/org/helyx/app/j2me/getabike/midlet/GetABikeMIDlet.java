package org.helyx.app.j2me.getabike.midlet;

import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.task.AppStartProgressTask;
import org.helyx.app.j2me.getabike.ui.view.MenuView;
import org.helyx.app.j2me.getabike.ui.view.SplashScreenView;
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
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.config.LoggerConfigurer;
import org.helyx.logging4me.config.XmlConfigurer;


public class GetABikeMIDlet extends AbstractMIDlet {

	private static final Logger logger = Logger.getLogger("GETABIKE_MIDLET");

	public GetABikeMIDlet() {
		super();
	}

	protected void onStart() {
		LoggerConfigurer loggerConfigurer = new XmlConfigurer("/org/helyx/app/j2me/getabike/logging4me.xml", true);
		loggerConfigurer.configure();
		
		setThemeConfiguration("default", "org.helyx.app.j2me.getabike.theme");
		setI18nConfiguration("messages", "org.helyx.app.j2me.getabike.i18n");
		setLocale(Locale.FRANCE);
		
		final SplashScreenView splashScreenView = new SplashScreenView(this);
		splashScreenView.show();
		
		IProgressTask appStartProgressTask = new AppStartProgressTask(splashScreenView);
		
		appStartProgressTask.addProgressListener(new ProgressAdapter(GetABikeMIDlet.logger.getCategory().getName()) {

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
		MenuView menuView = new MenuView(GetABikeMIDlet.this);
		view.showDisplayable(menuView);
	}

	private void onStartError(AbstractView view, String message, Throwable t) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
		logger.warn(t);
		String errorMessage = t.getMessage() == null ? getMessage("dialog.error.unexpected") : t.getMessage();
		DialogUtil.showMessageDialog(
				view, 
				"dialog.title.error", 
				getMessage("midlet.start.error.message.1") + ": " + errorMessage, 
				new OkResultCallback() {
					public void onOk(DialogView dialogView, Object data) {
						if (GetABikeMIDlet.this.logger.isInfoEnabled()) {
							GetABikeMIDlet.this.logger.info("Writing reset demand to prefs");
						}
						PrefManager.writePref(PrefConstants.CITY_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
						GetABikeMIDlet.this.exit();								
					}
				});
	}

	protected void onDestroy(boolean unconditional) throws MIDletStateChangeException {
		super.onDestroy(unconditional);
	}

	protected void onPause() {
		super.onPause();
	}

}
