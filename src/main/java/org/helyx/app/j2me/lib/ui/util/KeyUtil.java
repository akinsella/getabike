package org.helyx.app.j2me.lib.ui.util;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.Pref;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.velocite.PrefConstants;


public class KeyUtil {

	private static final String CAT = "KEY_UTIL";
	
	private static final String SOFT_KEY_DETECTION_PLATFORM = "PLATFORM";
	private static final String SOFT_KEY_DETECTION_AUTO_DETECTION = "AUTO_DETECTION";
	private static final String SOFT_KEY_DETECTION_DEFAULT = "DEFAULT";
	
	private static final int SOFT_1_DEFAULT = -6;
	private static final int SOFT_2_DEFAULT = -7;
	
	private static final String MICROEDITION_PLATFORM = "microedition.platform";
	
	private static final KeyMapConfig DEFAULT_KEY_MAP = new KeyMapConfig("AUTO", new KeyMap[] { new KeyMap(SOFT_1_DEFAULT, SOFT_2_DEFAULT) });
	
	private static KeyMapConfig keyMapConfig;
		
	private static KeyMapConfig[] keyMapArray = new KeyMapConfig[] {
		new KeyMapConfig("LG", new KeyMap[] { new KeyMap(-202, -203) }),
		new KeyMapConfig("SAGEM", new KeyMap[] { new KeyMap(-7, -6) }),
		new KeyMapConfig("SIEMENS", new KeyMap[] { new KeyMap(-1, -4), new KeyMap(105, 106) }),
		new KeyMapConfig("PANASONIC", new KeyMap[] { new KeyMap(-1, -4) }),
		new KeyMapConfig("SIEMENS", new KeyMap[] { new KeyMap(-21, -22) }),
		new KeyMapConfig("MOTOROLA", new KeyMap[] { new KeyMap(-10, -11) , new KeyMap(21, 22), new KeyMap(-20, -21) }),
		new KeyMapConfig("SHARP", new KeyMap[] { new KeyMap(-21, -22) }),
		new KeyMapConfig("BLACKBERRY", new KeyMap[] { new KeyMap(113, 111) }),
		new KeyMapConfig("INTENT", new KeyMap[] { new KeyMap(57345, 57346) }),
		new KeyMapConfig("HTC", new KeyMap[] { new KeyMap(57345, 57346) }),
		new KeyMapConfig("QTEK", new KeyMap[] { new KeyMap(57345, 57346) }),
		new KeyMapConfig("SPV", new KeyMap[] { new KeyMap(57345, 57346) }),
		new KeyMapConfig("IDEN", new KeyMap[] { new KeyMap(-20, -21) })
	};

	
	private KeyUtil() {
		super();
	}
	
	public static boolean isLeftSoftKey(int keyCode) {
		if (keyMapConfig == null || keyMapConfig.keyMapArray == null) {
			return false;
		}
		int length = keyMapConfig.keyMapArray.length;
		for (int i = 0 ; i < length ; i++) {
			if (keyCode == keyMapConfig.keyMapArray[i].softKeyLeft) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isRightSoftKey(int keyCode) {
		if (keyMapConfig == null || keyMapConfig.keyMapArray == null) {
			return false;
		}
		int length = keyMapConfig.keyMapArray.length;
		for (int i = 0 ; i < length ; i++) {
			if (keyCode == keyMapConfig.keyMapArray[i].softKeyRight) {
				return true;
			}
		}
		return false;
	}
	
	private static final String SOFT = "SOFT";
	private static final String ONE = "1";
	private static final String TWO = "2";
	
	public synchronized static void initKeyMapConfiguration(Canvas canvas) {

		if (keyMapConfig != null) {
			return;
		}
	
		Pref softKeyDetectionType = PrefManager.readPref(PrefConstants.SOFT_KEY_DETECTION_TYPE);
		
		if (softKeyDetectionType != null && SOFT_KEY_DETECTION_PLATFORM.equals(softKeyDetectionType.value)) {
			Log.info(CAT, "[SoftKey detection] Attempting to associate softkeys by platform");
			
			KeyMapConfig platformKeyMapConfig = findPlatformKeyMapConfig();
			
			if (platformKeyMapConfig == null) {
				cleanUpSoftKeyPref();
			}
			else {
				keyMapConfig = platformKeyMapConfig;
				return ;
			}
		}
		
		Pref leftSoftKeyPref = PrefManager.readPref(PrefConstants.SOFT_KEY_LEFT);
		Pref rightSoftKeyPref = PrefManager.readPref(PrefConstants.SOFT_KEY_RIGHT);
		
		if (leftSoftKeyPref != null && rightSoftKeyPref != null) {
			try {
				String leftSoftKeyStr = leftSoftKeyPref.value;
				int leftSoftKey = Integer.parseInt(leftSoftKeyStr);
				String rightSoftKeyStr = rightSoftKeyPref.value;
				int rightSoftKey = Integer.parseInt(rightSoftKeyStr);
				
				keyMapConfig = new KeyMapConfig("CACHED_VALUES", new KeyMap[] { new KeyMap(leftSoftKey, rightSoftKey) });
				Log.info(CAT, "[SoftKey detection] Associating softkeys from cached values: " + keyMapConfig);
				return ;
			}
			catch(Throwable t) {
				Log.warn(CAT, t);
			}
		}
		
		int leftSoftKey = 0;
		int rightSoftKey = 0;

		for (int i = 0 ; i <= 65536 ; i++) {
			String positiveKeyName = null;
			String negativeKeyName = null;
			try {			
				positiveKeyName = canvas.getKeyName(i);

				if (positiveKeyName != null && positiveKeyName.toUpperCase().indexOf(SOFT) >= 0) {
					if (positiveKeyName.indexOf(ONE) >= 0) {
						leftSoftKey = i;
					}
					else if (positiveKeyName.indexOf(TWO) >= 0) {
						rightSoftKey = i;
					}
				}	
			}
			catch(Throwable t) { }

			try {
				negativeKeyName = canvas.getKeyName(-i);

				if (negativeKeyName != null && negativeKeyName.toUpperCase().indexOf(SOFT) >= 0) {
					if (negativeKeyName.indexOf(ONE) >= 0) {
						leftSoftKey = -i;
					}
					else if (negativeKeyName.indexOf(TWO) >= 0) {
						rightSoftKey = -i;
					}
				}
			}
			catch(Throwable t) { }
			
			if (positiveKeyName != null || negativeKeyName != null) {
				Log.info(CAT, "[SoftKey detection] index=" + i + ", positiveKeyName='" + positiveKeyName + "', negativeKeyName='" + negativeKeyName + "', leftSoftKey=" + leftSoftKey + ", rightSoftKey=" + rightSoftKey);
			}
			
			if (leftSoftKey != 0 && rightSoftKey != 0) {
				keyMapConfig = new KeyMapConfig("AUTO_DETECTION", new KeyMap[] { new KeyMap(leftSoftKey, rightSoftKey) });
				Log.info(CAT, "[SoftKey detection] Associating softkeys by automatic detection: " + keyMapConfig);

				writeSoftKeyPref(SOFT_KEY_DETECTION_AUTO_DETECTION, leftSoftKey, rightSoftKey);
				return ;
			}	
		}
		
		Log.info(CAT, "[SoftKey detection] Automatic detection failed");
		Log.info(CAT, "[SoftKey detection] Attempting to associate softkeys by platform");
		
		KeyMapConfig platformKeyMapConfig = findPlatformKeyMapConfig();
		
		if (platformKeyMapConfig != null) {
			cleanUpSoftKeyPref();
			PrefManager.writePref(PrefConstants.SOFT_KEY_DETECTION_TYPE, SOFT_KEY_DETECTION_DEFAULT);
			keyMapConfig = platformKeyMapConfig;
			return ;
		}
		
		Log.info(CAT, "[SoftKey detection]  Softkeys association by platform failed");
		
		keyMapConfig = DEFAULT_KEY_MAP;

		writeSoftKeyPref(SOFT_KEY_DETECTION_DEFAULT, keyMapConfig.keyMapArray[0].softKeyLeft, keyMapConfig.keyMapArray[0].softKeyRight);
		Log.info(CAT, "[SoftKey detection] Associating softkeys to defaults: " + keyMapConfig);
	}
		
	private static void cleanUpSoftKeyPref() {
		PrefManager.removePref(PrefConstants.SOFT_KEY_DETECTION_TYPE);
		PrefManager.removePref(PrefConstants.SOFT_KEY_LEFT);
		PrefManager.removePref(PrefConstants.SOFT_KEY_RIGHT);
	}
	
	private static void writeSoftKeyPref(String softKeyDetectionType, int softKeyLeft, int softKeyRight) {
		Log.info(CAT, "Writing softkey infos: ");
		
		Log.info(CAT, "SOFT_KEY_DETECTION_TYPE: " + softKeyDetectionType);
		Log.info(CAT, "SOFT_KEY_LEFT: " + softKeyLeft);
		Log.info(CAT, "SOFT_KEY_RIGHT: " + softKeyRight);
		PrefManager.writePref(PrefConstants.SOFT_KEY_DETECTION_TYPE, softKeyDetectionType);
		PrefManager.writePref(PrefConstants.SOFT_KEY_LEFT, String.valueOf(softKeyLeft));
		PrefManager.writePref(PrefConstants.SOFT_KEY_RIGHT, String.valueOf(softKeyRight));
	}

	private static KeyMapConfig findPlatformKeyMapConfig() {
		
		String platformName = System.getProperty(MICROEDITION_PLATFORM).toUpperCase();

		Log.info(CAT, "[SoftKey detection] Platform name: " + platformName);

		int length = keyMapArray.length;
		for (int i = 0 ; i < length ; i++) {
			if (platformName.indexOf(keyMapArray[i].modelKey) >= 0) {
				KeyMapConfig keyMapConfig = keyMapArray[i];
				Log.info(CAT, "[SoftKey detection] Associating softkeys to platform '" + platformName + "': " + keyMapConfig);
				return keyMapConfig;
			}
		}
		
		return null;

	}
	
}
