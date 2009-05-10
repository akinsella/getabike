package org.helyx.app.j2me.velocite.task;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.helyx4me.pref.PrefManager;
import org.helyx.helyx4me.task.AbstractProgressTask;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.ui.util.KeyMap;
import org.helyx.helyx4me.ui.util.KeyMapConfig;
import org.helyx.helyx4me.ui.util.KeyUtil;
import org.helyx.logging4me.Logger;


public class SoftKeyConfigurationTask extends AbstractProgressTask {
	
	private static final Logger logger = Logger.getLogger("SOFT_KEY_CONFIGURATION_TASK");
	
	private Canvas canvas;
	
	public SoftKeyConfigurationTask(Canvas canvas) {
		super("Détection des touches");
		this.canvas = canvas;
	}
	
	public Runnable getRunnable() {
		return new Runnable() {

			public void run() {
				try {
					progressDispatcher.fireEvent(EventType.ON_START);
			
					progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Recherche de config. existante...");
					String softKeyDetectionTypeValue = PrefManager.readPrefString(PrefConstants.SOFT_KEY_DETECTION_TYPE);
					
					if (KeyUtil.SOFT_KEY_DETECTION_PLATFORM.equals(softKeyDetectionTypeValue)) {
						logger.info("[SoftKey detection] Attempting to associate softkeys by platform");
						
						KeyMapConfig platformKeyMapConfig = findPlatformKeyMapConfig();
						
						if (platformKeyMapConfig == null) {
							KeyUtil.cleanUpSoftKeyPref();
						}
						else {
							KeyUtil.keyMapConfig = platformKeyMapConfig;
							progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Configuration des touches Ok");
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
							logger.debug("[SoftKey detection] Associating softkeys from cached values: " + KeyUtil.keyMapConfig);
							progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Configuration des touches Ok");
							progressDispatcher.fireEvent(EventType.ON_SUCCESS);
							return ;
						}
						catch(Throwable t) {
							logger.warn(t);
						}
					}
					
					int leftSoftKey = 0;
					int rightSoftKey = 0;

					progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Recherche auto. en cours ...");
					for (int i = 0 ; i <= 256 ; i++) {
						String positiveKeyName = null;
						String negativeKeyName = null;
						try {			
							positiveKeyName = canvas.getKeyName(i);

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
							negativeKeyName = canvas.getKeyName(-i);

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
								progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Touche détectée: '" + positiveKeyName + "'");
							}
							if (negativeKeyName != null) {
								progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Touche détectée: '" + negativeKeyName + "'");
							}
							logger.debug("[SoftKey detection] index=" + i + ", positiveKeyName='" + positiveKeyName + "', negativeKeyName='" + negativeKeyName + "', leftSoftKey=" + leftSoftKey + ", rightSoftKey=" + rightSoftKey);
						}
						
						if (leftSoftKey != 0 && rightSoftKey != 0) {
							KeyUtil.keyMapConfig = new KeyMapConfig("AUTO_DETECTION", new KeyMap[] { new KeyMap(leftSoftKey, rightSoftKey) });
							logger.debug("[SoftKey detection] Associating softkeys by automatic detection: " + KeyUtil.keyMapConfig);

							KeyUtil.writeSoftKeyPref(KeyUtil.SOFT_KEY_DETECTION_AUTO_DETECTION, leftSoftKey, rightSoftKey);
							progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Configuration des touches Ok");
							progressDispatcher.fireEvent(EventType.ON_SUCCESS);
							return ;
						}	
					}
					
					progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Recherche auto. échouée");
					logger.info("[SoftKey detection] Automatic detection failed");
					logger.info("[SoftKey detection] Attempting to associate softkeys by platform");
					
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
					
					logger.info("[SoftKey detection] Softkeys association by platform failed");
					
					KeyUtil.keyMapConfig = KeyUtil.DEFAULT_KEY_MAP;

					KeyUtil.writeSoftKeyPref(KeyUtil.SOFT_KEY_DETECTION_DEFAULT, KeyUtil.keyMapConfig.keyMapArray[0].softKeyLeft, KeyUtil.keyMapConfig.keyMapArray[0].softKeyRight);
					logger.debug("[SoftKey detection] Associating softkeys to defaults: " + KeyUtil.keyMapConfig);
					progressDispatcher.fireEvent(EventType.ON_PROGRESS, "Config. par défaut");
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

			logger.info("[SoftKey detection] Platform name: " + platformName);

			int length = KeyUtil.keyMapArray.length;
			for (int i = 0 ; i < length ; i++) {
				if (platformName.indexOf(KeyUtil.keyMapArray[i].modelKey) >= 0) {
					KeyMapConfig keyMapConfig = KeyUtil.keyMapArray[i];
					logger.debug("[SoftKey detection] Associating softkeys to platform '" + platformName + "': " + keyMapConfig);
					return keyMapConfig;
				}
			}
			
			return null;
		}

}
