package org.helyx.app.j2me.lib.ui.view.xml;

public class XmlCanvasProcessingException extends Exception {

	public XmlCanvasProcessingException() {
		super();
	}

	public XmlCanvasProcessingException(String s) {
		super(s);
	}

	public XmlCanvasProcessingException(Throwable throwable) {
		super(throwable == null ? null : throwable.getMessage());
	}

}
