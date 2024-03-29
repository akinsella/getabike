package org.helyx.app.j2me.getabike.data.city.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.helyx.app.j2me.getabike.data.city.domain.Quartier;
import org.helyx.app.j2me.getabike.lib.serializer.BasicObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class QuartierSerializer implements BasicObjectSerializer {

	private static final Logger logger = Logger.getLogger("QUARTIER_SERIALIZER");

	public QuartierSerializer() {
		super();
	}

	public void serialize(DataOutputStream dos, Object object) throws SerializerException {
		try {
			Quartier quartier = (Quartier)object;
	
			dos.writeInt(quartier.id);
			dos.writeUTF(quartier.city);
			dos.writeUTF(quartier.name);
			dos.writeUTF(quartier.zipCode);
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}

	}

	public Object deserialize(DataInputStream dis) throws SerializerException {
		try {
			Quartier quartier = new Quartier();
	
			quartier.id = dis.readInt();
			quartier.city = dis.readUTF();
			quartier.name = dis.readUTF();
			quartier.zipCode = dis.readUTF();
			
			return quartier;
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}

	}
	
}
