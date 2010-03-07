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
package org.helyx.app.j2me.getabike.lib.reflect;

import java.util.Hashtable;

import org.helyx.app.j2me.getabike.lib.reflect.UnsupportedTypeException;

public class FieldType {

	public static final int BYTE = 1;
	public static final String BYTE_STR = "BYTE";
	
	public static final int SHORT = 2;
	public static final String SHORT_STR = "SHORT";
	
	public static final int INT = 3;
	public static final String INT_STR = "INT";

	public static final int LONG = 4;
	public static final String LONG_STR = "LONG";

	
	public static final int FLOAT = 5;
	public static final String FLOAT_STR = "FLOAT";

	public static final int DOUBLE = 6;
	public static final String DOUBLE_STR = "DOUBLE";

	
	public static final int CHAR = 7;
	public static final String CHAR_STR = "CHAR";

	public static final int STRING = 8;
	public static final String STRING_STR = "STRING";

	public static final int DATE = 9;
	public static final String DATE_STR = "DATE";
	
	public static final int BOOLEAN = 10;
	public static final String BOOLEAN_STR = "BOOLEAN";
	
	private static Hashtable nameToIndexMap;
	private static Hashtable indexToNameMap;
	
	static {
		nameToIndexMap = new Hashtable();
		nameToIndexMap.put(BYTE_STR, new Integer(BYTE));
		nameToIndexMap.put(SHORT_STR, new Integer(SHORT));
		nameToIndexMap.put(INT_STR, new Integer(INT));
		nameToIndexMap.put(LONG_STR, new Integer(LONG));
		nameToIndexMap.put(FLOAT_STR, new Integer(FLOAT));
		nameToIndexMap.put(DOUBLE_STR, new Integer(DOUBLE));
		nameToIndexMap.put(CHAR_STR, new Integer(CHAR));
		nameToIndexMap.put(STRING_STR, new Integer(STRING));
		nameToIndexMap.put(DATE_STR, new Integer(DATE));
		nameToIndexMap.put(BOOLEAN_STR, new Integer(BOOLEAN));

		indexToNameMap = new Hashtable();
		indexToNameMap.put(new Integer(BYTE), BYTE_STR);
		indexToNameMap.put(new Integer(SHORT), SHORT_STR);
		indexToNameMap.put(new Integer(INT), INT_STR);
		indexToNameMap.put(new Integer(LONG), LONG_STR);
		indexToNameMap.put(new Integer(FLOAT), FLOAT_STR);
		indexToNameMap.put(new Integer(DOUBLE), DOUBLE_STR);
		indexToNameMap.put(new Integer(CHAR), CHAR_STR);
		indexToNameMap.put(new Integer(STRING), STRING_STR);
		indexToNameMap.put(new Integer(DATE), DATE_STR);
		indexToNameMap.put(new Integer(BOOLEAN), BOOLEAN_STR);

	}
	
	private FieldType() {
		super();
	}
	
	public static int getIndex(String typeName) {
		Integer integerValue = (Integer)nameToIndexMap.get(typeName);
		if (integerValue == null) {
			throw new UnsupportedTypeException("Unknown typeName: " + typeName);
		}
		return integerValue.intValue();
	}

	public static String getName(int type) {
		String stringValue = (String)indexToNameMap.get(new Integer(type));
		if (stringValue == null) {
			throw new UnsupportedTypeException("Unknown type: " + type);
		}
		return stringValue;
	}
	
}
