package org.helyx.app.j2me.lib.pref;

import java.util.Vector;


public class PrefManager {

	private PrefManager() {
		super();
	}
	
	public static Pref readPref(String key) {
		IPrefService prefService = null;
		try {
			prefService = new PrefService();
			Pref pref = prefService.readPref(key);
			
			return pref;
		}
		finally {
			prefService.dispose();
		}
	}

	public static void writePref(String key, String value) {
		IPrefService prefService = null;
		try {
			prefService = new PrefService();
			prefService.writePref(new Pref(key, value));
		}
		finally {
			prefService.dispose();
		}
	}

	public static void removePref(String key) {
		IPrefService prefService = null;
		try {
			prefService = new PrefService();
			prefService.removePref(key);
		}
		finally {
			prefService.dispose();
		}
	}

	public static Vector findPrefList() {
		IPrefService prefService = null;
		try {
			prefService = new PrefService();
			Vector prefList = prefService.findAllPref();
			
			return prefList;
		}
		finally {
			prefService.dispose();
		}
	}

	public static void cleanUpSavedData() {
		IPrefService prefService = null;
		try {
			prefService = new PrefService();
			prefService.removeAllPrefs();
		}
		finally {
			prefService.dispose();
		}
	}

	public static String readPrefValue(String prefName) {
		Pref pref = readPref(prefName);
		if (pref == null) {
			return null;
		}
		
		return pref.value;
	}

}
