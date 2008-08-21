package org.helyx.app.j2me.lib.pref;

import java.io.IOException;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;

public class PrefSerializer extends AbstractObjectSerializer {

	private static final Log log = LogFactory.getLog("PREF_SERIALIZER");
	
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
