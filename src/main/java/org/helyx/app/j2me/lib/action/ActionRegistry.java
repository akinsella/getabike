package org.helyx.app.j2me.lib.action;

import java.util.Hashtable;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class ActionRegistry {

	private static final Logger logger = LoggerFactory.getLogger("ACTIVE_REGISTRY");
	
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
