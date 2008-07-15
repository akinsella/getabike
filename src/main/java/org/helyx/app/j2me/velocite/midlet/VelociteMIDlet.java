package org.helyx.app.j2me.velocite.midlet;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.lib.constant.BooleanConstants;
import org.helyx.app.j2me.lib.log.FileLogWriter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.view.SplashScreenView;


public class VelociteMIDlet extends MIDlet {

	private static final String CAT = "VELOCITE_MIDLET";
	
	private FileLogWriter flw;
	
	public VelociteMIDlet() {
		super();
		init();
	}

	private void init() {
		
	}
	
	protected void destroyApp(boolean arg0) {
//		closeFileLogWriter();
	}

	protected void pauseApp() {
//		closeFileLogWriter();
	}

	protected void startApp() throws MIDletStateChangeException {
		try { 
			PrefManager.writePref(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
			Log.setThresholdLevel(Log.INFO);
//			openFileLogWriter();
			logPlatformInfos();
			
			SplashScreenView splashScreenView = new SplashScreenView(this);
			splashScreenView.show();
		} 
		catch (Throwable t) {
			t.printStackTrace();
			notifyDestroyed();
		}
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
