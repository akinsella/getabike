package org.helyx.app.j2me.lib.midlet;

import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.lib.constant.BooleanConstants;
import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.log.appender.ConsoleAppender;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.ui.view.support.xml.XmlView;
import org.helyx.app.j2me.lib.xml.dom.DomUtil;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.kxml2.kdom.Document;

public class XmlMIDlet extends AbstractMIDlet {

	private static final Log log = LogFactory.getLog("XML_MIDLET");
	
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
			ConsoleAppender.getInstance().setThresholdLevel(Log.DEBUG);
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
		
		log.info("Platform name: '" + platformName + "'");
		log.info("Platform memory card: '" + memoryCard + "'");
	}

}
