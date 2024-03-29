package org.helyx.app.j2me.getabike.lib.test.midlet;

import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.lib.constant.BooleanConstants;
import org.helyx.app.j2me.getabike.lib.constant.EncodingConstants;
import org.helyx.app.j2me.getabike.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.getabike.lib.midlet.AbstractMIDlet;
import org.helyx.app.j2me.getabike.lib.pref.PrefManager;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.ui.view.support.xml.XmlView;
import org.helyx.app.j2me.getabike.lib.xml.dom.DomUtil;
import org.helyx.logging4me.Logger;
import org.helyx.logging4me.LoggerManager;
import org.helyx.logging4me.appender.ConsoleAppender;
import org.helyx.logging4me.layout.SimpleLayout;
import org.kxml2.kdom.Document;

public class XmlMIDlet extends AbstractMIDlet {

	private static final Logger logger = Logger.getLogger("XML_MIDLET");
	
	public XmlMIDlet() {
		super();
	}
	
	protected void destroyApp(boolean flag) {
	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		try { 
			PrefManager.writePref(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
			
			LoggerManager.setThresholdLevel(Logger.DEBUG);
			
			ConsoleAppender consoleAppender = new ConsoleAppender();
			consoleAppender.setLayout(new SimpleLayout());
			consoleAppender.setThresholdLevel(Logger.DEBUG);
			
			LoggerManager.registerAppender(consoleAppender);
			LoggerManager.getRootCategory().addAppender(consoleAppender);

			logPlatformInfos();
			
			ClasspathContentAccessor cca = new ClasspathContentAccessor("/org/helyx/app/j2me/getabike/view/xml/welcomeView.xml");
			InputStreamProvider isp = cca.getInputStreamProvider();
			
			try {
//				XmlPullParser xpp = XppUtil.createXpp(isp.createInputStream(), EncodingConstants.UTF_8);
				Document doc = DomUtil.parseDoc(isp.createInputStream(), EncodingConstants.UTF_8);
				
				XmlView xmlView = new XmlView(this);
				xmlView.configure(doc);

				
				xmlView.show();
			}
			finally {
				isp.dispose();
			}
		} 
		catch (Throwable t) {
			t.printStackTrace();
			notifyDestroyed();
		}
	}

	private void logPlatformInfos() {
		String platformName = System.getProperty(PrefConstants.MICROEDITION_PLATFORM);
		String memoryCard = System.getProperty(PrefConstants.FILECONN_DIR_MEMORYCARD);
		
		logger.info("Platform name: '" + platformName + "'");
		logger.info("Platform memory card: '" + memoryCard + "'");
	}

	public String getAppKey() {
		return "XML_APP_KEY";
	}

}
