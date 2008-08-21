package org.helyx.app.j2me.lib.reflect;

import java.util.Date;
import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class RefObject {

	private static final Log log = LogFactory.getLog("REF_OBJECT");

	Hashtable fieldMap = new Hashtable();
	
	public RefObject() {
		super();
	}
	
	public Object getRefObject(String fieldName) {
		RefObject refObject = (RefObject)fieldMap.get(fieldName);

		return refObject;
	}

	public boolean getBoolean(String fieldName) {
		Boolean boolObject = (Boolean)fieldMap.get(fieldName);

		if (boolObject == null) {
			return false;
		}
		
		boolean booleanValue = boolObject.booleanValue();
		
		return booleanValue;
	}

	public byte getByte(String fieldName) {
		Byte byteObject = (Byte)fieldMap.get(fieldName);

		if (byteObject == null) {
			return 0;
		}
		
		byte byteValue = byteObject.byteValue();
		
		return byteValue;
	}

	public short getShort(String fieldName) {
		Short shortObject = (Short)fieldMap.get(fieldName);

		if (shortObject == null) {
			return 0;
		}
		
		short shortValue = shortObject.shortValue();
		
		return shortValue;
	}

	public int getInt(String fieldName) {
		Integer intObject = (Integer)fieldMap.get(fieldName);

		if (intObject == null) {
			return 0;
		}
		
		int intValue = intObject.intValue();
		
		return intValue;
	}
	
	public long getLong(String fieldName) {
		Long longObject = (Long)fieldMap.get(fieldName);

		if (longObject == null) {
			return 0;
		}

		long longValue = longObject.longValue();

		return longValue;
	}
	
	public float getFloat(String fieldName) {
		Float floatObject = (Float)fieldMap.get(fieldName);

		if (floatObject == null) {
			return 0;
		}

		float floatValue = floatObject.floatValue();

		return floatValue;
	}
	
	public double getDouble(String fieldName) {
		Double doubleObject = (Double)fieldMap.get(fieldName);

		if (doubleObject == null) {
			return 0;
		}

		double doubleValue = doubleObject.doubleValue();

		return doubleValue;
	}
	
	public char getChar(String fieldName) {
		Character charObject = (Character)fieldMap.get(fieldName);

		if (charObject == null) {
			return 0;
		}
		
		char charValue = charObject.charValue();

		return charValue;
	}
	
	public String getString(String fieldName) {
		String stringObject = (String)fieldMap.get(fieldName);
		
		return stringObject;
	}
	
	public Date getDate(String fieldName) {
		Date dateObject = (Date)fieldMap.get(fieldName);
		if (dateObject == null) {
			return null;
		}
		
		return new Date(dateObject.getTime());
	}


	public void setRefObject(String fieldName, RefObject refObject) {
		fieldMap.put(fieldName, refObject);
	}

	public void setBoolean(String fieldName, boolean booleanValue) {
		fieldMap.put(fieldName, new Boolean(booleanValue));
	}

	public void setByte(String fieldName, byte byteValue) {
		fieldMap.put(fieldName, new Byte(byteValue));
	}

	public void setShort(String fieldName, short shortValue) {
		fieldMap.put(fieldName, new Short(shortValue));
	}

	public void setInt(String fieldName, int intValue) {
		fieldMap.put(fieldName, new Integer(intValue));
	}

	public void setLong(String fieldName, long longValue) {
		fieldMap.put(fieldName, new Long(longValue));
	}

	public void setFloat(String fieldName, float floatValue) {
		fieldMap.put(fieldName, new Float(floatValue));
	}

	public void setDouble(String fieldName, double doubleValue) {
		fieldMap.put(fieldName, new Float(doubleValue));
	}

	public void setChar(String fieldName, char charValue) {
		fieldMap.put(fieldName, new Character(charValue));
	}

	public void setDate(String fieldName, Date dateValue) {
		if (dateValue == null) {
			fieldMap.put(fieldName, null);
		}
		else {
			fieldMap.put(fieldName, new Date(dateValue.getTime()));
		}
	}

	public void setString(String fieldName, String stringValue) {
		fieldMap.put(fieldName, stringValue);
	}
	
	public String toString() {
		return fieldMap.toString();
	}

	public void clear() {
		fieldMap.clear();
	}

	public boolean containsKey(String key) {
		return fieldMap.containsKey(key);
	}

}
