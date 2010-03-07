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
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

import org.helyx.logging4me.Logger;


public class FileUtil {

	private static final Logger logger = Logger.getLogger("FILE_UTIL");
	
	private FileUtil() {
		super();
	}
	
	public static boolean isApiAvailable() {
		boolean isAPIAvailable = false;
		
	    if (System.getProperty("microedition.io.file.FileConnection.version") != null) {
	      isAPIAvailable = true;
	    }
	    
	    return isAPIAvailable;
	}
	
	public static String findFirstRoot() {
		Enumeration _enum = FileSystemRegistry.listRoots();
		if (_enum == null) {
			return null;
		}
		
		String firstRoot = (String)_enum.nextElement();
		
		return firstRoot;
	}
	
	public static FileConnection openFileConnection(String rootPath, String filePath, int mode) throws IOException {
		FileConnection fc = (FileConnection)Connector.open("file:///" + rootPath + "/" + filePath, mode);
		return fc;
	}

}
