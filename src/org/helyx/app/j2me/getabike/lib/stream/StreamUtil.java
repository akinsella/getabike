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
package org.helyx.app.j2me.getabike.lib.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.helyx.app.j2me.getabike.lib.stream.StreamUtil;
import org.helyx.logging4me.Logger;


public class StreamUtil {
	
	private static final Logger logger = Logger.getLogger("STREAM_UTIL");
	
	private static final byte[] ALPHABET = "abcdefghijklmnopkrstuvwxyz".getBytes();
	
	public static InputStream createFromClassPath(String path) throws IOException {
		return StreamUtil.class.getResourceAsStream(path);
	}

	public static boolean supportEncoding(String encoding) {
		
		try {
			InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(ALPHABET), encoding);
			try {
				isr.read();
				return true;
			}
			finally {
				if (isr != null) {
					isr.close();
				}
			}
		}
		catch(Throwable t) {
			logger.warn(t);
		}
		
		return false;
	}

	public static String readStream(InputStream inputStream, boolean closeStream) throws IOException {
		return new String(readStreamBinary(inputStream, closeStream));
	}

	public static byte[] readStreamBinary(InputStream inputStream, boolean closeStream) throws IOException {
		int length = -1;
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		try {
			while((length = inputStream.read(bytes)) >= 0) {
				if (length == 0) {
					continue;
				}
				baos.write(bytes, 0, length);
			}
			if (closeStream) {
				inputStream.close();
			}
			return baos.toByteArray();
		}
		finally {
			baos.close();
		}
	}
	
}
