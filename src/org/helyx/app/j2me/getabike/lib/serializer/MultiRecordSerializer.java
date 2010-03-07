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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.helyx.logging4me.Logger;


public class MultiRecordSerializer {

	private static final Logger logger = Logger.getLogger("MULTI_RECORD_SERIALIZER");
	
	private int byteBufferLength;
	
	protected ByteArrayOutputStream bos;
	protected DataOutputStream dos;
	private int recordCount;

	public MultiRecordSerializer() {
		super();
		init();
	}
	
	private void init() {
		
	}

	public void dispose() {
		if (dos != null) { 
			try { dos.close();  } catch (IOException e) { logger.warn(e); }
		}
	}
	
	public void initData(int recordCount, int byteBufferLength) throws IOException {

		this.recordCount = recordCount;
		this.byteBufferLength = byteBufferLength;
		if (bos == null) {
			bos = new ByteArrayOutputStream(byteBufferLength);
			dos = new DataOutputStream(bos);
		}
		else {
			bos.reset();
		}
		dos.writeInt(recordCount);
	}
	
	public byte[] getOutBytes() {
		return bos.toByteArray();	
	}
	
	public void addRecord(byte[] bytes) throws IOException {
		int length = bytes.length;
		dos.writeInt(length);
		dos.write(bytes);
	}

}
