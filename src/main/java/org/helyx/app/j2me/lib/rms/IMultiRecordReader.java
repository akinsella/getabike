package org.helyx.app.j2me.lib.rms;

import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.lib.serializer.SerializerException;

public interface IMultiRecordReader {

	Object nextRecord() throws IOException, SerializerException, RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException;
	
	void dispose();
	
	public void fetchNextRecord() throws IOException, SerializerException, RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException;

	public boolean filterMatchesPrefetchedRecord() throws SerializerException;
	
	public Object readPrefetchedRecord() throws SerializerException;

}
