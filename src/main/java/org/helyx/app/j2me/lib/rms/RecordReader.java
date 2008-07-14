package org.helyx.app.j2me.lib.rms;

import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.serializer.IObjectSerializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;

public class RecordReader implements IRecordReader {

	private RecordStore recordStore;
	private IObjectSerializer recordSerializer;
	private IRecordFilter recordFilter;
	
	public RecordReader(RecordStore recordStore, IObjectSerializer recordSerializer) {
		this(recordStore, recordSerializer, null);
	}
	
	public RecordReader(RecordStore recordStore, IObjectSerializer recordSerializer, IRecordFilter recordFilter) {
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
