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
package org.helyx.app.j2me.getabike.lib.util;

import java.util.Vector;

public class ConverterUtil {
	
	private ConverterUtil() {
		super();
	}
	
	public static Vector convertIntArrayToVector(int[] intArray) {
		if (intArray == null) {
			return null;
		}
		Vector intList = new Vector();
		int intArrayCount = intArray.length;
		for (int i = 0 ; i < intArrayCount ; i++) {
			intList.addElement(new Integer(intArray[i]));
		}
		
		return intList;
	}
	
	public static int[] convertVectorIntArray(Vector intList) {
		if (intList == null) {
			return null;
		}
		
		int intListCount = intList.size();
		int[] intArray = new int[intListCount];
		for (int i = 0 ; i < intListCount ; i++) {
			intArray[i] = ((Integer)intList.elementAt(i)).intValue();
		}
		
		return intArray;
	}

}
