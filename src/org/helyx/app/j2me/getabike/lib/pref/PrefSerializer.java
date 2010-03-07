/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.pref;

import java.io.IOException;

import org.helyx.app.j2me.getabike.lib.pref.Pref;
import org.helyx.app.j2me.getabike.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class PrefSerializer extends AbstractObjectSerializer {

	private static final Logger logger = Logger.getLogger("PREF_SERIALIZER");
	
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
