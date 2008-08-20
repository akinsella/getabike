package org.helyx.app.j2me.velocite.data.language.provider;

import java.io.InputStream;
import java.util.Vector;

import org.helyx.app.j2me.lib.constant.EncodingConstants;
import org.helyx.app.j2me.lib.content.accessor.IContentAccessor;
import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.lib.content.provider.ContentProviderException;
import org.helyx.app.j2me.lib.log.Log;
import org.helyx.app.j2me.lib.pref.PrefManager;
import org.helyx.app.j2me.lib.stream.InputStreamProvider;
import org.helyx.app.j2me.lib.task.ProgressEventType;
import org.helyx.app.j2me.lib.xml.xpp.XppAttributeProcessor;
import org.helyx.app.j2me.lib.xml.xpp.XppUtil;
import org.helyx.app.j2me.velocite.PrefConstants;
import org.helyx.app.j2me.velocite.data.language.LanguageConstants;
import org.helyx.app.j2me.velocite.data.language.domain.Language;
import org.helyx.basics4me.io.BufferedInputStream;
import org.xmlpull.v1.XmlPullParser;

public class DefaultLanguageContentProvider extends AbstractContentProvider {
	
	private static final String CAT = "DEFAULT_CITY_CONTENT_PROVIDER";


	private static final String LANGUAGES = "languages";
	private static final String LANGUAGE = "language";

	private static final String KEY = "key";
	private static final String NAME = "name";
	private static final String COUNTRY = "country";
	private static final String DEFAULT = "default";
	private static final String LOCALE_COUNTRY = "localeCountry";
	private static final String LOCALE_LANGUAGE = "localeLanguage";
	private static final String INVALID_CONTENT = "Xml content is invalid";
	
	private boolean cancel = false;

	private IContentAccessor languageContentAccessor;

	public DefaultLanguageContentProvider() {
		super();
	}

	public DefaultLanguageContentProvider(IContentAccessor languageContentAccessor) {
		super();
		this.languageContentAccessor = languageContentAccessor;
	}


	public void loadData() {
		
		Log.debug(CAT, "Loading languages informations ...");
		
		InputStream inputStream = null;
		InputStreamProvider languageInputStreamProvider = null;
		
		try {
			progressDispatcher.fireEvent(ProgressEventType.ON_START);
			try {			
				languageInputStreamProvider = languageContentAccessor.getInputStreamProvider();
				inputStream = new BufferedInputStream(languageInputStreamProvider.createInputStream());
				
				XmlPullParser xpp = XppUtil.createXpp(inputStream, EncodingConstants.UTF_8);
				
				if (!XppUtil.readToNextElement(xpp, LANGUAGES)) {
					throw new ContentProviderException(INVALID_CONTENT);
				}
	
				XppAttributeProcessor xppAttributeProcessor0 = new XppAttributeProcessor();
				xppAttributeProcessor0.addAll(new String[] { DEFAULT });
				xppAttributeProcessor0.processNode(xpp);

				if (!xppAttributeProcessor0.attrExists(DEFAULT)) {
					PrefManager.removePref(PrefConstants.LANGUAGE_DEFAULT_KEY);
				}
				else {
					String defaultLanguageKey = xppAttributeProcessor0.getAttrValueAsString(DEFAULT);
					Log.debug(CAT, "Default language key: '" + defaultLanguageKey + "'");
					PrefManager.writePref(PrefConstants.LANGUAGE_DEFAULT_KEY, defaultLanguageKey);					
				}
				
				XppAttributeProcessor xppAttributeProcessor = new XppAttributeProcessor();
				xppAttributeProcessor.addAll(new String[] { 
					KEY, NAME, COUNTRY, LOCALE_LANGUAGE, LOCALE_COUNTRY
				});

				Vector languageList = new Vector();
				while (XppUtil.readToNextElement(xpp, LANGUAGE)) {
					if (cancel) {
						progressDispatcher.fireEvent(ProgressEventType.ON_CANCEL);
						return ;
					}
					Language language = new Language();
					xppAttributeProcessor.processNode(xpp);

					language.key = xppAttributeProcessor.getAttrValueAsString(KEY);
					language.name = xppAttributeProcessor.getAttrValueAsString(NAME);
					language.country = xppAttributeProcessor.getAttrValueAsString(COUNTRY);
					language.localeCountry = xppAttributeProcessor.getAttrValueAsString(LOCALE_COUNTRY);
					language.localeLanguage = xppAttributeProcessor.getAttrValueAsString(LOCALE_LANGUAGE);
	
					languageList.addElement(language);
					progressDispatcher.fireEvent(LanguageConstants.ON_LANGUAGE_LOADED, language);				
				}
				progressDispatcher.fireEvent(ProgressEventType.ON_SUCCESS, languageList);
			}
			finally {
				languageInputStreamProvider.dispose();
			}
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
		return "Fetchs language configurations from path: '" + languageContentAccessor.getPath() + "'";
	}

}
