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

import java.io.IOException;
import java.io.InputStream;

import org.helyx.basics4me.io.BufferedInputStream;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.stream.StreamUtil;
import org.helyx.logging4me.Logger;


public class ClasspathInputStreamProvider implements InputStreamProvider {

	private static final Logger logger = Logger.getLogger("CLASSPATH_INPUT_STREAM_PROVIDER");
	
	private String path;
	private InputStream inputStream;
	private boolean isCreated = false;
	private boolean isDisposed = false;
	
	public ClasspathInputStreamProvider(String path) {
		super();
		this.path = path;
	}
	
	public InputStream createInputStream() throws IOException {
		return createInputStream(true);
	}
	
	public InputStream createInputStream(boolean buffered) throws IOException {
	
		inputStream = buffered ? new BufferedInputStream(StreamUtil.createFromClassPath(path)) : StreamUtil.createFromClassPath(path);
		isCreated = true;
		
		return inputStream;
	}	

	public void dispose() throws IOException {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		finally {
			isDisposed = true;
		}
	}

	public boolean isCreated() {
		return isCreated;
	}

	public boolean isDisposed() {
		return isDisposed;
	}

}
