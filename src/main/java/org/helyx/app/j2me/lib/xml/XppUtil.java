package org.helyx.app.j2me.lib.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.stream.StreamUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XppUtil {
	
	private static final String CAT = "XPP_UTIL";

	
	public static XmlPullParser createXpp(InputStream inputStream, String encoding) throws XmlPullParserException {
		boolean supportEncoding = StreamUtil.supportEncoding(encoding);
		Log.debug(CAT, "Encoding '" + encoding + "' supported: " + supportEncoding);
		Log.debug(CAT, "System encoding: " + System.getProperty("microedition.encoding"));
		
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

		xpp.next();
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			Log.debug(CAT, "Xpp eventType name: " + XmlPullParser.TYPES[eventType]);
			if (eventType == XmlPullParser.START_TAG) {
				Log.debug(CAT, "Xpp element name: " + xpp.getName());
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
