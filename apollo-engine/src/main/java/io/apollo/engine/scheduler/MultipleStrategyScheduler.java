package io.apollo.engine.scheduler;

import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.MutableSet;
import org.slf4j.Logger;

import io.gemini.definition.market.data.MarketData;
import io.gemini.definition.market.instrument.Instrument;
import io.gemini.definition.strategy.Strategy;
import io.gemini.definition.strategy.StrategyScheduler;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.collections.MutableSets;
import io.mercury.common.log.CommonLoggerFactory;

public abstract class MultipleStrategyScheduler<M extends MarketData> implements StrategyScheduler<M> {

	/**
	 * Logger
	 */
	private static final Logger log = CommonLoggerFactory.getLogger(MultipleStrategyScheduler.class);

	/**
	 * 策略列表
	 */
	protected final MutableIntObjectMap<Strategy<M>> strategyMap = MutableMaps.newIntObjectHashMap();

	/**
	 * 订阅合约的策略列表 <br>
	 * instrumentId -> Set::[Strategy]
	 */
	protected final MutableIntObjectMap<MutableSet<Strategy<M>>> subscribedMap = MutableMaps.newIntObjectHashMap();

	@Override
	public void addStrategy(Strategy<M> strategy) {
		log.info("Add strategy -> strategyId==[{}], strategyName==[{}], subAccount==[{}]", strategy.strategyId(),
				strategy.strategyName(), strategy.getSubAccount());
		strategyMap.put(strategy.strategyId(), strategy);
		strategy.instruments().each(instrument -> this.subscribeInstrument(instrument, strategy));
		strategy.enable();
	}

	private void subscribeInstrument(Instrument instrument, Strategy<M> strategy) {
		subscribedMap.getIfAbsentPut(instrument.id(), MutableSets::newUnifiedSet).add(strategy);
		log.info("Add subscribe instrument, strategyId==[{}], instrumentId==[{}]", strategy.strategyId(),
				instrument.id());
	}

}
