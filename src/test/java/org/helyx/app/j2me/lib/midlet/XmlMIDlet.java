package org.helyx.app.j2me.lib.midlet;

import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.helyx4me.constant.BooleanConstants;
import org.helyx.helyx4me.constant.EncodingConstants;
import org.helyx.helyx4me.content.accessor.ClasspathContentAccessor;
import org.helyx.helyx4me.midlet.AbstractMIDlet;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.helyx4me.ui.view.support.xml.XmlView;
import org.helyx.helyx4me.xml.dom.DomUtil;
import org.helyx.log4me.Logger;
import org.helyx.log4me.LoggerFactory;
import org.helyx.log4me.appender.ConsoleAppender;
import org.kxml2.kdom.Document;

public class XmlMIDlet extends AbstractMIDlet {

	private static final Logger logger = LoggerFactory.getLogger("XML_MIDLET");
	
	public XmlMIDlet() {
		super();
		init();
	}

	private void init() {
		
	}
	
	protected void destroyApp(boolean flag) {
	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		try { 
			PrefManager.writePref(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
			ConsoleAppender.getInstance().setThresholdLevel(Logger.DEBUG);
			logPlatformInfos();
			
			ClasspathContentAccessor cca = new ClasspathContentAccessor("/org/helyx/app/j2me/velocite/view/xml/welcomeView.xml");
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

}
