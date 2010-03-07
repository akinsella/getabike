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
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.rms.DaoException;
import org.helyx.app.j2me.getabike.lib.rms.IRecordReader;
import org.helyx.app.j2me.getabike.lib.rms.RecordReader;
import org.helyx.app.j2me.getabike.lib.serializer.IObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class DefaultRecordEnumeration implements Enumeration {

	public static final Logger logger = Logger.getLogger("DEFAULT_RECORD_ENUMERATION");
	
	private RecordEnumeration recordEnumeration;
	private IRecordReader recordReader;
	private boolean isFiltered;
	private Object nextRecord;
	
	public DefaultRecordEnumeration(RecordStore recordStore,RecordEnumeration recordEnumeration, IObjectSerializer recordSerializer) {
		this(recordStore, recordEnumeration, null, recordSerializer);
	}

	public DefaultRecordEnumeration(RecordStore recordStore, RecordEnumeration recordEnumeration, RecordFilter recordFilter, IObjectSerializer recordSerializer) {
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
