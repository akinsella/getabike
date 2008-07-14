package org.helyx.app.j2me.lib.serializer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.helyx.app.j2me.lib.log.Log;

public class MultiRecordSerializer {

	private static final String CAT = "MULTI_RECORD_SERIALIZER";
	
	private int byteBufferLength;
	
	protected ByteArrayOutputStream bos;
	protected DataOutputStream dos;
	private int recordCount;

	public MultiRecordSerializer() {
		super();
		init();
	}
	
	private void init() {
		
	}

	public void dispose() {
		if (dos != null) { 
			try { dos.close();  } catch (IOException e) { Log.warn(CAT, e); }
		}
	}
	
	public void initData(int recordCount, int byteBufferLength) throws IOException {

		this.recordCount = recordCount;
		this.byteBufferLength = byteBufferLength;
		if (bos == null) {
			bos = new ByteArrayOutputStream(byteBufferLength);
			dos = new DataOutputStream(bos);
		}
		else {
			bos.reset();
		}
		dos.writeInt(recordCount);
	}
	
	public byte[] getOutBytes() {
		return bos.toByteArray();	
	}
	
	public void addRecord(byte[] bytes) throws IOException {
		int length = bytes.length;
		dos.writeInt(length);
		dos.write(bytes);
	}

}
