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
import java.io.DataInputStream;
import java.io.IOException;

import org.helyx.logging4me.Logger;


public class MultiRecordDeserializer {

	private static final Logger logger = Logger.getLogger("MULTI_RECORD_DESERIALIZER");
	
	private int recordIterator;
	private int recordIteratorCount;
	
	protected DataInputStream dis;
	protected ByteArrayInputStream bis;
	
	private byte[] byteBuffer;

	public MultiRecordDeserializer(byte[] bytes) throws IOException {
		super();
		init(bytes);
	}
	
	private void init(byte[] bytes) throws IOException {
		bis = new ByteArrayInputStream(bytes);
		dis = new DataInputStream(bis);
		recordIteratorCount = dis.readInt();
		logger.debug("Record iterator count: " + recordIteratorCount);
	}
	
	public void dispose() {
		cleanUpResources();
	}
	
	private void cleanUpResources() {
		if (dis != null) {
			try { dis.close(); } catch (IOException e) { logger.warn(e); }
		}
	}

	public boolean hasMoreElements() {
		return recordIterator < recordIteratorCount;
	}
	
	public int nextElement() throws IOException {
		int recordSize = dis.readInt();
		if (byteBuffer == null || byteBuffer.length < recordSize) {
			byteBuffer = new byte[recordSize];
		}
		dis.read(byteBuffer, 0, recordSize);
		recordIterator++;
		return recordSize;
	}
	
	public int nextElement(byte[] bytes) throws IOException {
		int recordSize = dis.readInt();
		dis.read(bytes, 0, recordSize);
		recordIterator++;

		return recordSize;
	}
	
	public byte[] getByteBuffer() {
		return byteBuffer;
	}

}
