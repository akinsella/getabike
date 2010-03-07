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
package org.helyx.app.j2me.getabike.lib.util;

public class SystemProperties {
	  private static final String[] SYSPROPLISTS = {
	      "CLDC&MIDP", "Optional packages", "MMAPI", "Bluetooth API", 
	      "FileConnection API", "WMA", "SATSA", "Other"};
	  private static final String[] CLDC_MIDPPROPS = {
	      "microedition.profiles", "microedition.configuration", "microedition.locale",
	      "microedition.platform", "microedition.encoding", "microedition.commports",
	      "microedition.hostname", "microedition.jtwi.version"};
	  private static final String[] OPT_SYSPROPS = {
	      "microedition.media.version", "microedition.pim.version", 
	      "microedition.io.file.FileConnection.version", "microedition.m3g.version", 
	      "microedition.location.version", "microedition.global.version",
	      "microedition.chapi.version", "microedition.sip.version"};
	  private static final String[] MMAPI_SYSPROPS = {
	      "supports.mixing", "supports.audio.capture", "supports.video.capture",
	      "supports.recording", "audio.encodings", "video.encodings",
	      "video.snapshot.encodings", "streamable.contents"};
	  private static final String[] BT_SYSPROPS = {
		  "bluetooth.api.version", "bluetooth.l2cap.receiveMTU.max", 
		  "bluetooth.connected.devices.max", "bluetooth.connected.inquiry", 
		  "bluetooth.connected.page", "bluetooth.connected.inquiry.scan", 
		  "bluetooth.connected.page.scan", "bluetooth.master.switch", 
		  "bluetooth.sd.trans.max", "bluetooth.sd.attr.retrievable.max"};
	  private static final String[] FILE_API_SYSPROPS = { "fileconn.dir.photos",
	      "fileconn.dir.videos", "fileconn.dir.tones", "fileconn.dir.memorycard",
	      "fileconn.dir.private", "fileconn.dir.photos.name", "fileconn.dir.videos.name",
	      "fileconn.dir.tones.name", "fileconn.dir.memorycard.name", "file.separator"};
	  private static final String[] WMA_SYSPROPS = {
	      "wireless.messaging.sms.smsc"};
	  private static final String[] SATSA_SYSPROPS = {
	      "microedition.smartcardslots"};
	  private static final String[] OTHER_SYSPROPS = {
	      "com.nokia.mid.dateformat", "com.nokia.mid.timeformat",
	      "com.nokia.network.access", "com.nokia.mid.imei"};
}
