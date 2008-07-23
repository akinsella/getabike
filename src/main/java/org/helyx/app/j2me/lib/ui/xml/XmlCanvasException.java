package org.helyx.app.j2me.lib.ui.xml;

public class XmlCanvasException extends Exception {

	public XmlCanvasException() {
		super();
	}

	public XmlCanvasException(String s) {
		super(s);
	}

	public XmlCanvasException(Throwable throwable) {
		super(throwable == null ? null : throwable.getMessage());
	}

}
