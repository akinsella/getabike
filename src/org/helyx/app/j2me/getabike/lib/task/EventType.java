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
package org.helyx.app.j2me.getabike.lib.task;

import org.helyx.app.j2me.getabike.lib.task.EventTypeException;

public class EventType {

	public static final int ON_START = 1;
	public static final String ON_START_STR = "ON_START";

	public static final int ON_SUCCESS = 2;
	public static final String ON_SUCCESS_STR = "ON_SUCCESS";

	public static final int ON_PROGRESS = 3;
	public static final String ON_PROGRESS_STR = "ON_PROGRESS";
	
	public static final int ON_ERROR = 4;
	public static final String ON_ERROR_STR = "ON_ERROR";

	public static final int ON_CANCEL = 5;
	public static final String ON_CANCEL_STR = "ON_CANCEL";
	
	public static final int ON_CUSTOM = 6;
	public static final String ON_CUSTOM_STR = "ON_CUSTOM";
	
	
	private EventType() {
		super();
	}
	
	public static String getEventTypeName(int eventType) {
		if (eventType >= ON_CUSTOM) {
			return ON_CUSTOM_STR;
		}
		switch(eventType) {
			case ON_START:
				return ON_START_STR;
			case ON_SUCCESS:
				return ON_SUCCESS_STR;
			case ON_PROGRESS:
				return ON_PROGRESS_STR;
			case ON_ERROR:
				return ON_ERROR_STR;
			case ON_CANCEL:
				return ON_CANCEL_STR;
			default:
				throw new EventTypeException("Type non supporté");
		}
	}
	

}
