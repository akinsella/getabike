package org.helyx.app.j2me.velocite.data.city.serializer;

import java.io.IOException;

import org.helyx.app.j2me.velocite.data.city.domain.City;
import org.helyx.app.j2me.velocite.data.city.domain.Quartier;
import org.helyx.helyx4me.serializer.AbstractObjectSerializer;
import org.helyx.helyx4me.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class CitySerializer extends AbstractObjectSerializer {

	private static final Logger logger = Logger.getLogger("CITY_SERIALIZER");
	
	private static QuartierSerializer quartierSerializer = new QuartierSerializer();

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
			dos.writeUTF(city.serviceName);
			dos.writeUTF(city.webSite);
			dos.writeUTF(city.stationList);
			dos.writeUTF(city.stationDetails);
			dos.writeBoolean(city.bonus);
			dos.writeBoolean(city.state);
			dos.writeBoolean(city.tpe);
			dos.writeUTF(city.normalizer);
	
			int quartierCount = city.quartierList.size();
			dos.writeInt(quartierCount);
			
			for (int i = 0 ; i < quartierCount ; i++) {
				Quartier quartier = (Quartier)city.quartierList.elementAt(i);
				quartierSerializer.serialize(dos, quartier);
			}

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
			city.serviceName = dis.readUTF();
			city.webSite = dis.readUTF();
			city.stationList = dis.readUTF();
			city.stationDetails = dis.readUTF();
			city.bonus = dis.readBoolean();
			city.state = dis.readBoolean();
			city.tpe = dis.readBoolean();
			city.normalizer = dis.readUTF();
			
			int quartierCount = dis.readInt();
			
			for (int i = 0 ; i < quartierCount ; i++) {
				Quartier quartier = (Quartier)quartierSerializer.deserialize(dis);
				city.quartierList.addElement(quartier);
			}
			
			return city;
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}

	}
	
}
