package org.helyx.app.j2me.lib.concurrent;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class Mutex {

	private static final Log log = LogFactory.getLog("MUTEX");
	
	private boolean islocked = false;
	
	public Mutex() {
		super();
	}

	public synchronized boolean tryLock() {
		if (!islocked) {
			log.info("locking: " + this);
			islocked = true;
			return true;
		}
		return false;
	}

	public synchronized void unLock() {
		log.info("unlocking: " + this);
		islocked = false;
	}

}
