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
package org.helyx.app.j2me.getabike.lib.action;

import java.util.Hashtable;

import org.helyx.app.j2me.getabike.lib.action.IAction;
import org.helyx.logging4me.Logger;


public class ActionRegistry {

	private static final Logger logger = Logger.getLogger("ACTIVE_REGISTRY");
	
	private Hashtable actionMap = new Hashtable();
	
	public ActionRegistry() {
		super();
	}
	
	public boolean contains(String key) {
		return actionMap.containsKey(key);
	}
	
	public IAction get(String key) {
		return (IAction)actionMap.get(key);
	}
	
	public void put(String key, IAction action) {
		logger.debug("Associating key: '" + key + "' to action: '" + action + "'");
		actionMap.put(key, action);
	}
	
	public void remove(String key) {
		logger.debug("Removing key association '" + key + "'");
		actionMap.remove(key);
	}
	
	public void removeAll() {
		logger.debug("Removing All key associations");
		actionMap.clear();
	}

}
