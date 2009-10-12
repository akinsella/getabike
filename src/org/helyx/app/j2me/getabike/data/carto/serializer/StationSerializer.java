package org.helyx.app.j2me.getabike.data.carto.serializer;

import java.io.IOException;
import java.util.Date;

import org.helyx.app.j2me.getabike.data.carto.domain.Station;
import org.helyx.app.j2me.getabike.data.carto.domain.StationDetails;
import org.helyx.helyx4me.localization.Point;
import org.helyx.helyx4me.serializer.AbstractObjectSerializer;
import org.helyx.helyx4me.serializer.SerializerException;
import org.helyx.logging4me.Logger;


public class StationSerializer extends AbstractObjectSerializer {
	
	private static final Logger logger = Logger.getLogger("STATION_SERIALIZER");
	
	private boolean serializeDetails = true;

	public StationSerializer() {
		this(true);
	}

	public StationSerializer(boolean serializeDetails) {
		super();
		this.serializeDetails = serializeDetails;
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
			dos.writeBoolean(station.tpe);
			
			dos.writeBoolean(station.hasLocalization);		

			dos.writeDouble(station.localization.lng);
			dos.writeDouble(station.localization.lat);
			
			dos.writeBoolean(serializeDetails && station.details != null);
			
			if (serializeDetails && station.details != null) {
				dos.writeBoolean(station.details.open);
				dos.writeBoolean(station.details.ticket);
				dos.writeBoolean(station.details.tpe);
				dos.writeInt(station.details.available);
				dos.writeInt(station.details.free);
				dos.writeInt(station.details.hs);
				dos.writeInt(station.details.total);
				dos.writeLong(station.details.date.getTime());
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
			
			Station station = new Station();
						
			station.number = dis.readInt();
			
			station.name = dis.readUTF();
			station.address = dis.readUTF();
			station.fullAddress = dis.readUTF();
			station.zipCode = dis.readUTF();
			station.city = dis.readUTF();
			
			station.open = dis.readBoolean();
			station.bonus = dis.readBoolean();
			station.tpe = dis.readBoolean();
			
			station.hasLocalization = dis.readBoolean();
			
			Point localization = new Point();
			station.localization = localization;
			
			localization.lng = dis.readDouble();
			localization.lat = dis.readDouble();
			
			boolean hasStationDetails = dis.readBoolean();
			
			if (hasStationDetails) {
				StationDetails stationDetails = new StationDetails();
				stationDetails.stationNumber = station.number;
				stationDetails.open = dis.readBoolean();
				stationDetails.ticket = dis.readBoolean();
				stationDetails.tpe = dis.readBoolean();
				
				stationDetails.available = dis.readInt();
				stationDetails.free = dis.readInt();
				stationDetails.hs = dis.readInt();
				stationDetails.total = dis.readInt();
				stationDetails.date = new Date(dis.readLong());
			}
		
			return station;
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}
	}
	
}
