/**
 * Copyright (C) 2007-2009 Alexis Kinsella - http://www.helyx.org - <Helyx.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.helyx.app.j2me.getabike.lib.ui.util;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.getabike.lib.ui.util.ImageUtil;

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
