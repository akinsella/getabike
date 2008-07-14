package org.helyx.app.j2me.lib.pref;

import java.util.Vector;

import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordFilter;

import org.helyx.app.j2me.lib.rms.DefaultRecordEnumeration;


public interface IPrefDao {

	void writePref(Pref pref);
	Pref readPref(String key);
	void removePref(String key);
	void removeAllPref();
	Vector findAllPref();
	int countPrefs();
	void dispose();
	DefaultRecordEnumeration createPrefEnumeration(RecordFilter recordFilter, RecordComparator recordComparator);
	void destroyPrefEnumeration(DefaultRecordEnumeration prefEnumeration);
	
}
