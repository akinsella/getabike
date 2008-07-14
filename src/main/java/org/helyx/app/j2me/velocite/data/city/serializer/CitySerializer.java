package org.helyx.app.j2me.velocite.data.city.serializer;

import java.io.IOException;

import org.helyx.app.j2me.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;
import org.helyx.app.j2me.velocite.data.city.domain.City;

public class CitySerializer extends AbstractObjectSerializer {

	private static final String CAT = "CITY_SERIALIZER";

	public CitySerializer() {
		super();
	}

	public byte[] serialize(Object object) throws SerializerException {
		try {
			bos.reset();
	
			City city = (City)object;
	
			dos.writeUTF(city.key);
			dos.writeBoolean(city.active);
			dos.writeUTF(city.type);
			dos.writeUTF(city.name);
			dos.writeUTF(city.webSite);
			dos.writeUTF(city.contentProviderFactory);
			dos.writeUTF(city.stationList);
			dos.writeUTF(city.stationDetails);
			dos.writeUTF(city.offlineStationList);
	
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
			City city = new City();
	
			city.key = dis.readUTF();
			city.active = dis.readBoolean();
			city.type = dis.readUTF();
			city.name = dis.readUTF();
			city.webSite = dis.readUTF();
			city.contentProviderFactory = dis.readUTF();
			city.stationList = dis.readUTF();
			city.stationDetails = dis.readUTF();
			city.offlineStationList = dis.readUTF();
			
			return city;
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}

	}
	
}
