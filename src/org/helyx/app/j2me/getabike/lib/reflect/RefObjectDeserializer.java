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

import java.io.IOException;
import java.util.Date;

import org.helyx.app.j2me.getabike.lib.reflect.FieldInfo;
import org.helyx.app.j2me.getabike.lib.reflect.FieldType;
import org.helyx.app.j2me.getabike.lib.reflect.RefObject;
import org.helyx.app.j2me.getabike.lib.reflect.RefObjectMetaData;
import org.helyx.app.j2me.getabike.lib.reflect.UnsupportedTypeException;
import org.helyx.app.j2me.getabike.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class RefObjectDeserializer extends AbstractObjectSerializer {

	private static final Logger logger = Logger.getLogger("REF_OBJECT_SERIALIZER");
		
	private RefObjectMetaData refObjectMetaData;
	private FieldInfo[] fieldInfoArray;
	private int fieldInfoLength;

	public RefObjectDeserializer(RefObjectMetaData refObjectMetaData) {
		super();
		this.refObjectMetaData = refObjectMetaData;
		init();
	}
	
	private void init() {
		fieldInfoArray = refObjectMetaData.getOrderedFieldInfosByIndex();
		fieldInfoLength = fieldInfoArray.length;
	}

	public byte[] serialize(Object object) throws SerializerException {
		try {
			bos.reset();
			
			RefObject refObject = (RefObject)object;
			dos.writeByte(fieldInfoLength);
			for (int i = 0 ; i < fieldInfoLength ; i++) {
				FieldInfo fieldInfo = fieldInfoArray[i];
				dos.writeByte(fieldInfo.index);
				dos.writeByte(fieldInfo.type);
				switch(fieldInfo.type) {
					case FieldType.BOOLEAN:
						boolean booleanValue = refObject.getBoolean(fieldInfo.name);
						dos.writeBoolean(booleanValue);
						break;
					case FieldType.BYTE:
						byte byteValue = refObject.getByte(fieldInfo.name);
						dos.writeByte(byteValue);
						break;
					case FieldType.SHORT:
						short shortValue = refObject.getShort(fieldInfo.name);
						dos.writeShort(shortValue);
						break;
					case FieldType.INT:
						int intValue = refObject.getInt(fieldInfo.name);
						dos.writeInt(intValue);
						break;
					case FieldType.LONG:
						long longValue = refObject.getLong(fieldInfo.name);
						dos.writeLong(longValue);
						break;
					case FieldType.FLOAT:
						float floatValue = refObject.getFloat(fieldInfo.name);
						dos.writeFloat(floatValue);
						break;
					case FieldType.DOUBLE:
						double doubleValue = refObject.getDouble(fieldInfo.name);
						dos.writeDouble(doubleValue);
						break;
					case FieldType.CHAR:
						char charValue = refObject.getChar(fieldInfo.name);
						dos.writeChar(charValue);
						break;
					case FieldType.STRING:
						String stringValue = refObject.getString(fieldInfo.name);
						dos.writeUTF(stringValue);
						break;
					case FieldType.DATE:
						Date dateValue = refObject.getDate(fieldInfo.name);
						dos.writeLong(dateValue.getTime());
						break;
					default: 
						throw new UnsupportedTypeException("Type non supporté: " + fieldInfo.type);
				}
			}
			
			byte [] bytes = bos.toByteArray();
			
			return bytes;
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}
	}


	public Object deserialize() throws SerializerException {
		try {
			dis.reset();
			RefObject refObject = new RefObject();
			
			byte count = dis.readByte();
			
			for(int i = 0 ; i < count ; i++) {
				
				byte index = dis.readByte();
				byte type = dis.readByte();
				FieldInfo fieldInfo = refObjectMetaData.getFieldInfoByIndex(index);
				
				switch(type) {
					case FieldType.BOOLEAN:
						boolean booleanValue = dis.readBoolean();
						refObject.setBoolean(fieldInfo.name, booleanValue);
						break;
					case FieldType.BYTE:
						byte byteValue = dis.readByte();
						refObject.setByte(fieldInfo.name, byteValue);
						break;
					case FieldType.SHORT:
						short shortValue = dis.readShort();
						refObject.setShort(fieldInfo.name, shortValue);
						break;
					case FieldType.INT:
						int intValue = dis.readInt();
						refObject.setInt(fieldInfo.name, intValue);
						break;
					case FieldType.LONG:
						long longValue = dis.readLong();
						refObject.setLong(fieldInfo.name, longValue);
						break;
					case FieldType.FLOAT:
						float floatValue = dis.readFloat();
						refObject.setFloat(fieldInfo.name, floatValue);
						break;
					case FieldType.DOUBLE:
						double doubleValue = dis.readDouble();
						refObject.setDouble(fieldInfo.name, doubleValue);
						break;
					case FieldType.CHAR:
						char charValue = dis.readChar();
						refObject.setChar(fieldInfo.name, charValue);
						break;
					case FieldType.STRING:
						String stringValue = dis.readUTF();
						refObject.setString(fieldInfo.name, stringValue);
						break;
					case FieldType.DATE:
						Date dateValue = new Date(dis.readLong());
						refObject.setDate(fieldInfo.name, dateValue);
						break;
					default: 
						throw new UnsupportedTypeException("Type non supporté: " + fieldInfo.type);
				}
			}
			
			return refObject;
		}
		catch(IOException ioe) {
			throw new SerializerException(ioe);
		}
	}
	
}