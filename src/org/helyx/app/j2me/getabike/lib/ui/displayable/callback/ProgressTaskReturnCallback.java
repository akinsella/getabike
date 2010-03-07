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
package org.helyx.app.j2me.getabike.lib.ui.displayable.callback;

import org.helyx.app.j2me.getabike.lib.ui.displayable.AbstractDisplayable;
import org.helyx.app.j2me.getabike.lib.task.Event;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.task.EventTypeException;
import org.helyx.app.j2me.getabike.lib.ui.displayable.callback.IReturnCallback;

public class ProgressTaskReturnCallback implements IReturnCallback {

	public ProgressTaskReturnCallback() {
		super();
	}

	public void onReturn(AbstractDisplayable currentDisplayable, Object data) {
		Event event = (Event)data;
		switch(event.getType()) {
			case EventType.ON_SUCCESS:
				onSuccess(currentDisplayable, event.getMessage(), event.getData());
				break;
			case EventType.ON_ERROR:
				onError(currentDisplayable, event.getMessage(), event.getData());
				break;
			case EventType.ON_CANCEL:
				onError(currentDisplayable, event.getMessage(), event.getData());
				break;
			default:
				throw new EventTypeException("Unsupported event type: '" + EventType.getEventTypeName(event.getType()) + "'");
		}
	}

	public void onCancel(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {
		
	}

	public void onError(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {

	}

	public void onSuccess(AbstractDisplayable currentDisplayable, String eventMessage, Object eventData) {
	}

}
