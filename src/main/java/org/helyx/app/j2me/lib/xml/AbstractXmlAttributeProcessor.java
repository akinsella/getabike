package org.helyx.app.j2me.lib.xml;

import java.util.Hashtable;
import java.util.Vector;

import org.helyx.app.j2me.lib.constant.BooleanConstants;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class AbstractXmlAttributeProcessor {
	
	private static final Log log = LogFactory.getLog("ABSTRACT_XML_ATTRIBUTE_PROCESSOR");
		
	protected Hashtable attributeMap;
	protected Vector attributeList;
	
	public AbstractXmlAttributeProcessor() {
		init();
	}
	
	public AbstractXmlAttributeProcessor(String attributeName) {
		this();
		add(attributeName);
	}
	
	public AbstractXmlAttributeProcessor(String[] attributeNames) {
		this();
		addAll(attributeNames);
	}
	
	private void init() {
		attributeList = new Vector();
		attributeMap = new Hashtable();
	}
	
	public void add(String attributeName) {
		attributeList.addElement(attributeName);
	}
	
	public void remove(String attributeName) {
		attributeList.removeElement(attributeName);
	}
	
	public void removeAll() {
		attributeList.removeAllElements();
	}

	public void addAll(String[] attributeNames) {
		int length = attributeNames.length;
		
		for (int i = 0 ; i < length ; i++) {
			attributeList.addElement(attributeNames[i]);
		}
	}
	
	public boolean attrExists(String attributeName) {
		return attributeMap.containsKey(attributeName);
	}
	
	public boolean getAttrValueAsBoolean(String attributeName) {
		String booleanValue = (String)attributeMap.get(attributeName);
		
		if (booleanValue == null || booleanValue.length() == 0) {
			return false;
		}
		return BooleanConstants.TRUE.compareTo(booleanValue.toLowerCase()) == 0;
	}
	
	public byte getAttrValueAsByte(String attributeName) {
		String byteValue = (String)attributeMap.get(attributeName);
		
		if (byteValue == null || byteValue.length() == 0) {
			return 0;
		}
		return Byte.parseByte(byteValue);
	}
	
	public short getAttrValueAsShort(String attributeName) {
		String shortValue = (String)attributeMap.get(attributeName);
		
		if (shortValue == null || shortValue.length() == 0) {
			return 0;
		}
		return Short.parseShort(shortValue);
	}
	
	public int getAttrValueAsInt(String attributeName) {
		String intValue = (String)attributeMap.get(attributeName);
		
		if (intValue == null || intValue.length() == 0) {
			return 0;
		}
		return Integer.parseInt(intValue);
	}
	
	public long getAttrValueAsLong(String attributeName) {
		String longValue = (String)attributeMap.get(attributeName);
		
		if (longValue == null || longValue.length() == 0) {
			return 0;
		}
		return Long.parseLong(longValue);
	}

	public float getAttrValueAsFloat(String attributeName) {
		String floatValue = (String)attributeMap.get(attributeName);
		
		if (floatValue == null || floatValue.length() == 0) {
			return 0;
		}
		return Float.parseFloat(floatValue);
	}
	
	public double getAttrValueAsDouble(String attributeName) {
		String doubleValue = (String)attributeMap.get(attributeName);
		
		if (doubleValue == null || doubleValue.length() == 0) {
			return 0;
		}
		return Double.parseDouble((String)attributeMap.get(attributeName));
	}
	
	public String getAttrValueAsString(String attributeName) {
		return (String)attributeMap.get(attributeName);
	}

}
