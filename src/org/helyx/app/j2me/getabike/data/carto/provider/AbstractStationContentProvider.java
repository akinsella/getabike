package org.helyx.app.j2me.getabike.data.carto.provider;

import org.helyx.app.j2me.getabike.data.carto.provider.normalizer.IStationInfoNormalizer;
import org.helyx.app.j2me.getabike.lib.content.provider.AbstractContentProvider;

public abstract class AbstractStationContentProvider extends AbstractContentProvider {

	private IStationInfoNormalizer stationInfoNormalizer;

	public AbstractStationContentProvider() {
		super();
	}

	public AbstractStationContentProvider(IStationInfoNormalizer stationInfoNormalizer) {
		super();
		this.stationInfoNormalizer = stationInfoNormalizer;
	}

	public IStationInfoNormalizer getStationInfoNormalizer() {
		return stationInfoNormalizer;
	}

	public void setStationInfoNormalizer(IStationInfoNormalizer stationInfoNormalizer) {
		this.stationInfoNormalizer = stationInfoNormalizer;
	}

}
