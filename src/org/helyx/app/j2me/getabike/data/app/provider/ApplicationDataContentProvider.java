package org.helyx.app.j2me.velocite.data.app.provider;

import java.io.InputStream;

import org.helyx.app.j2me.velocite.data.app.domain.ConfigurationMetadata;
import org.helyx.app.j2me.velocite.data.app.domain.Version;
import org.helyx.app.j2me.velocite.data.app.domain.VersionRange;
import org.helyx.app.j2me.velocite.data.city.CityConstants;
import org.helyx.app.j2me.velocite.data.city.domain.Quartier;
import org.helyx.helyx4me.constant.EncodingConstants;
import org.helyx.helyx4me.content.accessor.IContentAccessor;
import org.helyx.helyx4me.content.provider.AbstractContentProvider;
import org.helyx.helyx4me.content.provider.exception.ContentProviderException;
import org.helyx.helyx4me.stream.InputStreamProvider;
import org.helyx.helyx4me.task.EventType;
import org.helyx.helyx4me.xml.xpp.XppAttributeProcessor;
import org.helyx.helyx4me.xml.xpp.XppUtil;
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
