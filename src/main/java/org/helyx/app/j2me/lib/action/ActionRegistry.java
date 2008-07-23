package org.helyx.app.j2me.lib.action;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;

public class ActionRegistry {

	private static final String CAT = "ACTIVE_REGISTRY";
	
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
		Log.debug(CAT, "Associating key: '" + key + "' to action: '" + action + "'");
		actionMap.put(key, action);
	}
	
	public void remove(String key) {
		Log.debug(CAT, "Removing key association '" + key + "'");
		actionMap.remove(key);
	}
	
	public void removeAll() {
		Log.debug(CAT, "Removing All key associations");
		actionMap.clear();
	}

}
