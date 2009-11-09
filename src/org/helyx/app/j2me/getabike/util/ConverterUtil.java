package org.helyx.app.j2me.getabike.util;

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
