package org.helyx.app.j2me.lib.cache;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class Cache {

	private static final Logger logger = LoggerFactory.getLogger("CACHE");
	
	private Hashtable dataMap;
	
	public Cache() {
		super();
		init();
	}
	
	private void init() {
		dataMap = new Hashtable();
	}
	
	public Object get(String key) {
		return dataMap.get(key);
	}
	
	public void set(String key, String value) {
		dataMap.put(key, value);
	}
	
	public Object remove(String key) {
		return dataMap.remove(key);
	}
	
	public void removeAll() {
		dataMap.clear();
	}
	
	public int size() {
		return dataMap.size();
	}
	
	
}
