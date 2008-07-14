package org.helyx.app.j2me.velocite.data.carto.serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import org.helyx.app.j2me.lib.serializer.AbstractObjectSerializer;
import org.helyx.app.j2me.lib.serializer.SerializerException;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;

public class StationDetailsSerializer extends AbstractObjectSerializer {

	private static final String CAT = "STATION_DETAILS_SERIALIZER";

	public StationDetailsSerializer() {
		super();
	}

	public byte[] serialize(Object object) throws SerializerException {
		try {
				
			bos.reset();
	
			StationDetails stationDetails = (StationDetails)object;
	
			dos = new DataOutputStream(bos);
			
			dos.writeInt(stationDetails.stationNumber);
			dos.writeLong(stationDetails.dateCreation.getTime());
			dos.writeInt(stationDetails.available);
			
			dos.writeInt(stationDetails.free);
			dos.writeBoolean(stationDetails.ticket);
	
			dos.writeInt(stationDetails.total);
	
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
		
			StationDetails stationDetails = new StationDetails();
		
			stationDetails.stationNumber = dis.readInt();
			stationDetails.dateCreation = new Date(dis.readLong());
			stationDetails.available = dis.readInt();
			stationDetails.free = dis.readInt();
			stationDetails.ticket = dis.readBoolean();
			stationDetails.total = dis.readInt();
			
			return stationDetails;
		}
		catch(IOException e) {
			throw new SerializerException(e);
		}

	}
	
}
