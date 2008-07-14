package org.helyx.app.j2me.lib.rms;

import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.lib.serializer.SerializerException;

public interface IRecordReader {

	Object readRecord(int recordId) throws IOException, SerializerException, RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException;
	
	void dispose();

	void setRecordStore(RecordStore recordStore);
	
	public void fetchNextRecord(int recordId) throws SerializerException, RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException;

	public boolean filterMatchesPrefetchedRecord() throws SerializerException;
	
	public Object readPrefetchedRecord() throws SerializerException;

}
