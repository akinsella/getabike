package org.helyx.app.j2me.lib.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface BasicObjectSerializer {

	void serialize(DataOutputStream dos, Object object) throws SerializerException;

	Object deserialize(DataInputStream dis) throws SerializerException;

}
