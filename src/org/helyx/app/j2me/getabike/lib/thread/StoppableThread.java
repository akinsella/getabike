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
package org.helyx.app.j2me.getabike.lib.thread;

import org.helyx.app.j2me.getabike.lib.thread.StoppableThread;
import org.helyx.logging4me.Logger;

public class StoppableThread extends Thread {

	private static final Logger logger = Logger.getLogger(StoppableThread.class);
	
	private boolean shouldStop = false;

	public StoppableThread() {
		super();
	}

	public StoppableThread(Runnable target, String name) {
		super(target, name);
	}

	public StoppableThread(Runnable target) {
		super(target);
	}

	public StoppableThread(String name) {
		super(name);
	}
	
	public boolean shouldStop() {
		return shouldStop;
	}
	
	public void askToStop(boolean waitEndOfThread) {
		shouldStop = true;
		if (waitEndOfThread) {
			waitEndOfThread();
		}
	}
	
	public void waitEndOfThread() {
		while(isAlive()) {
			try { Thread.sleep(100); } catch (InterruptedException ie) { logger.warn(ie); }
		}
	}
	
}
