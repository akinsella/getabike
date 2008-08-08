package org.helyx.app.j2me.lib.cache;

import java.util.Hashtable;

public class Cache {

	private static final String CAT = "CACHE";
	
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
