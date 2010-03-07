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

import java.util.Vector;

import org.helyx.app.j2me.getabike.lib.task.IProgressDispatcher;
import org.helyx.app.j2me.getabike.lib.task.ProgressListener;
import org.helyx.logging4me.Logger;


public class ProgressDispatcher implements IProgressDispatcher {
	
	private static final Logger logger = Logger.getLogger("PROGRESS_DISPATCHER");
	
	private String name;
	
	private Vector progressListenerList = new Vector();
	
	public ProgressDispatcher() {
		super();
	}
	
	private String getLoggerName() {
		if (name == null) {
			return logger.getCategory().getName();
		}
		return logger.getCategory() + "[" + name + "]";
	}
	

	public void addProgressListener(ProgressListener progressListener) {
		progressListenerList.addElement(progressListener);
	}

	public void removeProgressListener(ProgressListener progressListener) {
		progressListenerList.removeElement(progressListener);
	}
	
	public void fireEvent(int eventType) {
		fireEvent(eventType, null, null);
	}

	public void fireEvent(int eventType, String eventMessage) {
		fireEvent(eventType, eventMessage, null);
	}

	public void fireEvent(int eventType, Object eventData) {
		fireEvent(eventType, null, eventData);
	}
		
	public void fireEvent(int eventType, String eventMessage, Object eventData) {
		
		logger.debug(getLoggerName(), "[" + name + "] eventType=" + eventType + ", eventMessage=" + eventMessage + ", eventData=" + eventData);
		int size = progressListenerList.size();
		
		for (int i = 0 ; i < size ; i++) {
			try {
				ProgressListener progressListener = (ProgressListener)progressListenerList.elementAt(i);
				progressListener.onEvent(eventType, eventMessage, eventData);
			}
			catch(Throwable t) {
				logger.warn(t);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
