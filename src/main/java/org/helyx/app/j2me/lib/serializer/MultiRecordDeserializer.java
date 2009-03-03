package org.helyx.app.j2me.lib.serializer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;

public class MultiRecordDeserializer {

	private static final Logger logger = LoggerFactory.getLogger("MULTI_RECORD_DESERIALIZER");
	
	private int recordIterator;
	private int recordIteratorCount;
	
	protected DataInputStream dis;
	protected ByteArrayInputStream bis;
	
	private byte[] byteBuffer;

	public MultiRecordDeserializer(byte[] bytes) throws IOException {
		super();
		init(bytes);
	}
	
	private void init(byte[] bytes) throws IOException {
		bis = new ByteArrayInputStream(bytes);
		dis = new DataInputStream(bis);
		recordIteratorCount = dis.readInt();
		logger.debug("Record iterator count: " + recordIteratorCount);
	}
	
	public void dispose() {
		cleanUpResources();
	}
	
	private void cleanUpResources() {
		if (dis != null) {
			try { dis.close(); } catch (IOException e) { logger.warn(e); }
		}
	}

	public boolean hasMoreElements() {
		return recordIterator < recordIteratorCount;
	}
	
	public int nextElement() throws IOException {
		int recordSize = dis.readInt();
		if (byteBuffer == null || byteBuffer.length < recordSize) {
			byteBuffer = new byte[recordSize];
		}
		dis.read(byteBuffer, 0, recordSize);
		recordIterator++;
		return recordSize;
	}
	
	public int nextElement(byte[] bytes) throws IOException {
		int recordSize = dis.readInt();
		dis.read(bytes, 0, recordSize);
		recordIterator++;

		return recordSize;
	}
	
	public byte[] getByteBuffer() {
		return byteBuffer;
	}

}
