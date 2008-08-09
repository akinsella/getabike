package org.helyx.app.j2me.lib.i18n;

import java.io.InputStream;

import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.app.j2me.velocite.data.carto.CartoConstants;
import org.xmlpull.v1.XmlPullParser;


public class XmlResourceBundleContentProvider extends AbstractContentProvider {
	
	private static final String CAT = "XML_RESOURCE_BUNDLE_CONTENT_PROVIDER";

	private static final String ENTRY = "entry";
	
	private static final String KEY = "key";
	private static final String MESSAGE = "message";
	
	private boolean cancel = false;

	protected IContentAccessor messageContentAccessor;

	public XmlResourceBundleContentProvider(IContentAccessor messageContentAccessor) {
		super();
		this.messageContentAccessor = messageContentAccessor;
	}


	public void loadData() {
		
		Log.debug(CAT, "Loading messages ...");
		
		InputStream inputStream = null;
		InputStreamProvider cartoInputStreamProvider = null;
		
		ResourceBundle resourceBundle = null;
		try {

			progressDispatcher.fireEvent(ProgressEventType.ON_START);
			try {
				
				cartoInputStreamProvider = messageContentAccessor.getInputStreamProvider();
				inputStream = cartoInputStreamProvider.createInputStream();
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);

				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { KEY, MESSAGE });

				resourceBundle = new ResourceBundle();
				while (XppUtil.readToNextElement(xpp, ENTRY)) {
					if (cancel) {
						progressDispatcher.fireEvent(ProgressEventType.ON_CANCEL);
						return ;
					}
					xppAttributeProcessor.processNode(xpp);

					String key = xppAttributeProcessor.getAttrValueAsString(KEY);
					String message = xppAttributeProcessor.getAttrValueAsString(MESSAGE);
					
					resourceBundle.put(key, message);
				}
						
				progressDispatcher.fireEvent(CartoConstants.ON_STATION_LOADED, resourceBundle);
			}
			finally {
				cartoInputStreamProvider.dispose();
			}
			progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS, resourceBundle);
		}
		catch (Throwable t) {
    		Log.warn(CAT, t);
			progressDispatcher.fireEvent(ProgressEventType.ON_ERROR, t);
		}
	}

	public void cancel() {
		cancel = true;
	}

	public String getCat() {
		return CAT;
	}
	
	public String getDescription() {
		return "Fetchs messages from path: '" + messageContentAccessor.getPath() + "'";
	}


}
