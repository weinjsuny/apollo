package io.mercury.financial.indicator.impl.macd;

import io.mercury.financial.indicator.api.IndicatorEvent;

public interface MacdEvent extends IndicatorEvent {

	@Override
	default String eventName() {
		return "MacdEvent";
	}

}