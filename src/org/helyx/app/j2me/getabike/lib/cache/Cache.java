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
package org.helyx.app.j2me.getabike.lib.cache;

import java.util.Hashtable;

import org.helyx.logging4me.Logger;


public class Cache {

	private static final Logger logger = Logger.getLogger("CACHE");
	
	private Hashtable dataMap;
	
	public Cache() {
		super();
		init();
	}
	
	private void init() {
		dataMap = new Hashtable();
	}
	
	public boolean containsKey(Object key) {
		return dataMap.containsKey(key);
	}
	
	public Object get(Object key) {
		return dataMap.get(key);
	}
	
	public void set(Object key, Object value) {
		dataMap.put(key, value);
	}
	
	public Object remove(Object key) {
		return dataMap.remove(key);
	}
	
	public void removeAll() {
		dataMap.clear();
	}
	
	public int size() {
		return dataMap.size();
	}

	public void clear() {
		dataMap.clear();
	}
	
	
}
