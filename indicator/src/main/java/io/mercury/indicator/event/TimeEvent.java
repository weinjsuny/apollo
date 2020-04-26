package io.mercury.indicator.event;

import java.time.temporal.TemporalAdjuster;

import io.mercury.indicator.api.IndicatorEvent;

public interface TimeEvent<T extends TemporalAdjuster> extends IndicatorEvent {

	@Override
	default String eventName() {
		return "TimeEvent";
	}

	void onTime(T time);

}
