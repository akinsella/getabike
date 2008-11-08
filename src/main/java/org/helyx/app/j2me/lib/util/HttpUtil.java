package org.helyx.app.j2me.lib.util;

import java.io.InputStream;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.stream.StreamUtil;

public class HttpUtil {

	private static final Log log = LogFactory.getLog("HTTP_UTIL");
	
	public static byte[] loadAsBytes(String url) throws Exception {
		InputStream is = null;
		try {
			InputStreamProvider isp = new HttpContentAccessor(url).getInputStreamProvider();
			is = isp.createInputStream(true);
			byte[] byteBuffer = StreamUtil.readStreamBinary(is, false);
			
			return byteBuffer;
		}
		finally {
			if (is != null) {
				is.close();
			}
		}		
	}

}
