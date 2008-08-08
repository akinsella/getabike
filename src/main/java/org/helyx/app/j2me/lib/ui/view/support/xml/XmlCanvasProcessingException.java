package org.helyx.app.j2me.lib.ui.view.support.xml;

import org.helyx.app.j2me.lib.exception.NestedException;

public class XmlCanvasProcessingException extends NestedException {

	public XmlCanvasProcessingException() {
		super();
	}

	public XmlCanvasProcessingException(String message) {
		super(message);
	}

	public XmlCanvasProcessingException(Throwable throwable) {
		super(throwable);
	}

	public XmlCanvasProcessingException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
