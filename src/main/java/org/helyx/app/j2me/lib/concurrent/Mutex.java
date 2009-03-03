package org.helyx.app.j2me.lib.concurrent;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class Mutex {

	private static final Logger logger = LoggerFactory.getLogger("MUTEX");
	
	private boolean islocked = false;
	
	public Mutex() {
		super();
	}

	public synchronized boolean tryLock() {
		if (!islocked) {
//			logger.info("locking: " + this);
			islocked = true;
			return true;
		}
		return false;
	}

	public synchronized void unLock() {
//		logger.info("unlocking: " + this);
		islocked = false;
	}

}
