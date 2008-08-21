package org.helyx.app.j2me.lib.test;

import junit.framework.TestCase;

import org.helyx.app.j2me.lib.concurrent.Future;
import org.helyx.app.j2me.lib.content.accessor.ClasspathContentAccessor;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.ContentProviderProgressTaskAdapter;
import org.helyx.app.j2me.lib.content.provider.IContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.reflect.FieldType;
import org.helyx.app.j2me.lib.reflect.RefObject;
import org.helyx.app.j2me.lib.reflect.RefObjectDeserializer;
import org.helyx.app.j2me.lib.reflect.RefObjectMetaData;
import org.helyx.app.j2me.lib.reflect.RefObjectMetaDataContentProvider;
import org.helyx.app.j2me.lib.task.IProgressTask;

public class ReflectTest extends TestCase {
	
	private static final Log log = LogFactory.getLog("REFLECT_TEST");

	private static final String KEY = "key";
	private static final String ACTIVE = "active";
	private static final String NAME = "name";
	private static final String WEB_SITE = "webSite";
	private static final String OFFLINE_STATION_LIST = "offlineStationList";
	private static final String STATION_DETAILS = "stationDetails";
	private static final String STATION_LIST = "stationList";
	private static final String TYPE = "type";
	
	public void testSerialize() throws Exception {

		RefObject city = new RefObject();
		city.setString(KEY, "PARIS");
		city.setString(NAME, "Paris");
		city.setBoolean(ACTIVE, true);
		city.setString(TYPE, "DEFAULT");
		city.setString(WEB_SITE, "http://www.velib.paris.fr");
		city.setString(STATION_LIST, "http://www.velib.paris.fr/service/carto/");
		city.setString(STATION_DETAILS, "http://www.velib.paris.fr/service/stationdetails/");
		city.setString(OFFLINE_STATION_LIST, "/carto_paris.xml");
		
		
		IContentAccessor contentAccessor = new ClasspathContentAccessor("/data/md/cities.md.xml");
		IContentProvider metadataContentProvider = new RefObjectMetaDataContentProvider(contentAccessor);

		IProgressTask progressTask = new ContentProviderProgressTaskAdapter(metadataContentProvider);
		RefObjectMetaData rom = (RefObjectMetaData)Future.get(progressTask);
		RefObjectDeserializer refObjectDeserializer = new RefObjectDeserializer(rom);
		
//		for (int i = 0 ; i< 1000 ; i++) {
			byte[] bytes = refObjectDeserializer.serialize(city);
			int byteLength = bytes.length;
			byte[] targetBytes = refObjectDeserializer.getByteArray(byteLength);
			System.arraycopy(bytes, 0, targetBytes, 0, byteLength);
			Object newCity = refObjectDeserializer.deserialize();

			log.info("Original City: " + city);
			log.info("New City: " + newCity);
//		}
//		log.info("Fin");
	}

	private RefObjectMetaData getCityRefObjectMetaData() {
		RefObjectMetaData rom = new RefObjectMetaData("city");
		rom.addFieldInfoByValues(1, KEY, FieldType.STRING);
		rom.addFieldInfoByValues(2, ACTIVE, FieldType.BOOLEAN);
		rom.addFieldInfoByValues(3, NAME, FieldType.STRING);
		rom.addFieldInfoByValues(4, WEB_SITE, FieldType.STRING);
		rom.addFieldInfoByValues(5, OFFLINE_STATION_LIST, FieldType.STRING);
		rom.addFieldInfoByValues(6, STATION_DETAILS, FieldType.STRING);
		rom.addFieldInfoByValues(7, STATION_LIST, FieldType.STRING);
		rom.addFieldInfoByValues(8, TYPE, FieldType.STRING);
		
		return rom;
	}
	
}
