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
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.getabike.lib.filter.record.RecordFilter;
import org.helyx.app.j2me.getabike.lib.rms.IRecordReader;
import org.helyx.app.j2me.getabike.lib.serializer.IObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;

public class RecordReader implements IRecordReader {

	private RecordStore recordStore;
	private IObjectSerializer recordSerializer;
	private RecordFilter recordFilter;
	
	public RecordReader(RecordStore recordStore, IObjectSerializer recordSerializer) {
		this(recordStore, recordSerializer, null);
	}
	
	public RecordReader(RecordStore recordStore, IObjectSerializer recordSerializer, RecordFilter recordFilter) {
		super();
		this.recordStore = recordStore;
		this.recordSerializer = recordSerializer;
		this.recordFilter = recordFilter;
	}
	
	public void dispose() {
		if (recordSerializer != null) {
			recordSerializer.dispose();
		}
	}
	
	public void fetchNextRecord(int recordId) throws SerializerException, RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
		int recordSize = recordStore.getRecordSize(recordId);
		recordStore.getRecord(recordId, recordSerializer.getByteArray(recordSize), 0);		
	}
	
	public boolean filterMatchesPrefetchedRecord() throws SerializerException {
		return recordSerializer.matches(recordFilter);
	}
	
	public Object readPrefetchedRecord() throws SerializerException {
		Object object = recordSerializer.deserialize();
		return object;
	}

	public Object readRecord(int recordId) throws IOException, SerializerException, RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
		recordStore.getRecord(recordId, recordSerializer.getByteArray(), 0);		
		Object object = recordSerializer.deserialize();
		return object;
	}

	public void setRecordStore(RecordStore recordStore) {
		this.recordStore = recordStore;
	}
	
}
