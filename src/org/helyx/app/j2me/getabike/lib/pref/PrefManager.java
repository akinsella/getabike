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
package org.helyx.app.j2me.getabike.lib.pref;

import java.util.Vector;

import org.helyx.basics4me.lang.BooleanUtil;
import org.helyx.app.j2me.getabike.lib.pref.IPrefService;
import org.helyx.app.j2me.getabike.lib.pref.Pref;
import org.helyx.app.j2me.getabike.lib.pref.PrefHelper;
import org.helyx.app.j2me.getabike.lib.pref.PrefService;


public class PrefManager {

	private PrefManager() {
		super();
	}
	
	public static boolean containsPref(String key) {
		return readPref(key) != null;
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
	public static void writePrefBoolean(String key, boolean booleanValue) {
		writePref(key, booleanValue ? BooleanUtil.TRUE : BooleanUtil.FALSE);
	}

	public static boolean readPrefBoolean(String key) {
		return PrefHelper.readPrefBoolean(readPref(key));
	}
	
	public static boolean readPrefBoolean(String key, boolean defaultValue) {
		return PrefHelper.readPrefBoolean(readPref(key), defaultValue);
	}

	public static String readPrefString(String prefName, String defaultValue) {
		return PrefHelper.readPrefString(readPref(prefName), defaultValue);
	}
	
	public static String readPrefString(String prefName) {
		return PrefHelper.readPrefString(readPref(prefName));
	}


}
