package org.helyx.app.j2me.lib.map.google;

import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.log.LogFactory;
import org.helyx.app.j2me.lib.math.DistanceUtil;
import org.helyx.app.j2me.lib.math.MathUtil;
import org.helyx.app.j2me.lib.text.TextUtil;
import org.helyx.app.j2me.lib.util.HttpUtil;
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
		byte[] imageData = HttpUtil.loadAsBytes(getMapUrl(width, height, lng, lat, zoom, format));

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
		return "http://maps.google.com/maps/geo?q=" + TextUtil.urlEncode(address) + "&output=csv&key=" + apiKey;
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

	public Point geocodeAddress(String address) throws Exception
	{
		byte[] res = HttpUtil.loadAsBytes(getGeocodeUrl(address));
		
		String resString = new String(res, 0, res.length);
		
		String[] data = TextUtil.split(resString, ',');
		
		if(data[0].compareTo("200") != 0)
		{
			int errorCode = Integer.parseInt(data[0]);
			
			throw new Exception("Google Maps Exception: " + getGeocodeError(errorCode));
		}
		else {
			return new Point(
				Double.parseDouble(data[3]),
				Double.parseDouble(data[2]));
		}
	}
	
	public Point adjust(Point localization, int deltaX, int deltaY, int zoom) {
		return new Point(
			DistanceUtil.YToL(DistanceUtil.LToY(localization.lng) + (deltaX << (21 - zoom))),		
			DistanceUtil.XToL(DistanceUtil.LToX(localization.lat) + (deltaY << (21 - zoom)))
		);
	}

}