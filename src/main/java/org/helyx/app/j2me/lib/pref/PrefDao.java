package org.helyx.app.j2me.lib.pref;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.rms.DefaultRecordEnumeration;
import org.helyx.app.j2me.lib.rms.IRecordReader;
import org.helyx.app.j2me.lib.rms.RecordReader;
import org.helyx.app.j2me.lib.rms.exception.BasicRecordDaoException;
import org.helyx.app.j2me.lib.serializer.SerializerException;

public class PrefDao implements IPrefDao {
	
	private static final Logger logger = LoggerFactory.getLogger("PREF_DAO");

	public static final String PREF_RECORD_STORE = "pref";
	
	private RecordStore recordStore;
	private PrefSerializer prefSerializer;
	private IRecordReader recordReader;
	
	public PrefDao() {
		super();
		try {
			recordStore = RecordStore.openRecordStore(PREF_RECORD_STORE, true);
			init();
		}
		catch (RecordStoreFullException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreNotFoundException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	private void init() {
		prefSerializer = new PrefSerializer();
		recordReader = new RecordReader(recordStore, prefSerializer);
	}

	public void dispose() {
		try {
			if (recordReader != null) {
				recordReader.dispose();
			}
			if (recordStore != null) {
				recordStore.closeRecordStore();
			}
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}
	
	public DefaultRecordEnumeration createPrefEnumeration(RecordFilter recordFilter, RecordComparator recordComparator) {
		
		
		RecordEnumeration recordEnumeration = null;
		try {
			recordEnumeration = recordStore.enumerateRecords(recordFilter, recordComparator, false);
			DefaultRecordEnumeration defaultRecordEnumeration = new DefaultRecordEnumeration(recordStore, recordEnumeration, new PrefSerializer());
			return defaultRecordEnumeration;
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	public void destroyPrefEnumeration(DefaultRecordEnumeration prefEnumeration) {
		prefEnumeration.destroy();
	}

	public int countPrefs() {
		try {
			return recordStore.getNumRecords();
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	public Vector findAllPref() {
		
		RecordEnumeration recordEnumeration = null;
		try {
			try {
				recordEnumeration = recordStore.enumerateRecords(null, null, false);
				
				Vector prefList = new Vector();
				
				while (recordEnumeration.hasNextElement()) {
					int recordId = recordEnumeration.nextRecordId();
					Pref pref = (Pref)recordReader.readRecord(recordId);
					prefList.addElement(pref);
				}
				
				return prefList;
			}
			finally {
				if (recordEnumeration != null) {
					recordEnumeration.destroy();
				}
			}
		}
		catch (SerializerException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (IOException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (InvalidRecordIDException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}

	}

	public Pref readPref(String key) {
		RecordEnumeration recordEnumeration = null;
		
		try {
			try {
				logger.debug("Reading prefs");

				recordEnumeration = recordStore.enumerateRecords(null, null, false);
				while (recordEnumeration.hasNextElement()) {
					int recordId = recordEnumeration.nextRecordId();
					Pref pref = (Pref)recordReader.readRecord(recordId);
					logger.debug("Readed pref: " + pref);
					if (key.equals(pref.key)) {
						return pref;
					}
				}
				return null;
			}
			finally {
				if (recordEnumeration != null) {
					recordEnumeration.destroy();
				}
			}
		} 
		catch (SerializerException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (IOException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (InvalidRecordIDException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	public void removeAllPref() {
		logger.debug("Removing all prefs");
		RecordEnumeration recordEnumeration = null;
		
		try {
			try {
				recordEnumeration = recordStore.enumerateRecords(null, null, false);
				while (recordEnumeration.hasNextElement()) {
					recordStore.deleteRecord(recordEnumeration.nextRecordId());
				}
			}
			finally {
				if (recordEnumeration != null) {
					recordEnumeration.destroy();
				}
			}
		} 
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (InvalidRecordIDException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	public void removePref(String key) {
		logger.debug("Removing pref with key: '" + key + "'");
		RecordEnumeration recordEnumeration = null;
		
		try {
			try {
				recordEnumeration = recordStore.enumerateRecords(null, null, false);
				while (recordEnumeration.hasNextElement()) {
					int recordId = recordEnumeration.nextRecordId();
					Pref pref = (Pref)recordReader.readRecord(recordId);
					if (key.equals(pref.key)) {
						recordStore.deleteRecord(recordId);
						return;
					}
				}
			}
			finally {
				if (recordEnumeration != null) {
					recordEnumeration.destroy();
				}
			}
		} 
		catch (SerializerException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (IOException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (InvalidRecordIDException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	public void writePref(Pref pref) {
		logger.debug("Writing pref : " + pref);
		RecordEnumeration recordEnumeration = null;
		
		try {
			try {
				recordEnumeration = recordStore.enumerateRecords(null, null, false);
				while (recordEnumeration.hasNextElement()) {
					int recordId = recordEnumeration.nextRecordId();
					Pref prefItem = (Pref)recordReader.readRecord(recordId);
					if (pref.key.equals(prefItem.key)) {
						recordStore.deleteRecord(recordId);
						break;
					}
				}
				byte[] bytes = prefSerializer.serialize(pref);
				
				recordStore.addRecord(bytes, 0, bytes.length);
			}
			finally {
				if (recordEnumeration != null) {
					recordEnumeration.destroy();
				}
			}
		} 
		catch (SerializerException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (IOException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (InvalidRecordIDException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}

	}

}


