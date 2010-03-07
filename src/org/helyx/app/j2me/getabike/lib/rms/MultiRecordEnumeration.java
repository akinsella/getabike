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
import java.util.Enumeration;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.rms.DaoException;
import org.helyx.app.j2me.getabike.lib.rms.MultiRecordReader;
import org.helyx.app.j2me.getabike.lib.serializer.IObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.MultiRecordDeserializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class MultiRecordEnumeration implements Enumeration {

	public static final Logger logger = Logger.getLogger("MULTI_RECORD_ENUMERATION");
	
	private MultiRecordReader recordReader;
	private boolean isFiltered;
	private Object nextRecord;
	private MultiRecordDeserializer multiRecordDeserializer;
	private boolean hasRecords = false;
	
	public MultiRecordEnumeration(RecordStore recordStore, IObjectSerializer recordSerializer) {
		this(recordStore, null, recordSerializer);
	}

	public MultiRecordEnumeration(RecordStore recordStore, RecordFilter recordFilter, IObjectSerializer recordSerializer) {
		super();
		try {
			this.isFiltered = recordFilter != null;
			if (recordStore.getNumRecords() <= 0) {
				hasRecords = false;
				return;
			}
			hasRecords = true;
			byte[] bytes = recordStore.getRecord(1);
			logger.debug("First Record bytes length: " + bytes.length);
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
			logger.warn(e);
			throw new DaoException(e);
		}
		catch (SerializerException e) {
			logger.warn(e);
			throw new DaoException(e);
		}
		catch (InvalidRecordIDException e) {
			logger.warn(e);
			throw new DaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			logger.warn(e);
			throw new DaoException(e);
		}
		catch (RecordStoreException e) {
			logger.warn(e);
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
