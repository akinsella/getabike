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
package org.helyx.app.j2me.getabike.lib.test.list;

import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.getabike.lib.i18n.Locale;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.LoggerManager;
import org.helyx.logging4me.appender.ConsoleAppender;
import org.helyx.logging4me.layout.pattern.PatternLayout;


public class TestMenuMIDlet extends AbstractMIDlet {

	private static final Logger logger = Logger.getLogger("GETABIKE_MIDLET");

	public TestMenuMIDlet() {
		super();
	}

	protected void initLog() {
		LoggerManager.setThresholdLevel(Logger.DEBUG);
		
		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setLayout(new PatternLayout("| %T | %L | %C | %D{yyyy/MM/dd, HH:mm:ss.ZZ} | "));
		consoleAppender.setThresholdLevel(Logger.DEBUG);
		
		LoggerManager.registerAppender(consoleAppender);
		
		LoggerManager.getRootCategory().addAppender(consoleAppender);

		LoggerManager.addCategory("org.helyx.app.j2me.getabike.lib", Logger.INFO);
	}
	
	protected void onDestroy(boolean unconditional) throws MIDletStateChangeException {
		LoggerManager.dispose();
	}

	protected void onStart() {
		initLog();
		
		setLocale(Locale.FRANCE);
		
		setI18nConfiguration("org.helyx.app.j2me.getabike.lib.test.i18n", "messages");
		loadI18nResourceBundle();

		setThemeConfiguration("org.helyx.app.j2me.getabike.lib.test.theme", "default");
		loadThemeResourceBundle();

		final MenuView menuView = new MenuView(this);
		menuView.getViewCanvas().setDebug(false);

		menuView.show();
	}

	public String getAppKey() {
		return "TEST_APP_KEY";
	}

}
