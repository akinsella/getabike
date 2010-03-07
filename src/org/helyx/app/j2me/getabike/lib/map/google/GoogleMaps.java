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
package org.helyx.app.j2me.getabike.lib.map.google;

import javax.microedition.lcdui.Image;

import org.helyx.app.j2me.getabike.lib.localization.Point;
import org.helyx.app.j2me.getabike.lib.map.google.MapTypeConstants;
import org.helyx.app.j2me.getabike.lib.math.DistanceUtil;
import org.helyx.app.j2me.getabike.lib.text.TextUtil;
import org.helyx.app.j2me.getabike.lib.util.HttpUtil;
import org.helyx.logging4me.Logger;


public class GoogleMaps {
	
	private String apiKey = null;
	
	private static final Logger logger = Logger.getLogger("GOOGLE_MAPS");

	// these 2 properties will be used with map scrolling methods. You can
	// remove them if not needed
	private int offset = 268435456;
	private double radius = offset / Math.PI;
	
	private String mapType = MapTypeConstants.ROADMAP;
	
	private boolean mobile = true;

	public GoogleMaps(String apiKey) {
		this.apiKey = apiKey;
	}

	public Image retrieveStaticImage(int width, int height, double lat, double lng, int zoom, String format) throws Exception {
		long startTime = System.currentTimeMillis();

		byte[] imageData = HttpUtil.loadAsBytes(getMapUrl(width, height, lng, lat, zoom, format));
		Image googleMapsImage = Image.createImage(imageData, 0, imageData.length);
		
		long endTime = System.currentTimeMillis();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Google Maps Image loaded in " + Math.max(0, endTime - startTime) + " ms");
		}
		
		return googleMapsImage;
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
		String googleMapsUrl = new StringBuffer("http://maps.google.com/staticmap?center=")
			.append(lat).append(",").append(lng)
			.append("&format=").append(format)
			.append("&zoom=").append(zoom)
			.append("&size=").append(width).append("x").append(height)
			.append("&key=").append(apiKey)
			.append("&sensor=false")
			.append("&maptype=").append(mapType)
			.append("&mobile=").append(mobile ? "true" : "false")
			.toString();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Google maps URL: '" + googleMapsUrl + "'");
		}
		
		return googleMapsUrl;
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
			DistanceUtil.XToL(DistanceUtil.LToX(localization.lng) + (deltaX << (21 - zoom))),		
			DistanceUtil.YToL(DistanceUtil.LToY(localization.lat) + (deltaY << (21 - zoom)))
		);
	}

}