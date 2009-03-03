package org.helyx.app.j2me.lib.rms;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.serializer.IObjectSerializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;

public class DefaultRecordEnumeration implements Enumeration {

	public static final Logger logger = LoggerFactory.getLogger("DEFAULT_RECORD_ENUMERATION");
	
	private RecordEnumeration recordEnumeration;
	private IRecordReader recordReader;
	private boolean isFiltered;
	private Object nextRecord;
	
	public DefaultRecordEnumeration(RecordStore recordStore,RecordEnumeration recordEnumeration, IObjectSerializer recordSerializer) {
		this(recordStore, recordEnumeration, null, recordSerializer);
	}

	public DefaultRecordEnumeration(RecordStore recordStore, RecordEnumeration recordEnumeration, IRecordFilter recordFilter, IObjectSerializer recordSerializer) {
		super();
		this.recordEnumeration = recordEnumeration;
		this.isFiltered = recordFilter!= null;
		recordReader = new RecordReader(recordStore, recordSerializer, recordFilter);
	}

	public boolean hasMoreElements() {
		if (!isFiltered) {
			return recordEnumeration.hasNextElement();
		}
		try {
	
			while (recordEnumeration.hasNextElement()) {
				int recordId = recordEnumeration.nextRecordId();
	
				recordReader.fetchNextRecord(recordId);
				if (recordReader.filterMatchesPrefetchedRecord()) {
					nextRecord = recordReader.readPrefetchedRecord();
					return true;
				}
			}
			return false;
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


	public Object nextElement() {
		try {
			if (!isFiltered) {
				int recordId = recordEnumeration.nextRecordId();
				Object object = recordReader.readRecord(recordId);
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
		recordEnumeration.destroy();
		recordReader.dispose();
	}

}
