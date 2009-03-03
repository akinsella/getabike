package org.helyx.app.j2me.lib.xml.dom;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.xml.AbstractXmlAttributeProcessor;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;

public class DomAttributeProcessor extends AbstractXmlAttributeProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger("DOM_ATTRIBUTE_PROCESSOR");

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
		logger.debug("Element '" + elt.getName() + "' attribute count: " + attrCount);
		for (int i = 0 ; i < attrCount ; i++) {
			String attributeName = elt.getAttributeName(i);
			if (attributeList.contains(attributeName)) {
				logger.debug("Attribute[" + i + "] for Element[" + elt.getName() + "] name: '" + attributeName + "'");
				String attributeValue = elt.getAttributeValue(i);
				logger.debug("Associating value: '" +  attributeValue + "' to attribute name='" + attributeName + "'");
				attributeMap.put(attributeName, attributeValue);
			}
		}
	}

}
