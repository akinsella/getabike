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
package org.helyx.app.j2me.getabike.lib.xml.xpp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.helyx.app.j2me.getabike.lib.stream.StreamUtil;
import org.helyx.app.j2me.getabike.lib.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XppUtil {
	
	private static final Logger logger = Logger.getLogger("XPP_UTIL");

	
//	public static XmlPullParser createXpp(InputStream inputStream, String encoding) throws XmlPullParserException {
//		return createXpp(inputStream, encoding, false);
//	}
	
	public static XmlPullParser createXpp(InputStream inputStream, String encoding /*, boolean prefetchBeforeParsing */) throws XmlPullParserException {
		boolean supportEncoding = StreamUtil.supportEncoding(encoding);
		logger.debug("Encoding '" + encoding + "' supported: " + supportEncoding);
		logger.debug("System encoding: " + System.getProperty("microedition.encoding"));
		
		XmlPullParser xpp = null;
		
//		if (prefetchBeforeParsing) {
//			byte[] bytes = new byte[256];
//			int length = 0;
//			ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
//			ByteArrayInputStream bais = null;
//			try {
//				while ((length = inputStream.read()) >= 0) {
//					if (logger.isInfoEnabled()) { 
//						logger.info("*** Bytes fetched: " + length);
//					}
//					if (length > 0) {
//						baos.write(length);
//					}
//				}
//
//				byte[] tmpBytes = baos.toByteArray();
//				bais = new ByteArrayInputStream(tmpBytes);
//				if (logger.isInfoEnabled()) { 
//					try {
//						String tmpString = new String(tmpBytes);
//						logger.info(tmpString);
//					} 
//					catch(Throwable t) {
//						logger.warn(t);
//					}
//				}
//				inputStream = bais;
//			}
//			catch(IOException ioe) {
//				logger.warn(ioe);
//				throw new RuntimeException(ioe.getMessage());
//			}
//			finally {
//				if (baos != null) {
//					try { baos.close(); } catch(Throwable t) { logger.warn(t); }
//				}
//				if (bais != null) {
//					try { bais.close(); } catch(Throwable t) { logger.warn(t); }
//				}
//			}
//		}
		
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
