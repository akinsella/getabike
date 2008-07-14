package org.helyx.app.j2me.lib.serializer;

import org.helyx.app.j2me.lib.filter.IRecordFilter;

public interface IObjectSerializer {

	byte[] serialize(Object object) throws SerializerException;

	Object deserialize() throws SerializerException;
	
	void dispose();

	boolean matches(IRecordFilter recordFilter) throws SerializerException;
	
	void ensureInputByteBufferLength(int length);

	byte[] getByteArray();
	
	byte[] getByteArray(int length);

}
