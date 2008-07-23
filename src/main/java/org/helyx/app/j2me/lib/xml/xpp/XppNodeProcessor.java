package org.helyx.app.j2me.lib.xml.xpp;

import org.helyx.app.j2me.lib.ui.view.xml.XmlCanvasProcessingException;
import org.xmlpull.v1.XmlPullParser;

public interface XppNodeProcessor {

	void processNodeStart(XmlPullParser xpp) throws XmlCanvasProcessingException;
	void processNodeEnd(XmlPullParser xpp) throws XmlCanvasProcessingException;
	
}
