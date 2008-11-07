package org.helyx.app.j2me.lib.concurrent;

public class Mutex {

	private boolean islocked = false;
	
	public Mutex() {
		super();
	}

	public synchronized boolean tryLock() {
		if (!islocked) {
			islocked = true;
			return true;
		}
		return false;
	}

	public synchronized void unLock() {
		islocked = false;
	}

}
