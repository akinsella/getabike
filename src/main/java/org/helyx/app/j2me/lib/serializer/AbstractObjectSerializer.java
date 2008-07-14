package org.helyx.app.j2me.lib.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.helyx.app.j2me.lib.filter.IRecordFilter;
import org.helyx.app.j2me.lib.log.Log;

public abstract class AbstractObjectSerializer implements IObjectSerializer {
	
	private static final String CAT = "ABSTRACT_OBJECT_SERIALIZER";
	
	private static final int BYTE_BUFFER_LENGTH = 1024;
	
	protected byte[] byteArray;
	protected DataInputStream dis;
	protected ByteArrayInputStream bis;
	
	protected ByteArrayOutputStream bos;
	protected DataOutputStream dos;
	
	protected int bufferFileLength = BYTE_BUFFER_LENGTH;

	public AbstractObjectSerializer() {
		super();
		init();
	}
	
	private void init() {
		byteArray = new byte[BYTE_BUFFER_LENGTH];
		bis = new ByteArrayInputStream(byteArray);

		dis = new DataInputStream(bis);

		bos = new ByteArrayOutputStream(BYTE_BUFFER_LENGTH);
		dos = new DataOutputStream(bos);
	}

	public void ensureInputByteBufferLength(int length) {
		int bytesLength = length;
		if(bytesLength > byteArray.length) {
			tryCloseDis();
			byteArray = new byte[bytesLength];
			bis = new ByteArrayInputStream(byteArray);
			dis = new DataInputStream(bis);
		}
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public byte[] getByteArray(int length) {
		ensureInputByteBufferLength(length);
		return byteArray;
	}
	
	public void dispose() {
		tryCloseDos();
		tryCloseDis();
	}
	
	private void tryCloseDos() {
		if (dos != null) { 
			try { dos.close();  } catch (IOException e) { Log.warn(CAT, e); }
			bos = null;
			dos = null;
		}
	}
	
	private void tryCloseDis() {
		if (dis != null) {
			try { dis.close(); } catch (IOException e) { Log.warn(CAT, e); }
			bis = null;
			dis = null;
		}
	}

	public boolean matches(IRecordFilter recordFilter) throws SerializerException {
		try {
			return recordFilter.matches(dis);
		}
		catch (IOException ioe) {
			throw new SerializerException(ioe);
		}
	}

}
