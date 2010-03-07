package org.helyx.app.j2me.getabike.data.app.provider;

import java.io.InputStream;

import org.helyx.app.j2me.getabike.data.app.domain.ConfigurationMetadata;
import org.helyx.app.j2me.getabike.data.app.domain.Version;
import org.helyx.app.j2me.getabike.data.app.domain.VersionRange;
import org.helyx.app.j2me.getabike.lib.constant.EncodingConstants;
import org.helyx.app.j2me.getabike.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.getabike.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.getabike.lib.content.provider.exception.ContentProviderException;
import org.helyx.app.j2me.getabike.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.getabike.lib.task.EventType;
import org.helyx.app.j2me.getabike.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.getabike.lib.xml.xpp.XppUtil;
import org.helyx.logging4me.Logger;
import org.xmlpull.v1.XmlPullParser;

public class ApplicationDataContentProvider extends AbstractContentProvider {

	private static final Logger logger = Logger.getLogger("APPLICATION_DATA_CONTENT_PROVIDER");
	
	private static final String INVALID_CONTENT = "Xml content is invalid";
	private static final String APPLICATION = "application";
	private static final String UID = "uid";
	private static final String VERSION = "version";
	private static final String MIN = "min";
	private static final String MAX = "max";
	private static final String URL = "url";
	
	private boolean cancel = false;

	private IContentAccessor applicationDataContentAccessor;

	public ApplicationDataContentProvider(IContentAccessor applicationDataContentAccessor) {
		super();
		this.applicationDataContentAccessor = applicationDataContentAccessor;
	}

	public void loadData() {
		
		logger.debug("Loading application data ...");
		
		InputStream inputStream = null;
		InputStreamProvider applicationDataInputStreamProvider = null;
		try {
			ConfigurationMetadata applicationMetaData = null;
			progressDispatcher.fireEvent(EventType.ON_START);
			try {			
				applicationDataInputStreamProvider = applicationDataContentAccessor.getInputStreamProvider();
				inputStream = applicationDataInputStreamProvider.createInputStream(true);
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);
				
				if (!XppUtil.readToNextElement(xpp, APPLICATION)) {
					throw new ContentProviderException(INVALID_CONTENT);
				}
	
				XppAttributeProcessor applicationXppAttributeProcessor = new XppAttributeProcessor();
				applicationXppAttributeProcessor.addAll(new String[] { UID });
				applicationXppAttributeProcessor.processNode(xpp);
				
				String applicationUid = applicationXppAttributeProcessor.getAttrValueAsString(UID);

				
				applicationMetaData = new ConfigurationMetadata(applicationUid);

				while (XppUtil.readToNextElement(xpp, VERSION)) {
					if (cancel) {
						progressDispatcher.fireEvent(EventType.ON_CANCEL);
						return ;
					}
					XppAttributeProcessor versionXppAttributeProcessor = new XppAttributeProcessor();
					versionXppAttributeProcessor.addAll(new String[] { MIN, MAX, URL });
					versionXppAttributeProcessor.processNode(xpp);
					
					String minVersionStr = versionXppAttributeProcessor.getAttrValueAsString(MIN);
					Version minVersion = new Version(minVersionStr);

					String maxVersionStr = versionXppAttributeProcessor.getAttrValueAsString(MAX);
					Version maxVersion = null;
					if (maxVersionStr != null && maxVersionStr.trim().length() > 0) {
						maxVersion = new Version(maxVersionStr);
					}
					
					String propertiesUrl = versionXppAttributeProcessor.getAttrValueAsString(URL);
					
					VersionRange versionRange = new VersionRange(minVersion, maxVersion, propertiesUrl);

					
					applicationMetaData.addVersionRange(versionRange);		
				}
			}
			finally {
				applicationDataInputStreamProvider.dispose();
			}		
			progressDispatcher.fireEvent(EventType.ON_SUCCESS, applicationMetaData);
		}
		catch (Throwable t) {
    		logger.warn(t);
			progressDispatcher.fireEvent(EventType.ON_ERROR, t);
		}
		
	}

	public void cancel() {
		cancel = true;
	}

	public String getDescription() {
		return "Fetchs application data from path: '" + applicationDataContentAccessor.getPath() + "'";
	}


}
