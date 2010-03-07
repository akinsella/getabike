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

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.getabike.lib.rms.DaoException;
import org.helyx.app.j2me.getabike.lib.rms.IBasicRecordDao;
import org.helyx.app.j2me.getabike.lib.rms.IRecordReader;
import org.helyx.app.j2me.getabike.lib.rms.RecordReader;
import org.helyx.app.j2me.getabike.lib.rms.exception.BasicRecordDaoException;
import org.helyx.app.j2me.getabike.lib.rms.exception.MultiRecordDaoException;
import org.helyx.app.j2me.getabike.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class BasicRecordDao implements IBasicRecordDao {
	
	private static final Logger logger = Logger.getLogger("BASIC_RECORD_DAO");

	public String recordStoreName;
	
	private AbstractObjectSerializer recordSerializer;
	private RecordStore recordStore;
	private IRecordReader recordReader;
	
	public BasicRecordDao(String recordStoreName, AbstractObjectSerializer recordSerializer) {
		super();
		this.recordStoreName = recordStoreName;
		this.recordSerializer = recordSerializer;
		init();
	}
	
	private void init() {
		recordReader = new RecordReader(recordStore, recordSerializer);
		try {
			recordStore = RecordStore.openRecordStore(recordStoreName, true);
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

	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IBasicRecordDao#dispose()
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
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IBasicRecordDao#findFirstRecord()
	 */
	public Object findFirstRecord() {
		RecordEnumeration recordEnumeration = null;
		
		try {
			try {
				recordEnumeration = recordStore.enumerateRecords(null, null, false);
				if (recordEnumeration.hasNextElement()) {
					int recordId = recordEnumeration.nextRecordId();
					Object record = (Object)recordReader.readRecord(recordId);
					
					return record;
				}
				throw new BasicRecordDaoException("No record found.");
			}
			finally {
				if (recordEnumeration != null) {
					recordEnumeration.destroy();
				}
			}
		} 
		catch (IOException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (SerializerException e) {
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

	/* (non-Javadoc)
	 * @see org.helyx.app.j2me.lib.rms.IBasicRecordDao#removeAllRecords()
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
	 * @see org.helyx.app.j2me.lib.rms.IBasicRecordDao#saveNewRecord(java.lang.Object)
	 */
	public void saveNewRecord(Object record) {
		try {
			byte[] bytes = recordSerializer.serialize(record);
			
			recordStore.addRecord(bytes, 0, bytes.length);
		}
		catch (SerializerException e) {
			throw new DaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreFullException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

}


