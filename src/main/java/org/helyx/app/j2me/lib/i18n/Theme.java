package org.helyx.app.j2me.lib.i18n;

import java.util.Enumeration;
import java.util.Hashtable;

import org.helyx.basics4me.util.Properties;

public class Theme {
	
	private static final String CAT = "RESOURCE_BUNDLE";

	private Hashtable messageMap;
	
	public Theme() {
		super();
		init();
	}
	
	private void init() {
		messageMap = new Hashtable();
	}
	
	public void putAll(Properties properties) {
		Enumeration _enum = properties.propertyNames();
		while(_enum.hasMoreElements()) {
			String propertyKey = (String)_enum.nextElement();
			messageMap.put(propertyKey, properties.get(propertyKey));
		}
	}
	
	public void put(String key, String message) {
		messageMap.put(key, message);
	}
	
	public void remove(String key) {
		messageMap.remove(key);
	}
	
	public void clear() {
		messageMap.clear();
	}

	public Enumeration getMessageKeys() {
		return messageMap.keys();
	}

	public String get(String key) {
		return (String)messageMap.get(key);
	}

	public int size() {
		return messageMap.size();
	}
	
}
