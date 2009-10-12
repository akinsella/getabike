package org.helyx.app.j2me.velocite.data.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.helyx.basics4me.util.Properties;

public class PropertiesHelper {

	private PropertiesHelper() {
		super();
	}
	
	public static Properties convertStringToProperties(String propertiesStr) throws IOException {
		
		byte[] bytes = propertiesStr.getBytes();
		
		Properties properties = new Properties();

		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try {
			properties.load(bais);
		}
		finally {
			if (bais != null) {
				bais.close();
			}
		}
		
		return properties;
	}
	
	public static String convertPropertiesToString(Properties properties) throws IOException {
		String propertiesStr = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			properties.store(baos, "Application data properties");
			
			byte[] bytes = baos.toByteArray();
			propertiesStr = new String(bytes);		
		}
		finally {
			if (baos != null) {
				baos.close();
			}
		}
		
		return propertiesStr;
	}
	
}
