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

import org.helyx.app.j2me.getabike.lib.pref.IPrefDao;
import org.helyx.app.j2me.getabike.lib.pref.IPrefService;
import org.helyx.app.j2me.getabike.lib.pref.Pref;
import org.helyx.app.j2me.getabike.lib.pref.PrefDao;
import org.helyx.logging4me.Logger;




public class PrefService implements IPrefService {
	
	private static final Logger logger = Logger.getLogger("PREF_SERVICE");
	
	private IPrefDao prefDao;
	
	public PrefService() {
		super();
	}
	
	public void dispose() {
		if (prefDao != null) {
			prefDao.dispose();
		}
	}
	
	private IPrefDao getPrefDao() {
		if (prefDao == null) {
			prefDao = new PrefDao();
		}
		
		return prefDao;
	}

	public Vector findAllPref() {
		return getPrefDao().findAllPref();
	}

	public Pref readPref(String key) {
		return getPrefDao().readPref(key);
	}

	public void removeAllPrefs() {
		getPrefDao().removeAllPref();
	}

	public void removePref(String key) {
		getPrefDao().removePref(key);
	}

	public void writePref(Pref pref) {
		getPrefDao().writePref(pref);
	}
	
	public int countPrefs() {
		int count = getPrefDao().countPrefs();
		
		return count;
	}


}
