package org.helyx.app.j2me.lib.action;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class ActionRegistry {

	private static final Log log = LogFactory.getLog("ACTIVE_REGISTRY");
	
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
		log.debug("Associating key: '" + key + "' to action: '" + action + "'");
		actionMap.put(key, action);
	}
	
	public void remove(String key) {
		log.debug("Removing key association '" + key + "'");
		actionMap.remove(key);
	}
	
	public void removeAll() {
		log.debug("Removing All key associations");
		actionMap.clear();
	}

}
