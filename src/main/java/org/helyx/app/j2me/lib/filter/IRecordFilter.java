package org.helyx.app.j2me.lib.filter;

import java.io.DataInputStream;
import java.io.IOException;

public interface IRecordFilter {

	boolean matches(DataInputStream dis) throws IOException;
	
}
