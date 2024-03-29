package org.helyx.app.j2me.getabike.midlet;

import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.getabike.data.app.manager.AppManager;
import org.helyx.app.j2me.getabike.data.language.manager.LanguageManager;
import org.helyx.app.j2me.getabike.task.AppStartProgressTask;
import org.helyx.app.j2me.getabike.ui.view.MenuView;
import org.helyx.app.j2me.getabike.ui.view.SplashScreenView;
import org.helyx.app.j2me.getabike.util.ErrorManager;
import org.helyx.app.j2me.getabike.lib.i18n.Locale;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.task.IProgressTask;
import org.helyx.app.j2me.getabike.lib.task.ProgressAdapter;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.DialogView;
import org.helyx.app.j2me.getabike.lib.ui.view.support.dialog.result.callback.OkResultCallback;
import org.helyx.app.j2me.getabike.lib.util.ErrorUtil;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.config.LoggerConfigurer;
import org.helyx.logging4me.config.XmlConfigurer;


public class GetABikeMIDlet extends AbstractMIDlet {

	private static final Logger logger = Logger.getLogger("GETABIKE_MIDLET");

	public static final String APP_KEY = "GET_A_BIKE";

	public GetABikeMIDlet() {
		super();
	}
	
	public String getAppKey() {
		return APP_KEY;
	}

	protected void onStart() {
		LoggerConfigurer loggerConfigurer = new XmlConfigurer("/org/helyx/app/j2me/getabike/logging4me.xml", true);
		loggerConfigurer.configure();
		
		setDefaultLocale(Locale.FRANCE);
		
		loadConfiguredLocale();
		
		setThemeConfiguration("org.helyx.app.j2me.getabike.theme", "default");
		loadThemeResourceBundle();
		
		setI18nConfiguration("org.helyx.app.j2me.getabike.i18n", "messages");		
		loadI18nResourceBundle();
		
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

	private void loadConfiguredLocale() {
		try {
			LanguageManager.configureLocaleWithCurentLanguage(this);
		}
		catch(Throwable t) {
			if (logger.isDebugEnabled()) {
				logger.debug(t);				
			}
		}
	}

	private void onStartSuccess(AbstractView view) {
		MenuView menuView = new MenuView(GetABikeMIDlet.this);
		boolean checkUpdate = AppManager.checkUpdateApplication(menuView, true, true);
		if (!checkUpdate) {
			menuView.show();
		}
	}

	private void onStartError(final AbstractView view, String message, Throwable t) {
		if (logger.isInfoEnabled()) {
			logger.info(message);
		}
		logger.warn(t);
		

		Throwable rootCause = ErrorUtil.getRootCause(t);

		DialogUtil.showMessageDialog(
				view, 
				"dialog.title.error", 
				getMessage("dialog.title.warn") + ": " + ErrorManager.getErrorMessage(this, t), 
				new OkResultCallback() {
					public void onOk(DialogView dialogView, Object data) {
						onStartSuccess(view);
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
