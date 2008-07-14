package org.helyx.app.j2me.lib.resource;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {
	
	public static final String CAT = "RESOURCE_UTIL";

	public  ResourceUtil() {
		super();
	}

//	public String read(String filename) throws IOException {
//		InputStream in = null;
//
//		try {
//			int c;
//			StringBuffer sb;
//
//			sb = new StringBuffer();
//			in = getClass().getResourceAsStream(filename);
//
//			while ((c = in.read()) != -1) {
//				sb.append((char)c);
//			}
//			in.close();
//			return sb.toString();
//		}
//		finally {
//			if (in != null) {
//				in.close();
//			}
//		}
//	}
	
	public static InputStream createsFromClassPath(String path) throws IOException {
		return ResourceUtil.class.getResourceAsStream(path);
	}
	
//	public static ByteArrayInputStream createBaisFromClassPath(String path) throws IOException {
//		InputStream is = null;
//		ByteArrayOutputStream baos = null;
//		
//		try {
//			is = ImageUtil.class.getResourceAsStream(path);
//			baos = new ByteArrayOutputStream();
//			byte[] buffer = new byte[2048];
//			int length = 0;
//			while ((length = is.read(buffer)) != -1) {
//				baos.write(buffer, 0, length);
//			}
//			byte[] bytes = baos.toByteArray();
//			
//			Log.info("loadImageFromClassPath: " + path + ", length: " + (bytes.length));
//			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//			
//			return bais;
//		}
//		finally {
//			if (is != null) {
//				is.close();
//				is = null;
//			}
//			if (baos != null) {
//				baos.close();
//				baos = null;
//			}
//		}
//	}

}
