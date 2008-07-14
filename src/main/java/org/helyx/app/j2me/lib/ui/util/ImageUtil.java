package org.helyx.app.j2me.lib.ui.util;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Image;

public class ImageUtil {

	private ImageUtil() {
		super();
	}
	
	public static Image createImageFromClassPath(String path) throws IOException {
		InputStream is = null;
		
		try {
			is = ImageUtil.class.getResourceAsStream(path);
			Image image = Image.createImage(is);
			
			return image;
		}
		finally {
			if (is != null) {
				is.close();
				is = null;
			}
		}
	}
	
//	public static Image createImageFromClassPath(String path) throws IOException {
//		InputStream is = null;
//		ByteArrayOutputStream baos = null;
//		
//		try {
//			is = ImageUtil.class.getResourceAsStream(path);
//			baos = new ByteArrayOutputStream();
//			byte[] buffer = new byte[4096];
//			int length = 0;
//			while ((length = is.read(buffer)) != -1) {
//				baos.write(buffer, 0, length);
//			}
//			byte[] bytes = baos.toByteArray();
//			
//			Log.info("loadImageFromClassPath: " + path + ", length: " + (bytes.length));
//			Image image = Image.createImage(bytes, 0, bytes.length);
//			
//			return image;
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
