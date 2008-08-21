package org.helyx.app.j2me.lib.log;

import java.io.PrintStream;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.helyx.app.j2me.lib.ui.util.FileUtil;

public class FileLogWriter extends AbstractLogWriter {
	
	private static final Log log = LogFactory.getLog("FILE_LOG_WRITER");

	private String filePath;
	private FileConnection fc;
	private PrintStream ps;
	
	public FileLogWriter(String filePath) {
		super();
		this.filePath = filePath;
	}

	public void onWrite(int level, Log log, String message, Date date) {
		try {
			if (ps != null) {
				ps.print(getLogMessage(level, log, message, date) + "\r\n");
			}
		}
		catch(Exception e) { 
			e.printStackTrace(); 
		}
	}

	public void open() throws Exception {
		String firstRootPath = FileUtil.findFirstRoot();
		log.info("First root path: " + firstRootPath);
		fc = FileUtil.openFileConnection(firstRootPath, filePath, Connector.READ_WRITE);
		
		log.info("Log File path: " + fc.getPath());
		log.info("Log File url: " + fc.getURL());
		boolean fileExists = fc.exists();
		
		log.info(fileExists ? "Log file already exists" : "Log file does not exist");

		if (!fileExists) {
			log.info("Creating file");
			fc.create();
		}

		log.info(fc.canWrite() ? "Log file is writable" : "Log file is not writable");

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
