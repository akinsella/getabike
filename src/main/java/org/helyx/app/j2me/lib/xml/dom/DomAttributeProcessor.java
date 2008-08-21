package org.helyx.app.j2me.lib.xml.dom;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.xml.AbstractXmlAttributeProcessor;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public class DomAttributeProcessor extends AbstractXmlAttributeProcessor {
	
	private static final Log log = LogFactory.getLog("DOM_ATTRIBUTE_PROCESSOR");

	public DomAttributeProcessor() {
		super();
	}
	
	public DomAttributeProcessor(String attributeName) {
		super(attributeName);
	}
	
	public DomAttributeProcessor(String[] attributeNames) {
		super(attributeNames);
	}

	public void processNode(Document doc, Element elt) {
		attributeMap.clear();
		int attrCount = elt.getAttributeCount();
		log.debug("Element '" + elt.getName() + "' attribute count: " + attrCount);
		for (int i = 0 ; i < attrCount ; i++) {
			String attributeName = elt.getAttributeName(i);
			if (attributeList.contains(attributeName)) {
				log.debug("Attribute[" + i + "] for Element[" + elt.getName() + "] name: '" + attributeName + "'");
				String attributeValue = elt.getAttributeValue(i);
				log.debug("Associating value: '" +  attributeValue + "' to attribute name='" + attributeName + "'");
				attributeMap.put(attributeName, attributeValue);
			}
		}
	}

}
