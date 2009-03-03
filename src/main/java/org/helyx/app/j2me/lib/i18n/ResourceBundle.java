package org.helyx.app.j2me.lib.i18n;

import java.util.Enumeration;
import java.util.Hashtable;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.basics4me.util.Properties;

public class ResourceBundle {
	
	private static final Logger logger = LoggerFactory.getLogger("RESOURCE_BUNDLE");
	
	private static final String TOKEN = "##";

	protected Hashtable messageMap;
	
	public ResourceBundle() {
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

	public boolean containsKey(String key) {
		return messageMap.containsKey(key);
	}

	public String get(String key) {
		String messageKey = (String)messageMap.get(key);
		if (messageKey == null) {
			String fallbackMessage = new StringBuffer(TOKEN).append(key).append(TOKEN).toString();
			return fallbackMessage;
		}
		return (String)messageMap.get(key);
	}

	public int size() {
		return messageMap.size();
	}
	
}
