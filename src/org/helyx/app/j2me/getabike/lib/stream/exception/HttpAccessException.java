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
package org.helyx.app.j2me.getabike.lib.stream.exception;

import org.helyx.app.j2me.getabike.lib.exception.NestedRuntimeException;

public class HttpAccessException extends NestedRuntimeException {

	private int returnCode = 0;

	public HttpAccessException(int returnCode) {
		super();
		this.returnCode = returnCode;
	}

	public HttpAccessException(int returnCode, String message, Throwable throwable) {
		super(message, throwable);
		this.returnCode = returnCode;
	}

	public HttpAccessException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public HttpAccessException(int returnCode, String message) {
		super(message);
		this.returnCode = returnCode;
	}

	public HttpAccessException(int returnCode, Throwable throwable) {
		super(throwable);
		this.returnCode = returnCode;
	}
	
	public int getReturnCode() {
		return returnCode;
	}

}
