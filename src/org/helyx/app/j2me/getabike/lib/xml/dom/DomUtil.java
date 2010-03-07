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
package org.helyx.app.j2me.getabike.lib.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;

import org.helyx.logging4me.Logger;

import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

public class DomUtil {

	private static final Logger logger = Logger.getLogger("DOM_UTIL");

	public static Document parseDoc(Reader reader) throws XmlPullParserException, IOException {
		KXmlParser parser = new KXmlParser();
		
		parser.setInput(reader);
		Document document = new Document();
        document.parse(parser);			
        
        return document;
	}

	public static Document parseDoc(InputStream inputStream, String encoding) throws XmlPullParserException, IOException {
		KXmlParser parser = new KXmlParser();
		
		parser.setInput(inputStream, encoding);
		Document document = new Document();
        document.parse(parser);
        
        return document;
	}
	
	public static Enumeration elementIterator(final Element parentElt) {
		Enumeration enumeration = elementIterator(parentElt, null); 
		
		return enumeration;
	}
	
	public static Enumeration elementIterator(final Element parentElt, final String name) {
	
		Enumeration it = new Enumeration() {

			private Element nextElt;
			private int counter;
			private int parentEltCount;
			private boolean isInit = false;

			private void checkInit() {
				if (!isInit) {
					parentEltCount = parentElt.getChildCount();
					nextElt = fetchNextElement();
					isInit = true;
				}
			}
			
			public boolean hasMoreElements() {
				checkInit();
				return nextElt != null;
			}

			public Object nextElement() {
				checkInit();
				try {
					return nextElt;
				}
				finally {
					nextElt = fetchNextElement();
				}
			}
			
			private Element fetchNextElement() {
				for ( ; counter < parentEltCount ; counter++) {
					Object node = (Object)parentElt.getChild(counter);
					int type = parentElt.getType(counter);
					if (type == Node.ELEMENT) {
						Element elt = (Element)node;
						if (name == null || name.equals(elt.getName())) {
							counter++;
							return elt;
						}
					}
				}
				return null;
			}
			
		};
		
		return it;
	}

	public static Element element(Element parentElt, String name) {
		int childCount = parentElt.getChildCount();
		
		for (int i = 0 ; i < childCount ; i++) {
			Object node = (Object)parentElt.getChild(i);
			int type = parentElt.getType(i);
			if (type == Node.ELEMENT) {
				Element elt = (Element)node;
				if (name.equals(elt.getName())) {
					return elt;
				}
			}
		}
		
		return null;
	}
	
}
