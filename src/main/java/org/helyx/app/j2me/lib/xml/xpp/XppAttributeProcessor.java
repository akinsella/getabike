package org.helyx.app.j2me.lib.xml.xpp;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.xml.AbstractXmlAttributeProcessor;
import org.xmlpull.v1.XmlPullParser;

public class XppAttributeProcessor extends AbstractXmlAttributeProcessor {
	
	private static final Log log = LogFactory.getLog("XPP_ATTRIBUTE_PROCESSOR");
		
	public XppAttributeProcessor() {
		super();
	}
	
	public XppAttributeProcessor(String attributeName) {
		super(attributeName);
	}
	
	public XppAttributeProcessor(String[] attributeNames) {
		super(attributeNames);
	}

	public void processNode(XmlPullParser xpp) {
		attributeMap.clear();
		int attrCount = xpp.getAttributeCount();
		log.debug("Element '" + xpp.getName() + "' attribute count: " + attrCount);
		for (int i = 0 ; i < attrCount ; i++) {
			String attributeName = xpp.getAttributeName(i);
			if (attributeList.contains(attributeName)) {
				log.debug("Attribute[" + i + "] for Element[" + xpp.getName() + "] name: '" + attributeName + "'");
				String attributeValue = xpp.getAttributeValue(i);
				log.debug("Associating value: '" +  attributeValue + "' to attribute name='" + attributeName + "'");
				attributeMap.put(attributeName, attributeValue);
			}
		}
	}

}
