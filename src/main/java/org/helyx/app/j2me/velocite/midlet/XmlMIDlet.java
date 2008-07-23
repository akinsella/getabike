package org.helyx.app.j2me.velocite.midlet;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.helyx.app.j2me.lib.EncodingConstants;
import org.helyx.app.j2me.lib.constant.BooleanConstants;
import org.helyx.app.j2me.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.ui.view.XmlView;
import org.helyx.app.j2me.lib.ui.xml.widget.XmlCanvas;
import org.helyx.app.j2me.lib.xml.dom.DomUtil;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.kxml2.kdom.Document;
import org.xmlpull.v1.XmlPullParser;


public class XmlMIDlet extends MIDlet {

	private static final String CAT = "XML_MIDLET";
	
	public XmlMIDlet() {
		super();
		init();
	}

	private void init() {
		
	}
	
	protected void destroyApp(boolean arg0) {
	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		try { 
			PrefManager.writePref(PrefConstants.APPLICATION_DATA_CLEAN_UP_NEEDED, BooleanConstants.TRUE);
			Log.setThresholdLevel(Log.DEBUG);
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
		
		Log.info(CAT, "Platform name: '" + platformName + "'");
		Log.info(CAT, "Platform memory card: '" + memoryCard + "'");
	}

}
