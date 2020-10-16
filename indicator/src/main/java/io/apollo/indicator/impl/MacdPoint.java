package io.apollo.indicator.impl;

import io.apollo.indicator.impl.base.FixedPeriodPoint;
import io.gemini.definition.market.data.impl.BasicMarketData;
import io.gemini.definition.market.vector.TimePeriodSerial;

public final class MacdPoint extends FixedPeriodPoint<BasicMarketData> {

	private MacdPoint(int index, TimePeriodSerial timePeriod) {
		super(index, timePeriod);
	}

	@Override
	protected void handleMarketData0(BasicMarketData marketData) {
		// TODO Auto-generated method stub

	}

}
