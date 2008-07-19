package org.helyx.app.j2me.velocite.task;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.task.AbstractProgressTask;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.ui.util.KeyMap;
import org.helyx.app.j2me.lib.ui.util.KeyMapConfig;
import org.helyx.app.j2me.lib.ui.util.KeyUtil;
import org.helyx.app.j2me.velocite.PrefConstants;

public class SoftKeyConfigurationTask extends AbstractProgressTask {
	
	private static final String CAT = "SOFT_KEY_CONFIGURATION_TASK";
	
	private Canvas canvas;
	
	public SoftKeyConfigurationTask(Canvas canvas) {
		super("D�tection des touches");
		this.canvas = canvas;
	}
	
	public void execute() {
		
			progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Recerche de config. existante...");
			String softKeyDetectionTypeValue = PrefManager.readPrefValue(PrefConstants.SOFT_KEY_DETECTION_TYPE);
			
			if (KeyUtil.SOFT_KEY_DETECTION_PLATFORM.equals(softKeyDetectionTypeValue)) {
				Log.info(CAT, "[SoftKey detection] Attempting to associate softkeys by platform");
				
				KeyMapConfig platformKeyMapConfig = findPlatformKeyMapConfig();
				
				if (platformKeyMapConfig == null) {
					KeyUtil.cleanUpSoftKeyPref();
				}
				else {
					progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Configuration des touches Ok");
					KeyUtil.keyMapConfig = platformKeyMapConfig;
					return ;
				}
			}
			
			String leftSoftKeyStr = PrefManager.readPrefValue(PrefConstants.SOFT_KEY_LEFT);
			String rightSoftKeyStr = PrefManager.readPrefValue(PrefConstants.SOFT_KEY_RIGHT);
			
			if (leftSoftKeyStr != null && rightSoftKeyStr != null) {
				try {
					int leftSoftKey = Integer.parseInt(leftSoftKeyStr);
					int rightSoftKey = Integer.parseInt(rightSoftKeyStr);
					
					KeyUtil.keyMapConfig = new KeyMapConfig("CACHED_VALUES", new KeyMap[] { new KeyMap(leftSoftKey, rightSoftKey) });
					progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Configuration des touches Ok");
					Log.info(CAT, "[SoftKey detection] Associating softkeys from cached values: " + KeyUtil.keyMapConfig);
					return ;
				}
				catch(Throwable t) {
					Log.warn(CAT, t);
				}
			}
			
			int leftSoftKey = 0;
			int rightSoftKey = 0;

			progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Recerche auto. en cours ...");
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
						progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Touche d�tect�e: '" + positiveKeyName + "'");
					}
					if (negativeKeyName != null) {
						progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Touche d�tect�e: '" + positiveKeyName + "'");
					}
					Log.info(CAT, "[SoftKey detection] index=" + i + ", positiveKeyName='" + positiveKeyName + "', negativeKeyName='" + negativeKeyName + "', leftSoftKey=" + leftSoftKey + ", rightSoftKey=" + rightSoftKey);
				}
				
				if (leftSoftKey != 0 && rightSoftKey != 0) {
					KeyUtil.keyMapConfig = new KeyMapConfig("AUTO_DETECTION", new KeyMap[] { new KeyMap(leftSoftKey, rightSoftKey) });
					Log.info(CAT, "[SoftKey detection] Associating softkeys by automatic detection: " + KeyUtil.keyMapConfig);

					progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Configuration des touches Ok");
					KeyUtil.writeSoftKeyPref(KeyUtil.SOFT_KEY_DETECTION_AUTO_DETECTION, leftSoftKey, rightSoftKey);
					return ;
				}	
			}
			
			progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Recherche auto. �chou�e");
			Log.info(CAT, "[SoftKey detection] Automatic detection failed");
			Log.info(CAT, "[SoftKey detection] Attempting to associate softkeys by platform");
			
			progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Recherche par plateforme...");
			KeyMapConfig platformKeyMapConfig = findPlatformKeyMapConfig();
			
			if (platformKeyMapConfig != null) {
				KeyUtil.cleanUpSoftKeyPref();
				PrefManager.writePref(PrefConstants.SOFT_KEY_DETECTION_TYPE, KeyUtil.SOFT_KEY_DETECTION_DEFAULT);
				KeyUtil.keyMapConfig = platformKeyMapConfig;
				progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Configuration des touches Ok");
				return ;
			}
			
			Log.info(CAT, "[SoftKey detection] Softkeys association by platform failed");
			
			KeyUtil.keyMapConfig = KeyUtil.DEFAULT_KEY_MAP;

			progressDispatcher.fireEvent(ProgressEventType.ON_PROGRESS, "Config. par d�faut");
			KeyUtil.writeSoftKeyPref(KeyUtil.SOFT_KEY_DETECTION_DEFAULT, KeyUtil.keyMapConfig.keyMapArray[0].softKeyLeft, KeyUtil.keyMapConfig.keyMapArray[0].softKeyRight);
			Log.info(CAT, "[SoftKey detection] Associating softkeys to defaults: " + KeyUtil.keyMapConfig);
		}

		private static KeyMapConfig findPlatformKeyMapConfig() {
			
			String platformName = System.getProperty(KeyUtil.MICROEDITION_PLATFORM).toUpperCase();

			Log.info(CAT, "[SoftKey detection] Platform name: " + platformName);

			int length = KeyUtil.keyMapArray.length;
			for (int i = 0 ; i < length ; i++) {
				if (platformName.indexOf(KeyUtil.keyMapArray[i].modelKey) >= 0) {
					KeyMapConfig keyMapConfig = KeyUtil.keyMapArray[i];
					Log.info(CAT, "[SoftKey detection] Associating softkeys to platform '" + platformName + "': " + keyMapConfig);
					return keyMapConfig;
				}
			}
			
			return null;
		}

}
