package org.helyx.app.j2me.lib.rms;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.rms.exception.MultiRecordDaoException;
import org.helyx.app.j2me.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.lib.serializer.MultiRecordSerializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;

public class MultiRecordDao implements IMultiRecordDao {
	
	private static final String CAT = "MULTI_RECORD_DAO";
	private static final int DEFAULT_RECORD_BUFFER_LENGTH = 1024;
	
	private int recordBufferLength;
	private String recordStoreName;
	private RecordStore recordStore;
	private IRecordReader recordReader;
	private AbstractObjectSerializer recordSerializer;
	
	public MultiRecordDao(String recordStoreName, AbstractObjectSerializer recordSerializer) {
		this(recordStoreName, recordSerializer, DEFAULT_RECORD_BUFFER_LENGTH);
	}
	
	public MultiRecordDao(String recordStoreName, AbstractObjectSerializer recordSerializer, int recordBufferLength) {
		super();
		this.recordStoreName = recordStoreName;
		this.recordBufferLength = recordBufferLength;
		this.recordSerializer = recordSerializer;
		init();
	}

	private void init() {
		try {
			recordStore = RecordStore.openRecordStore(recordStoreName, true);
			recordReader = new RecordReader(recordStore, recordSerializer);
		}
		catch (RecordStoreFullException e) {
			throw new MultiRecordDaoException(e);
		}
		catch (RecordStoreNotFoundException e) {
			throw new MultiRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new MultiRecordDaoException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IMultiRecordDao#dispose()
	 */
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
			throw new MultiRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new MultiRecordDaoException(e);
		}
	}	
	
	
	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IMultiRecordDao#findAllRecords()
	 */
	public Vector findAllRecords() {
		Vector objectList = new Vector();
		MultiRecordEnumeration multiRecordEnumeration = new MultiRecordEnumeration(recordStore, null, recordSerializer);
		try {
			while (multiRecordEnumeration.hasMoreElements()) {
				Object object = multiRecordEnumeration.nextElement();
				objectList.addElement(object);
			}
			
			return objectList;
		}
		finally {
			multiRecordEnumeration.destroy();
		}
	}

	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IMultiRecordDao#removeAllRecords()
	 */
	public void removeAllRecords() {
				
		try {
			recordStore.closeRecordStore();
			RecordStore.deleteRecordStore(recordStoreName);
			recordStore = RecordStore.openRecordStore(recordStoreName, true);
			recordReader.setRecordStore(recordStore);
		} 
		catch (RecordStoreException e) {
			throw new MultiRecordDaoException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IMultiRecordDao#saveRecordArray(java.lang.Object[])
	 */
	public void saveRecordArray(Object[] recordArray) {
		MultiRecordSerializer multiRecordSerializer = null;
		try {
			int recordCount = recordArray.length;
			multiRecordSerializer = new MultiRecordSerializer();
			multiRecordSerializer.initData(recordCount, recordBufferLength);
			for (int i = 0  ; i < recordCount ; i++) {
				Object record = recordArray[i];
				byte[] bytes = recordSerializer.serialize(record);
				multiRecordSerializer.addRecord(bytes);
			}
			byte[] bytes = multiRecordSerializer.getOutBytes();
			Log.debug(CAT, "Bytes length: " + bytes.length);
			recordStore.addRecord(bytes, 0, bytes.length);
		}
		catch (SerializerException e) {
			throw new MultiRecordDaoException(e);
		}
		catch (IOException e) {
			throw new MultiRecordDaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			throw new MultiRecordDaoException(e);
		}
		catch (RecordStoreFullException e) {
			throw new MultiRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new MultiRecordDaoException(e);
		}
		finally {
			if (multiRecordSerializer != null) {
				multiRecordSerializer.dispose();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IMultiRecordDao#createRecordEnumeration(org.helyx.app.j2me.lib.filter.IRecordFilter)
	 */
	public MultiRecordEnumeration createRecordEnumeration(IRecordFilter recordFilter) {
		MultiRecordEnumeration multiRecordEnumeration = new MultiRecordEnumeration(recordStore, recordFilter, recordSerializer);
		return multiRecordEnumeration;
	}

	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IMultiRecordDao#destroyRecordEnumeration(org.helyx.app.j2me.lib.rms.MultiRecordEnumeration)
	 */
	public void destroyRecordEnumeration(MultiRecordEnumeration multiRecordEnumeration) {
		multiRecordEnumeration.destroy();
	}

	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IMultiRecordDao#countRecords()
	 */
	public int countRecords() {
		try {
			return recordStore.getNumRecords();
		}
		catch (RecordStoreNotOpenException e) {
			throw new MultiRecordDaoException(e);
		}
	}
}
