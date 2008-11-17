package org.helyx.app.j2me.velocite.data.carto.provider;

import org.helyx.app.j2me.lib.content.provider.AbstractContentProvider;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.DefaultStationInfoNormalizer;
import org.helyx.app.j2me.velocite.data.carto.provider.normalizer.IStationInfoNormalizer;

public abstract class AbstractStationContentProvider extends AbstractContentProvider {

	public static final IStationInfoNormalizer DEFAULT_STATION_INFO_NORMALIZER = new DefaultStationInfoNormalizer();
	
	private IStationInfoNormalizer stationInfoNormalizer = DEFAULT_STATION_INFO_NORMALIZER;

	public AbstractStationContentProvider() {
		super();
	}

	public AbstractStationContentProvider(IStationInfoNormalizer stationInfoNormalizer) {
		super();
		this.stationInfoNormalizer = stationInfoNormalizer;
	}

	public IStationInfoNormalizer getStationInfoNormalizer() {
		if (stationInfoNormalizer == null) {
			return DEFAULT_STATION_INFO_NORMALIZER;
		}
		return stationInfoNormalizer;
	}

	public void setStationInfoNormalizer(IStationInfoNormalizer stationInfoNormalizer) {
		this.stationInfoNormalizer = stationInfoNormalizer;
	}

}
