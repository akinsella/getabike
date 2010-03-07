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

import org.helyx.app.j2me.getabike.lib.exception.NestedException;
import org.helyx.app.j2me.getabike.lib.exception.NestedRuntimeException;

public class ErrorUtil {

	private ErrorUtil() {
		super();
	}
	
	public static String getMessage(Object object) {
		
		if (object == null) {
			return "null";
		}
		if (object instanceof Throwable) {
			Throwable throwable = (Throwable)object;
			return throwable.getMessage() == null ? throwable.toString() : throwable.getMessage();
		}
		
		return object.toString();
	}
	
	public static Throwable getRootCause(Throwable t) {
		if (t instanceof NestedRuntimeException) {
			NestedRuntimeException nre = (NestedRuntimeException)t;
			Throwable cause = nre.getCause();
			return cause == null ? t : getRootCause(cause);
		}
		else if (t instanceof NestedException) {
			NestedException ne = (NestedException)t;
			Throwable cause = ne.getCause();
			return cause == null ? t : getRootCause(cause);
		}
		
		return t;
	}
	
}
