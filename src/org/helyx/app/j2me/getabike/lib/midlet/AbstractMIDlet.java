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
package org.helyx.app.j2me.getabike.lib.midlet;

import java.util.Timer;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.getabike.lib.cache.Cache;
import org.helyx.app.j2me.getabike.lib.concurrent.Future;
import org.helyx.app.j2me.getabike.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.getabike.lib.i18n.ClasspathResourceBundleContentProviderFactory;
import org.helyx.app.j2me.getabike.lib.i18n.Locale;
import org.helyx.app.j2me.getabike.lib.i18n.ResourceBundle;
import org.helyx.app.j2me.getabike.lib.i18n.ResourceBundleConfiguration;
import org.helyx.app.j2me.getabike.lib.pref.PrefConstants;
import org.helyx.app.j2me.getabike.lib.renderer.text.I18NTextRenderer;
import org.helyx.app.j2me.getabike.lib.renderer.text.ITextRenderer;
import org.helyx.app.j2me.getabike.lib.text.StringFormat;
import org.helyx.app.j2me.getabike.lib.ui.theme.Theme;
import org.helyx.logging4me.Logger;

public abstract class AbstractMIDlet extends MIDlet {

	private static final Logger logger = Logger.getLogger("ABSTRACT_MIDLET");

	private Theme theme;
	private ResourceBundle resourceBundle;
	private Locale locale;
	private Locale defaultLocale;
	private Cache cache;
	
	private ResourceBundleConfiguration themeConfiguration;
	private ResourceBundleConfiguration i18nConfiguration;
	
	private ITextRenderer i18nTextRenderer;
	
	private Timer timer;
	
	public AbstractMIDlet() {
		super();
	}
	
	public abstract String getAppKey();
	
	public Cache getCache() {
		return cache;
	}
	
	public void exit() {
		 try {
			destroyApp(false);
		}
		 catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void pause() {
		notifyPaused();
	}
	
	public ITextRenderer getI18NTextRenderer() {
		return i18nTextRenderer;
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

	public boolean containsMessage(String key) {
		return resourceBundle.containsKey(key);
	}

	
	public String getMessage(String key, Object object) {
		return 	new StringFormat(resourceBundle.get(key)).format(new Object[] { object });
	}
	
	public String getMessage(String key, Object[] objects) {
		return 	new StringFormat(resourceBundle.get(key)).format(objects);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public void loadCurrentLocale() {
		loadI18nResourceBundle();
		loadThemeResourceBundle();
	}
	
	public void loadI18nResourceBundle() {
		try { 
			Locale locale = getLocale();
			if (locale != null) {
				loadI18nResourceBundle(locale);
				return;
			}
		} 
		catch(Throwable t) {
			logger.warn(t);
		}
		try {
			Locale defaultLocale = getDefaultLocale();
			logger.debug("Default Locale: '" + defaultLocale + "'");

			loadI18nResourceBundle(defaultLocale);
		} 
		catch(Throwable t1) {
			logger.warn(t1);
		}
	}
	
	public void loadThemeResourceBundle() {
		try {
			Locale locale = getLocale();
			if (locale != null) {
				loadThemeResourceBundle(locale);
				return ;
			}
		}
		catch(Throwable t) {
			logger.warn(t);
		}
		try {
			Locale defaultLocale = getDefaultLocale();
			logger.debug("Default Locale: '" + defaultLocale + "'");
			
			loadThemeResourceBundle(defaultLocale); 
		}
		catch(Throwable t1) {
			logger.warn(t1);
		}
	}
	
	protected void loadThemeResourceBundle(Locale locale) {
		logger.debug("Locale: '" + locale + "'");
		ClasspathResourceBundleContentProviderFactory cprbcpf = new ClasspathResourceBundleContentProviderFactory(locale, themeConfiguration.getPackageName(), themeConfiguration.getName());
		ResourceBundle resourceBundle = (ResourceBundle)Future.get(new ContentProviderProgressTaskAdapter(cprbcpf.getContentProviderFactory()));
		Theme theme = new Theme(resourceBundle);
		
		this.theme = theme;
	}
	
	protected void loadI18nResourceBundle(Locale locale) {

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
	
	public Timer getTimer() {
		return timer;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		try {
			onDestroy(unconditional);
			dispose();
		}
    	catch(Throwable t) {
    		t.printStackTrace();
    	}
	    notifyDestroyed();
	}

	protected void dispose() {
		if (timer != null) {
			timer.cancel();
		}
	}
	
	private boolean pause = false;

	protected void pauseApp() {
		pause = true;
		onPause();
	}
	
	private void init() {
		cache = new Cache();
		i18nTextRenderer = new I18NTextRenderer(this);
		timer = new Timer();
		setDefaultLocale(Locale.ENGLAND);
	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			if (pause) {
				pause = false;
				onRestart();
			}
			else {
				init();				
				onStart();				
			}
		} 
		catch (Throwable t) {
			t.printStackTrace();
			destroyApp(false);
		}
	}
	
	protected void onRestart() {
		
	}

	protected void onStart() {
		
	}
	
	protected void onPause() {

	}

	protected void onDestroy(boolean unconditional) throws MIDletStateChangeException {

	}

	public ResourceBundleConfiguration getI18nConfiguration() {
		return i18nConfiguration;
	}

	protected void setI18nConfiguration(String packageConfiguration, String name) {
		ResourceBundleConfiguration i18nConfiguration = new ResourceBundleConfiguration(packageConfiguration, name);
		setI18nConfiguration(i18nConfiguration);
	}

	protected void setI18nConfiguration(ResourceBundleConfiguration i18nConfiguration) {
		this.i18nConfiguration = i18nConfiguration;
	}

	public ResourceBundleConfiguration getThemeConfiguration() {
		return themeConfiguration;
	}
	
	protected void setThemeConfiguration(String packageConfiguration, String name) {
		ResourceBundleConfiguration themeConfiguration = new ResourceBundleConfiguration(packageConfiguration, name);
		setThemeConfiguration(themeConfiguration);
	}
	
	protected void setThemeConfiguration(ResourceBundleConfiguration themeConfiguration) {
		this.themeConfiguration = themeConfiguration;
	}

	private void logPlatformInfos() {
		String platformName = System.getProperty(PrefConstants.MICROEDITION_PLATFORM);
		String memoryCard = System.getProperty(PrefConstants.FILECONN_DIR_MEMORYCARD);
		if (logger.isDebugEnabled()) {
			logger.debug("Platform name: '" + platformName + "'");
			logger.debug("Platform memory card: '" + memoryCard + "'");
		}
	}

}
