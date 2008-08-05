package org.helyx.app.j2me.lib.text;

public class StringFormat {
	
	private static final String CAT = "STRING_FORMAT";
	
	private static final String FIRST = "{1}";
	
	private String stringValue;
	
	public StringFormat(String stringValue) {
		super();
		this.stringValue = stringValue;
	}
	
	public String format(byte byteValue) {
		String result = TextUtil.replaceAll(stringValue, FIRST, String.valueOf(byteValue));
		
		return result;
	}
	
	public String format(short shortValue) {
		String result = TextUtil.replaceAll(stringValue, FIRST, String.valueOf(shortValue));
		
		return result;
	}
	
	public String format(int intValue) {
		String result = TextUtil.replaceAll(stringValue, FIRST, String.valueOf(intValue));
		
		return result;
	}
	
	public String format(long longvalue) {
		String result = TextUtil.replaceAll(stringValue, FIRST, String.valueOf(longvalue));
		
		return result;
	}
	
	public String format(float floatValue) {
		String result = TextUtil.replaceAll(stringValue, FIRST, String.valueOf(floatValue));
		
		return result;
	}
	
	public String format(double doubleValue) {
		String result = TextUtil.replaceAll(stringValue, FIRST, String.valueOf(doubleValue));
		
		return result;
	}
	
	public String format(boolean booleanValue) {
		String result = TextUtil.replaceAll(stringValue, FIRST, String.valueOf(booleanValue));
		
		return result;
	}
	
	public String format(String stringValue) {
		String result = TextUtil.replaceAll(stringValue, FIRST, stringValue);
		
		return result;
	}

}
