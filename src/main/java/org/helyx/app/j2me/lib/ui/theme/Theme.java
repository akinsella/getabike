package org.helyx.app.j2me.lib.ui.theme;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.i18n.ResourceBundle;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.ui.graphics.Color;
import org.helyx.basics4me.lang.BooleanUtil;

public class Theme {
	
	private static final Log log = LogFactory.getLog("THEME");

	private ResourceBundle resourceBundle;
	private Hashtable colorCache = new Hashtable();
	
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
	
	public Color getColor(String key) {
		String colorValue = resourceBundle.get(key);
		if (colorCache.containsKey(colorValue)) {
			return (Color)colorCache.get(colorValue);
		}
		Color color = new Color(colorValue);
		colorCache.put(colorValue, color);
		return color;
	}

}
