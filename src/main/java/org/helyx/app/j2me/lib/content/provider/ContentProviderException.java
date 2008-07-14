package org.helyx.app.j2me.lib.content.provider;


public class ContentProviderException extends Exception {

	public ContentProviderException() {
		super();
	}

	public ContentProviderException(String message) {
		super(message);
	}

	public ContentProviderException(Throwable throwable) {
		super(throwable == null ? null : throwable.getMessage());
	}


}
