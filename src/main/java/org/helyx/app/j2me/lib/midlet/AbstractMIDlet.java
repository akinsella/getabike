package org.helyx.app.j2me.lib.midlet;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.lib.constant.BooleanConstants;
import org.helyx.app.j2me.lib.i18n.Locale;
import org.helyx.app.j2me.lib.log.FileLogWriter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.theme.Theme;
import org.helyx.app.j2me.velocite.PrefConstants;

public class AbstractMIDlet extends MIDlet {

		private static final String CAT = "ABSTRACT_MIDLET";
		
		private FileLogWriter flw;
		private Theme theme;
		private Locale locale;
		
		public AbstractMIDlet() {
			super();
			init();
		}

		private void init() {
			
		}

		public Locale getLocale() {
			return locale;
		}

		public void setLocale(Locale locale) {
			this.locale = locale;
		}
		
		public Theme getTheme() {
			return theme;
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
				setLocale(Locale.FRANCE);
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
