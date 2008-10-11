package org.helyx.app.j2me.lib.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;

public class StreamUtil {
	
	private static final Log log = LogFactory.getLog("STREAM_UTIL");
	
	private static final byte[] ALPHABET = "abcdefghijklmnopkrstuvwxyz".getBytes();
	
	public static InputStream createsFromClassPath(String path) throws IOException {
		return StreamUtil.class.getResourceAsStream(path);
	}

	public static boolean supportEncoding(String encoding) {
		
		try {
			InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(ALPHABET), encoding);
			try {
				isr.read();
				return true;
			}
			finally {
				if (isr != null) {
					isr.close();
				}
			}
		}
		catch(Throwable t) {
			log.warn(t);
		}
		
		return false;
	}

	public static String readStream(InputStream inputStream, boolean b) throws IOException {
		int length = -1;
		StringBuffer sb = new StringBuffer();
		byte[] bytes = new byte[1024];
		while((length = inputStream.read(bytes)) >= 0) {
			if (length == 0) {
				continue;
			}
			String line = new String(bytes);
			sb.append(line);
		}
		String content = sb.toString();
		
		return content;
	}
	
}
