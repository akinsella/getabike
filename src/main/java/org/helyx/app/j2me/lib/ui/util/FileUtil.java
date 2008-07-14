package org.helyx.app.j2me.lib.ui.util;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

public class FileUtil {

	private static final String CAT = "FILE_UTIL";
	
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
