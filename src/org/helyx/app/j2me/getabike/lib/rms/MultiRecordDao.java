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
package org.helyx.app.j2me.getabike.lib.rms;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.rms.IMultiRecordDao;
import org.helyx.app.j2me.getabike.lib.rms.IRecordReader;
import org.helyx.app.j2me.getabike.lib.rms.MultiRecordEnumeration;
import org.helyx.app.j2me.getabike.lib.rms.RecordReader;
import org.helyx.app.j2me.getabike.lib.rms.exception.MultiRecordDaoException;
import org.helyx.app.j2me.getabike.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.MultiRecordSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class MultiRecordDao implements IMultiRecordDao {
	
	private static final Logger logger = Logger.getLogger("MULTI_RECORD_DAO");
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
			logger.debug("Bytes length: " + bytes.length);
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
	public MultiRecordEnumeration createRecordEnumeration(RecordFilter recordFilter) {
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
