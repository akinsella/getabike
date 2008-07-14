package org.helyx.app.j2me.lib.rms;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.serializer.IObjectSerializer;
import org.helyx.app.j2me.lib.serializer.MultiRecordDeserializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;

public class MultiRecordEnumeration implements Enumeration {

	public static final String CAT = "MULTI_RECORD_ENUMERATION";
	
	private MultiRecordReader recordReader;
	private boolean isFiltered;
	private Object nextRecord;
	private MultiRecordDeserializer multiRecordDeserializer;
	private boolean hasRecords = false;
	
	public MultiRecordEnumeration(RecordStore recordStore, IObjectSerializer recordSerializer) {
		this(recordStore, null, recordSerializer);
	}

	public MultiRecordEnumeration(RecordStore recordStore, IRecordFilter recordFilter, IObjectSerializer recordSerializer) {
		super();
		try {
			this.isFiltered = recordFilter != null;
			if (recordStore.getNumRecords() <= 0) {
				hasRecords = false;
				return;
			}
			hasRecords = true;
			byte[] bytes = recordStore.getRecord(1);
			Log.info(CAT, "First Record bytes length: " + bytes.length);
			multiRecordDeserializer = new MultiRecordDeserializer(bytes);
			recordReader = new MultiRecordReader(multiRecordDeserializer, recordSerializer, recordFilter);
		}
		catch (RecordStoreNotOpenException e) {
			throw new DaoException(e);
		} 
		catch (IOException e) {
			throw new DaoException(e);
		}
		catch (InvalidRecordIDException e) {
			throw new DaoException(e);
		}
		catch (RecordStoreException e) {
			throw new DaoException(e);
		}
	}

	public boolean hasMoreElements() {
		try {
			if (!hasRecords) {
				return false;
			}
			if (!isFiltered) {
				return multiRecordDeserializer.hasMoreElements();
			}
	
			while (multiRecordDeserializer.hasMoreElements()) {
	
				recordReader.fetchNextRecord();
				if (recordReader.filterMatchesPrefetchedRecord()) {
					nextRecord = recordReader.readPrefetchedRecord();
					return true;
				}
			}
			return false;
		}
		catch (IOException e) {
			Log.warn(CAT, e);
			throw new DaoException(e);
		}
		catch (SerializerException e) {
			Log.warn(CAT, e);
			throw new DaoException(e);
		}
		catch (InvalidRecordIDException e) {
			Log.warn(CAT, e);
			throw new DaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			Log.warn(CAT, e);
			throw new DaoException(e);
		}
		catch (RecordStoreException e) {
			Log.warn(CAT, e);
			throw new DaoException(e);
		}
	}


	public Object nextElement() {
		try {
			if (!isFiltered) {
				Object object = recordReader.nextRecord();
				return object;
			}
			return nextRecord;
		}
		catch (IOException e) {
			throw new DaoException(e);
		}
		catch (SerializerException e) {
			throw new DaoException(e);
		}
		catch (InvalidRecordIDException e) {
			throw new DaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			throw new DaoException(e);
		}
		catch (RecordStoreException e) {
			throw new DaoException(e);
		}
	}

	public void destroy() {
		if (recordReader != null) {
			recordReader.dispose();
		}
		if (multiRecordDeserializer != null) {
			multiRecordDeserializer.dispose();
		}
	}

}
