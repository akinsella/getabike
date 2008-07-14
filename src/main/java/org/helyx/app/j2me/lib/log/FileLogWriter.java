package org.helyx.app.j2me.lib.log;

import java.io.PrintStream;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.helyx.app.j2me.lib.format.DateFormatUtil;
import org.helyx.app.j2me.lib.ui.util.FileUtil;

public class FileLogWriter implements ILogWriter {
	
	private static final String CAT = "FILE_LOG_WRITER";

	private String filePath;
	private FileConnection fc;
	private PrintStream ps;
	
	public FileLogWriter(String filePath) {
		super();
		this.filePath = filePath;
	}

	public void write(int level, String category, String message, Date date) {
		try {
		
			String dateStr = DateFormatUtil.formatDate(date);
			
			StringBuffer sb = new StringBuffer().append("[").append(Log.getLevelName(level)).append(" - ").append(category).append(" - ").append(dateStr).append("] ").append(message);

			if (ps != null) {
				ps.print(sb.toString() + "\r\n");
			}
		}
		catch(Exception e) { 
			e.printStackTrace(); 
		}
	}

	public void open() throws Exception {
		String firstRootPath = FileUtil.findFirstRoot();
		Log.info(CAT, "First root path: " + firstRootPath);
		fc = FileUtil.openFileConnection(firstRootPath, filePath, Connector.READ_WRITE);
		
		Log.info(CAT, "Log File path: " + fc.getPath());
		Log.info(CAT, "Log File url: " + fc.getURL());
		boolean fileExists = fc.exists();
		
		Log.info(CAT, fileExists ? "Log file already exists" : "Log file does not exist");

		if (!fileExists) {
			Log.info(CAT, "Creating file");
			fc.create();
		}

		Log.info(CAT, fc.canWrite() ? "Log file is writable" : "Log file is not writable");

		ps = new PrintStream(fc.openOutputStream());
	}

	public void close() throws Exception {
		if (ps != null) {
			try {
				ps.close();
			}
			catch(Throwable t) {
				t.printStackTrace();
			}
		}
		if (fc != null) {
			try {
				fc.close();
			}
			catch(Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public void flush() throws Exception {
		if (ps != null) {
			ps.flush();
		}
	}

}
