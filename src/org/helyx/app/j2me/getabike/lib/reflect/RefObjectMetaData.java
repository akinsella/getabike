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

import java.util.Enumeration;
import java.util.Hashtable;

import org.helyx.app.j2me.getabike.lib.comparator.Comparator;
import org.helyx.app.j2me.getabike.lib.reflect.FieldInfo;
import org.helyx.app.j2me.getabike.lib.reflect.FieldInfoAlreadyExistsExcpetion;
import org.helyx.app.j2me.getabike.lib.sort.FastQuickSort;

public class RefObjectMetaData {
	
	private static final Comparator fieldIndexComparator = new Comparator() {

		public int compare(Object object1, Object object2) {
			if (object1 == null && object2 == null) {
				return 0;
			}
			if (object1 == null) {
				return -1;
			}
			if (object2 == null) {
				return 1;
			}

			FieldInfo fieldInfo1 = (FieldInfo)object1;
			FieldInfo fieldInfo2 = (FieldInfo)object2;
			
			if (fieldInfo1.index < fieldInfo2.index) {
				return -1;
			}
			if (fieldInfo1.index > fieldInfo2.index) {
				return 1;
			}
			
			return 0;
		}
		
	};
	
	private static final Comparator fieldNameComparator = new Comparator() {

		public int compare(Object object1, Object object2) {
			if (object1 == null && object2 == null) {
				return 0;
			}
			if (object1 == null) {
				return -1;
			}
			if (object2 == null) {
				return 1;
			}

			return ((FieldInfo)object1).name.compareTo(((FieldInfo)object2).name);
		}
		
	};

	private String objectName;
	
	private Hashtable fieldNameInfoMap;
	private Hashtable fieldIndexInfoMap;
	
	public RefObjectMetaData() {
		super();
		init();
	}

	public RefObjectMetaData(String objectName) {
		super();
		this.objectName = objectName;
		init();
	}
	
	private void init() {
		fieldNameInfoMap = new Hashtable();
		fieldIndexInfoMap = new Hashtable();
	}

	public FieldInfo getFieldInfoByName(String fieldName) {
		FieldInfo fieldInfo = (FieldInfo)fieldNameInfoMap.get(fieldName);
		return fieldInfo;
	}

	public FieldInfo getFieldInfoByIndex(int index) {
		FieldInfo fieldInfo = (FieldInfo)fieldIndexInfoMap.get(new Integer(index));
		return fieldInfo;
	}
	
	public FieldInfo addFieldInfoByValues(int fieldIndex, String fieldName, int fieldType) {
		FieldInfo fieldInfo = new FieldInfo(fieldIndex, fieldName, fieldType);
		addFieldInfo(fieldInfo);
		
		return fieldInfo;
	}
	
	public void addFieldInfo(FieldInfo fieldInfo) {
		if (fieldNameInfoMap.containsKey(fieldInfo.name)) {
			throw new FieldInfoAlreadyExistsExcpetion();
		}
		Integer fieldIndex = new Integer(fieldInfo.index);
		if (fieldIndexInfoMap.containsKey(fieldIndex)) {
			throw new FieldInfoAlreadyExistsExcpetion();
		}
		
		fieldNameInfoMap.put(fieldInfo.name, fieldInfo);
		fieldIndexInfoMap.put(fieldIndex, fieldInfo);
	}
	
	public void removeFieldInfo(FieldInfo fieldInfo) {
		fieldNameInfoMap.remove(fieldInfo);
		fieldIndexInfoMap.remove(fieldInfo);
	}
	
	public void removeAllFieldInfo() {
		fieldNameInfoMap.clear();
		fieldIndexInfoMap.clear();
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public FieldInfo[] getOrderedFieldInfosByName() {
		return getOrderedFieldInfosInternal(fieldNameComparator);
	}

	public FieldInfo[] getOrderedFieldInfosByIndex() {
		return getOrderedFieldInfosInternal(fieldIndexComparator);
	}
	
	private FieldInfo[] getOrderedFieldInfosInternal(Comparator comparator) {
		
		FieldInfo[] fieldInfoArray = new FieldInfo[fieldNameInfoMap.size()];

		Enumeration _enum = fieldNameInfoMap.elements();
		int i = 0;
		while(_enum.hasMoreElements()) {
			FieldInfo fieldInfo = (FieldInfo)_enum.nextElement();
			fieldInfoArray[i] = fieldInfo;
			i++;
		}

		new FastQuickSort(comparator).sort(fieldInfoArray);
		
		return fieldInfoArray;
	}
	
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[RefObjectMetaData] name='" + objectName + "', ");
		sb.append("fields=[" + fieldNameInfoMap + "]");
		
		String result = sb.toString();
		
		return result;
	}
	
}
