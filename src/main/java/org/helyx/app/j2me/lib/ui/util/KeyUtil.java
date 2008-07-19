package org.helyx.app.j2me.lib.ui.util;

import javax.microedition.lcdui.Canvas;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.Pref;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.velocite.PrefConstants;


public class KeyUtil {

	private static final String CAT = "KEY_UTIL";
	
	public static final String SOFT_KEY_DETECTION_PLATFORM = "PLATFORM";
	public static final String SOFT_KEY_DETECTION_AUTO_DETECTION = "AUTO_DETECTION";
	public static final String SOFT_KEY_DETECTION_DEFAULT = "DEFAULT";
	
	public static final int SOFT_1_DEFAULT = -6;
	public static final int SOFT_2_DEFAULT = -7;
	
	public static final String MICROEDITION_PLATFORM = "microedition.platform";
	
	public static final KeyMapConfig DEFAULT_KEY_MAP = new KeyMapConfig("AUTO", new KeyMap[] { new KeyMap(SOFT_1_DEFAULT, SOFT_2_DEFAULT) });
	
	public static final String SOFT = "SOFT";
	public static final String ONE = "1";
	public static final String TWO = "2";
	
	public static KeyMapConfig keyMapConfig;
		
	public static KeyMapConfig[] keyMapArray = new KeyMapConfig[] {
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
		
	public static void cleanUpSoftKeyPref() {
		PrefManager.removePref(PrefConstants.SOFT_KEY_DETECTION_TYPE);
		PrefManager.removePref(PrefConstants.SOFT_KEY_LEFT);
		PrefManager.removePref(PrefConstants.SOFT_KEY_RIGHT);
	}
	
	public static void writeSoftKeyPref(String softKeyDetectionType, int softKeyLeft, int softKeyRight) {
		Log.info(CAT, "Writing softkey infos: ");
		
		Log.info(CAT, "SOFT_KEY_DETECTION_TYPE: " + softKeyDetectionType);
		Log.info(CAT, "SOFT_KEY_LEFT: " + softKeyLeft);
		Log.info(CAT, "SOFT_KEY_RIGHT: " + softKeyRight);
		PrefManager.writePref(PrefConstants.SOFT_KEY_DETECTION_TYPE, softKeyDetectionType);
		PrefManager.writePref(PrefConstants.SOFT_KEY_LEFT, String.valueOf(softKeyLeft));
		PrefManager.writePref(PrefConstants.SOFT_KEY_RIGHT, String.valueOf(softKeyRight));
	}
	
}
