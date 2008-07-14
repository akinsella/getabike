package org.helyx.app.j2me.lib.pref;

import java.util.Vector;



public class PrefService implements IPrefService {
	
	private static final String CAT = "PREF_SERVICE";
	
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
