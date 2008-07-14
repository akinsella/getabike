package org.helyx.app.j2me.lib.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.helyx.app.j2me.lib.log.Log;

public class StreamUtil {
	
	private static final String CAT = "STREAM_UTIL";
	
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
			Log.warn(CAT, t);
		}
		
		return false;
	}
	
}
