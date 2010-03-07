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
package org.helyx.app.j2me.getabike.lib.i18n;

import java.util.Enumeration;
import java.util.Hashtable;

import org.helyx.basics4me.util.Properties;
import org.helyx.logging4me.Logger;


public class ResourceBundle {
	
	private static final Logger logger = Logger.getLogger("RESOURCE_BUNDLE");
	
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
