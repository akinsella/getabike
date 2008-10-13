package org.helyx.app.j2me.velocite.data.carto.serializer;

import java.io.IOException;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;
import org.helyx.app.j2me.velocite.data.carto.domain.Station;

public class StationSerializer extends AbstractObjectSerializer {
	
	private static final Log log = LogFactory.getLog("STATION_SERIALIZER");

	public StationSerializer() {
		super();
	}
	
	public byte[] serialize(Object object) throws SerializerException {		
		try {
			bos.reset();
	
			Station station = (Station)object;
	
			dos.writeInt(station.number);
			
			dos.writeUTF(station.name);
			dos.writeUTF(station.address);
			dos.writeUTF(station.fullAddress);
			
			dos.writeUTF(station.zipCode == null ? "" : station.zipCode);
			dos.writeUTF(station.city == null ? "" : station.city);
			
			dos.writeBoolean(station.open);
			dos.writeBoolean(station.bonus);
			
			dos.writeBoolean(station.hasLocalization);		
			
			dos.writeDouble(station.localization.lng);
			dos.writeDouble(station.localization.lat);
	
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
			
			Station station = new Station();
						
			station.number = dis.readInt();
			
			station.name = dis.readUTF();
			station.address = dis.readUTF();
			station.fullAddress = dis.readUTF();
			station.zipCode = dis.readUTF();
			station.city = dis.readUTF();
			
			station.open = dis.readBoolean();
			station.bonus = dis.readBoolean();
			
			station.hasLocalization = dis.readBoolean();
			
			Point localization = new Point();
			station.localization = localization;
			
			localization.lng = dis.readDouble();
			localization.lat = dis.readDouble();
			
			return station;
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}
	}
	
}
