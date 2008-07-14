package org.helyx.app.j2me.lib.rms;

import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.serializer.IObjectSerializer;
import org.helyx.app.j2me.lib.serializer.MultiRecordDeserializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;

public class MultiRecordReader implements IMultiRecordReader {

	private MultiRecordDeserializer multiRecordDeserializer;
	private IObjectSerializer recordSerializer;
	private IRecordFilter recordFilter;
	
	public MultiRecordReader(MultiRecordDeserializer multiRecordDeserializer, IObjectSerializer recordSerializer) {
		this(multiRecordDeserializer, recordSerializer, null);
	}
	
	public MultiRecordReader(MultiRecordDeserializer multiRecordDeserializer, IObjectSerializer recordSerializer, IRecordFilter recordFilter) {
		super();
		this.multiRecordDeserializer = multiRecordDeserializer;
		this.recordSerializer = recordSerializer;
		this.recordFilter = recordFilter;
	}
	
	public void dispose() {
		if (recordSerializer != null) {
			recordSerializer.dispose();
		}
	}
	
	public void fetchNextRecord() throws IOException, RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
		multiRecordDeserializer.nextElement(recordSerializer.getByteArray());
	}
	
	public boolean filterMatchesPrefetchedRecord() throws SerializerException {
		return recordSerializer.matches(recordFilter);
	}
	
	public Object readPrefetchedRecord() throws SerializerException {
		Object object = recordSerializer.deserialize();
		return object;
	}

	public Object nextRecord() throws IOException, SerializerException, RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
		multiRecordDeserializer.nextElement(recordSerializer.getByteArray());
		Object object = recordSerializer.deserialize();
		return object;
	}

}
