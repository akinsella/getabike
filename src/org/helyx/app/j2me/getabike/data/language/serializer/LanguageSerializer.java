package org.helyx.app.j2me.getabike.data.language.serializer;

import java.io.IOException;

import org.helyx.app.j2me.getabike.data.language.domain.Language;
import org.helyx.app.j2me.getabike.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.getabike.lib.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class LanguageSerializer extends AbstractObjectSerializer {

	private static final Logger logger = Logger.getLogger("LANGUAGE_SERIALIZER");

	public LanguageSerializer() {
		super();
	}

	public byte[] serialize(Object object) throws SerializerException {
		try {
			bos.reset();
	
			Language language = (Language)object;
	
			dos.writeUTF(language.key);
			dos.writeUTF(language.name);
			dos.writeUTF(language.country);
			dos.writeUTF(language.localeCountry);
			dos.writeUTF(language.localeLanguage);
	
			byte [] bytes = bos.toByteArray();
			
			return bytes;
	
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}

	}

	public Object deserialize() throws SerializerException {
		try {
			dis.reset();
			Language language = new Language();
	
			language.key = dis.readUTF();
			language.name = dis.readUTF();
			language.country = dis.readUTF();
			language.localeCountry = dis.readUTF();
			language.localeLanguage = dis.readUTF();
			
			return language;
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}

	}
	
}
