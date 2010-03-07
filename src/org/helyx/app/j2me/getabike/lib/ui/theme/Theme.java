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
package org.helyx.app.j2me.getabike.lib.ui.theme;

import java.util.Hashtable;

import org.helyx.basics4me.lang.BooleanUtil;
import org.helyx.app.j2me.getabike.lib.i18n.ResourceBundle;
import org.helyx.app.j2me.getabike.lib.ui.graphics.Color;
import org.helyx.logging4me.Logger;


public class Theme {
	
	private static final Logger logger = Logger.getLogger("THEME");

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
