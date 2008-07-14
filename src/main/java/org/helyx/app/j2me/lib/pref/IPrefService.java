package org.helyx.app.j2me.lib.pref;

import java.util.Vector;



public interface IPrefService {

	void writePref(Pref pref);
	Pref readPref(String key);
	void removePref(String key);
	void removeAllPrefs();
	Vector findAllPref();
	int countPrefs();
	void dispose();
	
}
