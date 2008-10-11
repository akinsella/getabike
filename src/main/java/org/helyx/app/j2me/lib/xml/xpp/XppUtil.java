package org.helyx.app.j2me.lib.xml.xpp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.stream.StreamUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XppUtil {
	
	private static final Log log = LogFactory.getLog("XPP_UTIL");

	
	public static XmlPullParser createXpp(InputStream inputStream, String encoding) throws XmlPullParserException {
		boolean supportEncoding = StreamUtil.supportEncoding(encoding);
		log.debug("Encoding '" + encoding + "' supported: " + supportEncoding);
		log.debug("System encoding: " + System.getProperty("microedition.encoding"));
		
		XmlPullParser xpp = null;
		
		if (supportEncoding) {
			xpp = XppUtil.createXpp();
			xpp.setInput(inputStream, null);
		}
		else {
			xpp = XppUtil.createXpp();
			xpp.setInput(new InputStreamReader(inputStream));					
		}
		
		return xpp;
	}
	
	
	public static XmlPullParser createXpp() throws XmlPullParserException {

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

		factory.setNamespaceAware(true);
		factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);

		XmlPullParser xpp = factory.newPullParser();
        
        return xpp;
	}

	public static boolean readToNextElement(XmlPullParser xpp, String name) throws XmlPullParserException, IOException {
		return readToNextElement(xpp, name, true);
	}

	public static boolean readToNextElement(XmlPullParser xpp, String name, boolean iterateDirectly) throws XmlPullParserException, IOException {

		if (iterateDirectly) {
			xpp.next();
		}
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				if (xpp.getName().equals(name)) {
					return true;
				}
			}

			eventType = xpp.next();
		}
		return false;
	}

	public static boolean readNextElement(XmlPullParser xpp) throws XmlPullParserException, IOException {

		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				return true;
			}

			eventType = xpp.next();
		}
		return false;
	}

	public static String readNextText(XmlPullParser xpp) throws XmlPullParserException, IOException {
		return xpp.nextText();
	}
	
}
