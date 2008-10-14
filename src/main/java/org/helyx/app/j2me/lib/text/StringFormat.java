package org.helyx.app.j2me.lib.text;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class StringFormat {
	
	private static final Log log = LogFactory.getLog("STRING_FORMAT");
	
	private static final String NULL = "null";
	
	private static final String FIRST = "{1}";
	
	private static final String START = "{";
	private static final String END = "}";
	
	private String originalContent;
	
	public StringFormat(String originalContent) {
		super();
		this.originalContent = originalContent;
	}
	
	public String format(byte byteValue) {
		String result = TextUtil.replaceAll(originalContent, FIRST, String.valueOf(byteValue));
		
		return result;
	}
	
	public String format(short shortValue) {
		String result = TextUtil.replaceAll(originalContent, FIRST, String.valueOf(shortValue));
		
		return result;
	}
	
	public String format(int intValue) {
		String result = TextUtil.replaceAll(originalContent, FIRST, String.valueOf(intValue));
		
		return result;
	}
	
	public String format(long longvalue) {
		String result = TextUtil.replaceAll(originalContent, FIRST, String.valueOf(longvalue));
		
		return result;
	}
	
	public String format(float floatValue) {
		String result = TextUtil.replaceAll(originalContent, FIRST, String.valueOf(floatValue));
		
		return result;
	}
	
	public String format(double doubleValue) {
		String result = TextUtil.replaceAll(originalContent, FIRST, String.valueOf(doubleValue));
		
		return result;
	}
	
	public String format(boolean booleanValue) {
		String result = TextUtil.replaceAll(originalContent, FIRST, String.valueOf(booleanValue));
		
		return result;
	}
	
	public String format(String stringValue) {
		String result = TextUtil.replaceAll(this.originalContent, FIRST, stringValue);
		
		return result;
	}

	public String format(Object objectValue) {
		String result = null;
		if (objectValue instanceof String) {
			result = TextUtil.replaceAll(originalContent, FIRST, (String)objectValue);
		}
		else {
			result = TextUtil.replaceAll(originalContent, FIRST, objectValue.toString());			
		}
		
		return result;
	}

	public String format(Object[] objectValues) {
		int length = objectValues.length;
		String result = originalContent;
		for (int i = 0 ; i < length ; i++) {
			Object objectValue = objectValues[i];
			String token = new StringBuffer(START).append(i + 1).append(END).toString();
			if (objectValue instanceof String) {
				result = TextUtil.replaceAll(result, token, objectValue == null ? NULL : (String)objectValue);		
			}
			else {
				result = TextUtil.replaceAll(result, token, objectValue == null ? NULL : objectValue.toString());
			}
		}
		
		return result;
	}

}
