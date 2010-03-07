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

import java.io.IOException;
import java.io.InputStream;

import org.helyx.app.j2me.getabike.lib.content.accessor.ContentAccessorException;
import org.helyx.app.j2me.getabike.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.stream.StreamUtil;
import org.helyx.logging4me.Logger;


public class HttpUtil {

	private static final Logger logger = Logger.getLogger("HTTP_UTIL");
	
	public static byte[] loadAsBytes(String url) throws Exception {
		return loadAsBytes(url, true, true);
	}
	
	public static byte[] loadAsBytes(String url, boolean buffered) throws Exception {
		return loadAsBytes(url, buffered, true);
	}
	
	public static byte[] loadAsBytes(String url, boolean buffered, boolean forceReadBytePerByte) throws IOException, ContentAccessorException {
		InputStream is = null;
		try {
			InputStreamProvider isp = new HttpContentAccessor(url, forceReadBytePerByte).getInputStreamProvider();
			is = isp.createInputStream(true);
			byte[] byteBuffer = StreamUtil.readStreamBinary(is, false);
			
			return byteBuffer;
		}
		finally {
			if (is != null) {
				try { is.close(); } catch(IOException ioe) { logger.warn(ioe); }
			}
		}		
	}

}
