package org.helyx.app.j2me.lib.midlet;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.lib.cache.Cache;
import org.helyx.app.j2me.lib.concurrent.Future;
import org.helyx.app.j2me.lib.constant.BooleanConstants;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.i18n.ClasspathResourceBundleContentProviderFactory;
import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.i18n.ResourceBundle;
import org.helyx.app.j2me.lib.i18n.ResourceBundleConfiguration;
import org.helyx.app.j2me.lib.log.FileLogWriter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.ui.theme.Theme;
import org.helyx.app.j2me.velocite.PrefConstants;

public class AbstractMIDlet extends MIDlet {

	private static final String CAT = "ABSTRACT_MIDLET";
	
	private FileLogWriter flw;
	private Theme theme;
	private ResourceBundle resourceBundle;
	private Locale locale;
	private Locale defaultLocale;
	private Cache cache;
	
	private ResourceBundleConfiguration themeConfiguration;
	private ResourceBundleConfiguration i18nConfiguration;
	
	public AbstractMIDlet() {
		super();
		init();
	}

	private void init() {
		cache = new Cache();
	}
	
	public Cache getCache() {
		return cache;
	}
	
	public void exit() {
		notifyDestroyed();
	}
	
	public void pause() {
		notifyPaused();
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public String getMessage(String key) {
		return resourceBundle.get(key);
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		try { loadI18nResourceBundle(getLocale()); } 
		catch(Throwable t) {
			Log.warn(CAT, t);
			try { loadI18nResourceBundle(getDefaultLocale()); } 
			catch(Throwable t1) {
				Log.warn(CAT, t1);
			}
		}
		try { loadThemeResourceBundle(getLocale()); }
		catch(Throwable t) {
			Log.warn(CAT, t);
			try { loadThemeResourceBundle(getDefaultLocale()); } 
			catch(Throwable t1) {
				Log.warn(CAT, t1);
			}
		}
	}
	protected void loadThemeResourceBundle(Locale locale) {
		
		if (themeConfiguration == null) {
			themeConfiguration = new ResourceBundleConfiguration("default", "org.helyx.app.j2me.lib.theme");
		}
		ClasspathResourceBundleContentProviderFactory cprbcpf = new ClasspathResourceBundleContentProviderFactory(locale, themeConfiguration.getPackageName(), themeConfiguration.getName());
		ResourceBundle resourceBundle = (ResourceBundle)Future.get(new ContentProviderProgressTaskAdapter(cprbcpf.getContentProviderFactory()));
		Theme theme = new Theme(resourceBundle);
		
		this.theme = theme;
	}
	
	protected void loadI18nResourceBundle(Locale locale) {
		if (i18nConfiguration == null) {
			i18nConfiguration = new ResourceBundleConfiguration("messages", "org.helyx.app.j2me.lib.i18n");
		}
		
		ClasspathResourceBundleContentProviderFactory cprbcpf = new ClasspathResourceBundleContentProviderFactory(locale, i18nConfiguration.getPackageName(), i18nConfiguration.getName());
		ResourceBundle resourceBundle = (ResourceBundle)Future.get(new ContentProviderProgressTaskAdapter(cprbcpf.getContentProviderFactory()));

		this.resourceBundle = resourceBundle;
	}

	public Theme getTheme() {
		return theme;
	}
	
	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	protected void destroyApp(boolean flag) {
//			closeFileLogWriter();
		onDestroy(flag);
	}

	protected void pauseApp() {
//			closeFileLogWriter();
		onPause();
	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			setDefaultLocale(Locale.ENGLAND);
			setLocale(Locale.ENGLAND);
			PrefManager.writePref(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
			Log.setThresholdLevel(Log.DEBUG);
	//		openFileLogWriter();
			logPlatformInfos();
			onStart();
		} 
		catch (Throwable t) {
			t.printStackTrace();
			notifyDestroyed();
		}
	}

	protected void onDestroy(boolean flag) {

	}
	
	protected void onPause() {

	}
	
	protected void onStart() {
		
	}

	public ResourceBundleConfiguration getI18nConfiguration() {
		return i18nConfiguration;
	}

	protected void setI18nConfiguration(String name, String packageConfiguration) {
		ResourceBundleConfiguration i18nConfiguration = new ResourceBundleConfiguration(name, packageConfiguration);
		setI18nConfiguration(i18nConfiguration);
	}

	protected void setI18nConfiguration(ResourceBundleConfiguration i18nConfiguration) {
		this.i18nConfiguration = i18nConfiguration;
	}

	public ResourceBundleConfiguration getThemeConfiguration() {
		return themeConfiguration;
	}
	
	protected void setThemeConfiguration(String name, String packageConfiguration) {
		ResourceBundleConfiguration themeConfiguration = new ResourceBundleConfiguration(name, packageConfiguration);
		setThemeConfiguration(themeConfiguration);
	}
	
	protected void setThemeConfiguration(ResourceBundleConfiguration themeConfiguration) {
		this.themeConfiguration = themeConfiguration;
	}

	private void logPlatformInfos() {
		String platformName = System.getProperty(PrefConstants.MICROEDITION_PLATFORM);
		String memoryCard = System.getProperty(PrefConstants.FILECONN_DIR_MEMORYCARD);
		
		Log.info(CAT, "Platform name: '" + platformName + "'");
		Log.info(CAT, "Platform memory card: '" + memoryCard + "'");
	}

	
	private void openFileLogWriter() {
		if (flw == null) {
			try {
				flw = new FileLogWriter("VeloCite.log");
				flw.open();
				Log.addLogWriter(flw);
			}
			catch(Throwable t) {
				t.printStackTrace();
				flw = null;
			}
		}

	}
	
	private void closeFileLogWriter() {
		if (flw != null) {
			try {
				Log.removeLogWriter(flw);
				flw.flush();
				flw.close();
			}
			catch(Throwable t) {
				t.printStackTrace();
				flw = null;
			}
		}

	}

}
