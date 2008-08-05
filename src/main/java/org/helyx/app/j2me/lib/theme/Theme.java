package org.helyx.app.j2me.lib.theme;

import org.helyx.app.j2me.lib.i18n.ResourceBundle;
import org.helyx.basics4me.lang.BooleanUtil;

public class Theme {
	
	private static final String CAT = "THEME";

	private ResourceBundle resourceBundle;
	
	public Theme(ResourceBundle resourceBundle) {
		super();
		this.resourceBundle = resourceBundle;
	}
	
	public byte getByte(String key) {
		byte byteValue = Byte.parseByte(resourceBundle.get(key));
		
		return byteValue;
	}
	
	public short getShort(String key) {
		short shortValue = Short.parseShort(resourceBundle.get(key));
		
		return shortValue;
	}
	
	public int getInt(String key) {
		int intValue = Integer.parseInt(resourceBundle.get(key));
		
		return intValue;
	}
	
	public long getLong(String key) {
		long longValue = Long.parseLong(resourceBundle.get(key));
		
		return longValue;
	}
	
	public float getFloat(String key) {
		float floatValue = Float.parseFloat(resourceBundle.get(key));
		
		return floatValue;
	}
	
	public double getDouble(String key) {
		double doubleValue = Double.parseDouble(resourceBundle.get(key));
		
		return doubleValue;
	}
	
	public boolean getBoolean(String key) {
		boolean booleanValue = BooleanUtil.parseBoolean(resourceBundle.get(key));
		
		return booleanValue;
	}

	public String getString(String key) {
		String stringValue = resourceBundle.get(key);
		
		return stringValue;
	}

}
