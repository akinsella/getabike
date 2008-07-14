package org.helyx.app.j2me.velocite.data.carto.dao;


import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import org.helyx.app.j2me.lib.rms.IRecordReader;
import org.helyx.app.j2me.lib.rms.RecordReader;
import org.helyx.app.j2me.lib.rms.exception.BasicRecordDaoException;
import org.helyx.app.j2me.lib.serializer.SerializerException;
import org.helyx.app.j2me.velocite.data.carto.comparator.StationDetailsDateCreationComparator;
import org.helyx.app.j2me.velocite.data.carto.domain.StationDetails;
import org.helyx.app.j2me.velocite.data.carto.filter.StationDetailsNumberFilter;
import org.helyx.app.j2me.velocite.data.carto.serializer.StationDetailsSerializer;

public class StationDetailsDao implements IStationDetailsDao {
	
	private static final String CAT = "STATION_DETAILS_DAO";

	public static final String STATION_DETAILS_RECORD_STORE = "station_details";
	
	private RecordStore recordStore;
	private IRecordReader recordReader;
	
	private StationDetailsSerializer stationDetailsSerializer;
	
	public StationDetailsDao() {
		super();
		try {
			recordStore = RecordStore.openRecordStore(STATION_DETAILS_RECORD_STORE, true);
			init();
		}
		catch (RecordStoreFullException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreNotFoundException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	private void init() {
		stationDetailsSerializer = new StationDetailsSerializer();
		recordReader = new RecordReader(recordStore, stationDetailsSerializer);
	}

	public void dispose() {
		try {
			if (recordReader != null) {
				recordReader.dispose();
			}
			if (recordStore != null) {
				recordStore.closeRecordStore();
			}
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	public int saveStationDetails(StationDetails stationDetails) {
		try {
			byte[] bytes = stationDetailsSerializer.serialize(stationDetails);
			
			int recordId = recordStore.addRecord(bytes, 0, bytes.length);
			
			return recordId;
		}
		catch (SerializerException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreFullException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

	public StationDetails findStationDetailsByNumber(int stationNumber) {
		RecordEnumeration recordEnumeration = null;
		
		try {
			try {
				recordEnumeration = recordStore.enumerateRecords(
						new StationDetailsNumberFilter(stationNumber), 
						new StationDetailsDateCreationComparator(), 
						false);
				if (recordEnumeration.hasNextElement()) {
					int recordId = recordEnumeration.nextRecordId();
					StationDetails stationDetails = (StationDetails)recordReader.readRecord(recordId);
					return stationDetails;
				}
				else {
					return null;
				}
			}
			finally {
				if (recordEnumeration != null) {
					recordEnumeration.destroy();
				}
			}
		} 
		catch (SerializerException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (IOException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreNotOpenException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (InvalidRecordIDException e) {
			throw new BasicRecordDaoException(e);
		}
		catch (RecordStoreException e) {
			throw new BasicRecordDaoException(e);
		}
	}

}


