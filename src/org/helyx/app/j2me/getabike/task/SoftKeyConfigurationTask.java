package org.helyx.app.j2me.getabike.task;

import org.helyx.app.j2me.getabike.PrefConstants;
import org.helyx.app.j2me.getabike.lib.pref.PrefManager;
import org.helyx.app.j2me.getabike.lib.task.AbstractProgressTask;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.ui.util.KeyMap;
import org.helyx.app.j2me.getabike.lib.ui.util.KeyMapConfig;
import org.helyx.app.j2me.getabike.lib.ui.util.KeyUtil;
import org.helyx.app.j2me.getabike.lib.ui.view.AbstractView;
import org.helyx.logging4me.Logger;


public class SoftKeyConfigurationTask extends AbstractProgressTask {
	
	private static final Logger logger = Logger.getLogger("SOFT_KEY_CONFIGURATION_TASK");
	
	private AbstractView view;
	
	public SoftKeyConfigurationTask(AbstractView view) {
		super(view.getMessage("task.soft.key.title"));
		
		this.view = view;
	}
	
	public Runnable getRunnable() {
		return new Runnable() {

			public void run() {
				try {
					progressDispatcher.fireEvent(EventType.ON_START);
			
					progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.soft.key.search.config"));
					String softKeyDetectionTypeValue = PrefManager.readPrefString(PrefConstants.SOFT_KEY_DETECTION_TYPE);
					
					if (KeyUtil.SOFT_KEY_DETECTION_PLATFORM.equals(softKeyDetectionTypeValue)) {
						if (logger.isInfoEnabled()) {
							logger.info("[SoftKey detection] Attempting to associate softkeys by platform");
						}
						
						KeyMapConfig platformKeyMapConfig = findPlatformKeyMapConfig();
						
						if (platformKeyMapConfig == null) {
							KeyUtil.cleanUpSoftKeyPref();
						}
						else {
							KeyUtil.keyMapConfig = platformKeyMapConfig;
							progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.soft.key.search.config.ok"));
							progressDispatcher.fireEvent(EventType.ON_SUCCESS);
							return ;
						}
					}
					
					String leftSoftKeyStr = PrefManager.readPrefString(PrefConstants.SOFT_KEY_LEFT);
					String rightSoftKeyStr = PrefManager.readPrefString(PrefConstants.SOFT_KEY_RIGHT);
					
					if (leftSoftKeyStr != null && rightSoftKeyStr != null) {
						try {
							int leftSoftKey = Integer.parseInt(leftSoftKeyStr);
							int rightSoftKey = Integer.parseInt(rightSoftKeyStr);
							
							KeyUtil.keyMapConfig = new KeyMapConfig("CACHED_VALUES", new KeyMap[] { new KeyMap(leftSoftKey, rightSoftKey) });
							if (logger.isDebugEnabled()) { 
								logger.debug("[SoftKey detection] Associating softkeys from cached values: " + KeyUtil.keyMapConfig);
							}
							progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.soft.key.search.config.ok"));
							progressDispatcher.fireEvent(EventType.ON_SUCCESS);
							return ;
						}
						catch(Throwable t) {
							logger.warn(t);
						}
					}
					
					int leftSoftKey = 0;
					int rightSoftKey = 0;

					progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.soft.key.search.auto"));
					for (int i = 0 ; i <= 256 ; i++) {
						String positiveKeyName = null;
						String negativeKeyName = null;
						try {			
							positiveKeyName = view.getViewCanvas().getKeyName(i);

							if (positiveKeyName != null && positiveKeyName.toUpperCase().indexOf(KeyUtil.SOFT) >= 0) {
								if (positiveKeyName.indexOf(KeyUtil.ONE) >= 0) {
									leftSoftKey = i;
								}
								else if (positiveKeyName.indexOf(KeyUtil.TWO) >= 0) {
									rightSoftKey = i;
								}
							}	
						}
						catch(Throwable t) { }

						try {
							negativeKeyName = view.getViewCanvas().getKeyName(-i);

							if (negativeKeyName != null && negativeKeyName.toUpperCase().indexOf(KeyUtil.SOFT) >= 0) {
								if (negativeKeyName.indexOf(KeyUtil.ONE) >= 0) {
									leftSoftKey = -i;
								}
								else if (negativeKeyName.indexOf(KeyUtil.TWO) >= 0) {
									rightSoftKey = -i;
								}
							}
						}
						catch(Throwable t) { }
						
						if (positiveKeyName != null || negativeKeyName != null) {
							if (positiveKeyName != null) {
								progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.soft.key.key.detected") + positiveKeyName + "'");
							}
							if (negativeKeyName != null) {
								progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.soft.key.key.detected") + ": '" + negativeKeyName + "'");
							}
							if (logger.isDebugEnabled()) {
								logger.debug("[SoftKey detection] index=" + i + ", positiveKeyName='" + positiveKeyName + "', negativeKeyName='" + negativeKeyName + "', leftSoftKey=" + leftSoftKey + ", rightSoftKey=" + rightSoftKey);
							}
						}
						if (leftSoftKey != 0 && rightSoftKey != 0) {
							KeyUtil.keyMapConfig = new KeyMapConfig("AUTO_DETECTION", new KeyMap[] { new KeyMap(leftSoftKey, rightSoftKey) });
							if (logger.isDebugEnabled()) {
								logger.debug("[SoftKey detection] Associating softkeys by automatic detection: " + KeyUtil.keyMapConfig);
							}

							KeyUtil.writeSoftKeyPref(KeyUtil.SOFT_KEY_DETECTION_AUTO_DETECTION, leftSoftKey, rightSoftKey);
							progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.soft.key.search.config.ok"));
							progressDispatcher.fireEvent(EventType.ON_SUCCESS);
							return ;
						}	
					}
					
					progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.soft.key.search.auto.failed"));
					if (logger.isInfoEnabled()) {
						logger.info("[SoftKey detection] Automatic detection failed");
						logger.info("[SoftKey detection] Attempting to associate softkeys by platform");
					}
					
					progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Recherche par plateforme...");
					KeyMapConfig platformKeyMapConfig = findPlatformKeyMapConfig();
					
					if (platformKeyMapConfig != null) {
						KeyUtil.cleanUpSoftKeyPref();
						PrefManager.writePref(PrefConstants.SOFT_KEY_DETECTION_TYPE, KeyUtil.SOFT_KEY_DETECTION_DEFAULT);
						KeyUtil.keyMapConfig = platformKeyMapConfig;
						progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Configuration des touches Ok");
						progressDispatcher.fireEvent(EventType.ON_SUCCESS);
						return ;
					}
					
					if (logger.isInfoEnabled()) {
						logger.info("[SoftKey detection] Softkeys association by platform failed");
					}
					
					KeyUtil.keyMapConfig = KeyUtil.DEFAULT_KEY_MAP;

					KeyUtil.writeSoftKeyPref(KeyUtil.SOFT_KEY_DETECTION_DEFAULT, KeyUtil.keyMapConfig.keyMapArray[0].softKeyLeft, KeyUtil.keyMapConfig.keyMapArray[0].softKeyRight);
					if (logger.isDebugEnabled()) {
						logger.debug("[SoftKey detection] Associating softkeys to defaults: " + KeyUtil.keyMapConfig);
					}
					progressDispatcher.fireEvent(EventType.ON_PROGRESS, view.getMessage("task.soft.key.config.default"));
					progressDispatcher.fireEvent(EventType.ON_SUCCESS);
				}
				catch(Throwable t) {
					logger.warn(t);
					progressDispatcher.fireEvent(EventType.ON_ERROR, t.getMessage(), t);
				}
			}
			
		};
	
	}


		private static KeyMapConfig findPlatformKeyMapConfig() {
			
			String platformName = System.getProperty(KeyUtil.MICROEDITION_PLATFORM).toUpperCase();

			if (logger.isInfoEnabled()) {
				logger.info("[SoftKey detection] Platform name: " + platformName);
			}

			int length = KeyUtil.keyMapArray.length;
			for (int i = 0 ; i < length ; i++) {
				if (platformName.indexOf(KeyUtil.keyMapArray[i].modelKey) >= 0) {
					KeyMapConfig keyMapConfig = KeyUtil.keyMapArray[i];
					if (logger.isDebugEnabled()) {
						logger.debug("[SoftKey detection] Associating softkeys to platform '" + platformName + "': " + keyMapConfig);
					}
					return keyMapConfig;
				}
			}
			
			return null;
		}

}
