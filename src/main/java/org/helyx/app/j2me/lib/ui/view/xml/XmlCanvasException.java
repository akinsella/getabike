package org.helyx.app.j2me.lib.ui.view.xml;

import org.helyx.app.j2me.lib.exception.NestedException;

public class XmlCanvasException extends NestedException {

	public XmlCanvasException() {
		super();
	}

	public XmlCanvasException(String s) {
		super(s);
	}

	public XmlCanvasException(Throwable throwable) {
		super(throwable);
	}

	public XmlCanvasException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
