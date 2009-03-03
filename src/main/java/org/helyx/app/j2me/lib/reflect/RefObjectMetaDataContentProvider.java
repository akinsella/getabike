package org.helyx.app.j2me.lib.reflect;

import java.io.InputStream;

import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.content.provider.exception.ContentProviderException;
import org.helyx.app.j2me.lib.logger.Logger;
import org.helyx.app.j2me.lib.logger.LoggerFactory;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.EventType;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.basics4me.io.BufferedInputStream;
import org.xmlpull.v1.XmlPullParser;

public class RefObjectMetaDataContentProvider extends AbstractContentProvider {
	
	private static final Logger logger = LoggerFactory.getLogger("REF_OBJECT_METADATA_CONTENT_PROVIDER");
	
	private static final String INDEX = "index";
	private static final String NAME = "name";
	private static final String TYPE = "type";
	
	private static final String METADATA = "metadata";
	private static final String FIELD = "field";
		

	private static final String INVALID_CONTENT = "Xml content is invalid";

	private IContentAccessor metadataContentAccessor;


	public RefObjectMetaDataContentProvider(IContentAccessor metadataContentAccessor) {
		super();
		this.metadataContentAccessor = metadataContentAccessor;
	}

	public void loadData() {
		
		logger.debug("Loading metadata info ...");
		
		InputStream inputStream = null;
		InputStreamProvider metadataInputStreamProvider = null;
		
		try {
			progressDispatcher.fireEvent(EventType.ON_START);
			
			RefObjectMetaData refObjectMetaData = new RefObjectMetaData();
			try {			
				metadataInputStreamProvider = metadataContentAccessor.getInputStreamProvider();
				inputStream = new BufferedInputStream(metadataInputStreamProvider.createInputStream());
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream,  EncodingConstants.UTF_8);
	
				logger.debug("Parsing XML Document: '" + metadataContentAccessor.getPath() + "'");
				
				if (!XppUtil.readToNextElement(xpp, METADATA)) {
					throw new ContentProviderException(INVALID_CONTENT);
				}
				
				for (int i = 0 ; i < xpp.getAttributeCount() ; i++) {
					String attributeName = xpp.getAttributeName(i);
					if (attributeName.equals(NAME)) {
						refObjectMetaData.setObjectName(xpp.getAttributeValue(i));
					}
				}
				
				int count = 0;
				while (XppUtil.readNextElement(xpp) && xpp.getName().equals(FIELD)) {

					FieldInfo fieldInfo = new FieldInfo();
					for (int i = 0 ; i < xpp.getAttributeCount() ; i++) {
						String attributeName = xpp.getAttributeName(i);
						if (attributeName.equals(INDEX)) {
							fieldInfo.index = Integer.parseInt(xpp.getAttributeValue(i));
						}
						else if (attributeName.equals(NAME)) {
							fieldInfo.name = xpp.getAttributeValue(i);
						}
						else if (attributeName.equals(TYPE)) {
							fieldInfo.type = FieldType.getIndex(xpp.getAttributeValue(i));
						}
					}
					
					refObjectMetaData.addFieldInfo(fieldInfo);
					
	
					++count;
					if (count % 10 == 0) {
//						logger.debug("COUNT: " + String.valueOf(count));
					}

				}
			}
			finally {
				metadataInputStreamProvider.dispose();
			}
			progressDispatcher.fireEvent(EventType.ON_SUCCESS, refObjectMetaData);
		}
		catch (Throwable t) {
    		logger.warn(t);
			progressDispatcher.fireEvent(EventType.ON_ERROR, t);
		}
	}

	public String getDescription() {
		return "Fetchs Object Metadata from path: '" + metadataContentAccessor.getPath() + "'";
	}

}
