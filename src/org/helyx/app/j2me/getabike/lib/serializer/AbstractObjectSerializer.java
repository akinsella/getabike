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
package org.helyx.app.j2me.getabike.lib.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.serializer.IObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public abstract class AbstractObjectSerializer implements IObjectSerializer {
	
	private static final Logger logger = Logger.getLogger("ABSTRACT_OBJECT_SERIALIZER");
	
	private static final int BYTE_BUFFER_LENGTH = 1024;
	
	protected byte[] byteArray;
	protected DataInputStream dis;
	protected ByteArrayInputStream bis;
	
	protected ByteArrayOutputStream bos;
	protected DataOutputStream dos;
	
	protected int bufferFileLength = BYTE_BUFFER_LENGTH;

	public AbstractObjectSerializer() {
		super();
		init();
	}
	
	private void init() {
		byteArray = new byte[BYTE_BUFFER_LENGTH];
		bis = new ByteArrayInputStream(byteArray);

		dis = new DataInputStream(bis);

		bos = new ByteArrayOutputStream(BYTE_BUFFER_LENGTH);
		dos = new DataOutputStream(bos);
	}

	public void ensureInputByteBufferLength(int length) {
		int bytesLength = length;
		if(bytesLength > byteArray.length) {
			tryCloseDis();
			byteArray = new byte[bytesLength];
			bis = new ByteArrayInputStream(byteArray);
			dis = new DataInputStream(bis);
		}
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public byte[] getByteArray(int length) {
		ensureInputByteBufferLength(length);
		return byteArray;
	}
	
	public void dispose() {
		tryCloseDos();
		tryCloseDis();
	}
	
	private void tryCloseDos() {
		if (dos != null) { 
			try { dos.close();  } catch (IOException e) { logger.warn(e); }
			bos = null;
			dos = null;
		}
	}
	
	private void tryCloseDis() {
		if (dis != null) {
			try { dis.close(); } catch (IOException e) { logger.warn(e); }
			bis = null;
			dis = null;
		}
	}

	public boolean matches(RecordFilter recordFilter) throws SerializerException {
		try {
			return recordFilter.matches(dis);
		}
		catch (IOException ioe) {
			throw new SerializerException(ioe);
		}
	}

}
