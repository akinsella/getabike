package org.helyx.app.j2me.lib.pref;

import java.io.IOException;

import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;

public class PrefSerializer extends AbstractObjectSerializer {

	private static final Logger logger = LoggerFactory.getLogger("PREF_SERIALIZER");
	
	public PrefSerializer() {
		super();
	}

	public byte[] serialize(Object object) throws SerializerException {
		try {
			bos.reset();
	
			Pref pref = (Pref)object;
	
			dos.writeUTF(pref.key);
			dos.writeUTF(pref.value);
	
			byte [] bytes = bos.toByteArray();
			return bytes;
		}
		catch (IOException e) {
			throw new SerializerException(e);
		}

	}

	public Object deserialize() throws SerializerException {
		try {
			dis.reset();
	
			Pref pref = new Pref();
			
			pref.key = dis.readUTF();
			pref.value = dis.readUTF();
	
			return pref;
		}
		catch (IOException e) {
			throw new SerializerException(e);
		}
	}

}
