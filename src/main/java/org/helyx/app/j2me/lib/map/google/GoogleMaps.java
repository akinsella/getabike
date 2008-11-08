package org.helyx.app.j2me.lib.map.google;

import java.io.InputStream;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.lib.content.accessor.HttpContentAccessor;
import org.helyx.app.j2me.lib.dfp.dfp;
import org.helyx.app.j2me.lib.dfp.dfpdec;
import org.helyx.app.j2me.lib.dfp.dfpmath;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.math.MathUtil;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.stream.StreamUtil;
import org.helyx.app.j2me.velocite.data.carto.domain.Point;

public class GoogleMaps {
	
	private String apiKey = null;
	
	private static final Log log = LogFactory.getLog("GOOGLE_MAPS");

	// these 2 properties will be used with map scrolling methods. You can
	// remove them if not needed
	private int offset = 268435456;
	private double radius = offset / Math.PI;

	public GoogleMaps(String apiKey) {
		this.apiKey = apiKey;
	}

	public Image retrieveStaticImage(int width, int height, double lat, double lng, int zoom, String format) throws Exception {
		byte[] imageData = loadHttpFile(getMapUrl(width, height, lng, lat, zoom, format));

		return Image.createImage(imageData, 0, imageData.length);
	}

	String getGeocodeError(int errorCode) {
		switch (errorCode) {
			case 400:
				return "Bad request";
			case 500:
				return "Server error";
			case 601:
				return "Missing query";
			case 602:
				return "Unknown address";
			case 603:
				return "Unavailable address";
			case 604:
				return "Unknown directions";
			case 610:
				return "Bad API key";
			case 620:
				return "Too many queries";
			default:
				return "Generic error";
		}
	}

	String getGeocodeUrl(String address) {
		return "http://maps.google.com/maps/geo?q=" + urlEncode(address) + "&output=csv&key=" + apiKey;
	}

	String getMapUrl(int width, int height, double lng, double lat, int zoom, String format) {
		return new StringBuffer("http://maps.google.com/staticmap?center=")
			.append(lat).append(",").append(lng)
			.append("&format=").append(format)
			.append("&zoom=").append(zoom)
			.append("&size=").append(width).append("x").append(height)
			.append("&key=").append(apiKey)
			.toString();
	}

	String urlEncode(String str) {
		StringBuffer buf = new StringBuffer();
		char c;
		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
				buf.append(c);
			}
			else {
				buf.append("%").append(Integer.toHexString((int)str.charAt(i)));
			}
		}
		return buf.toString();
	}

	byte[] loadHttpFile(String url) throws Exception {
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

	static String[] split(String s, int chr) {
		Vector res = new Vector();

		int curr = 0;
		int prev = 0;

		while ((curr = s.indexOf(chr, prev)) >= 0) {
			res.addElement(s.substring(prev, curr));

			prev = curr + 1;
		}
		res.addElement(s.substring(prev));

		String[] splitted = new String[res.size()];

		res.copyInto(splitted);

		return splitted;
	}

	public Point geocodeAddress(String address) throws Exception
	{
		byte[] res = loadHttpFile(getGeocodeUrl(address));
		
		String resString = new String(res, 0, res.length);
		
		String[] data = split(resString, ',');
		
		if(data[0].compareTo("200") != 0)
		{
			int errorCode = Integer.parseInt(data[0]);
			
			throw new Exception("Google Maps Exception: " + getGeocodeError(errorCode));
		}
		else
		{
			return new Point(
				Double.parseDouble(data[3]),
				Double.parseDouble(data[2]));
		}
	}

	
	public Point adjust(Point localization, int deltaX, int deltaY, int zoom) {
		return new Point(
			YToL(LToY(localization.lng) + (deltaX << (21 - zoom))),		
			XToL(LToX(localization.lat) + (deltaY << (21 - zoom)))
		);
	}
	
	private double LToX(double x) {
		return offset + radius * x * Math.PI / 180;
	}
	 
	double XToL(double x) {
		return ((x - offset) / radius) * 180 / Math.PI;
	}
	 
	double LToY(double y) {
		double tmp = Math.sin(y * Math.PI / 180);
		return offset - radius * MathUtil.ln((1 + tmp) / (1 - tmp)) / 2;
	}
	
	double YToL(double y) {
		dfp exp = dfpmath.exp(new dfp(String.valueOf((y - offset) / radius)));
		dfp atan = dfpmath.atan(exp);
		double dblAtan = Double.parseDouble(atan.toString());
		return (Math.PI / 2 - 2 * MathUtil.atan((y - offset) / radius)) * 180 / Math.PI;
	}

}