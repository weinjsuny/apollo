package io.mercury.redstone.core.order;

import io.mercury.financial.instrument.Instrument;
import io.mercury.redstone.core.order.enums.OrdType;
import io.mercury.redstone.core.order.enums.TrdAction;
import io.mercury.redstone.core.order.enums.TrdDirection;
import io.mercury.redstone.core.order.structure.OrdPrice;
import io.mercury.redstone.core.order.structure.OrdQty;

public abstract class ActualOrder extends OrderBasicImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6034876220144503779L;

	/**
	 * 交易动作
	 */
	private final TrdAction action;

	/**
	 * 所属上级ordId
	 */
	private final long ownerUniqueId;

	/**
	 * 
	 * @param uniqueId
	 * @param strategyId
	 * @param accountId
	 * @param subAccountId
	 * @param instrument
	 * @param ordQty
	 * @param ordPrice
	 * @param ordType
	 * @param direction
	 * @param action
	 * @param ownerOrdId
	 */
	protected ActualOrder(long uniqueId, int strategyId, int accountId, int subAccountId, Instrument instrument,
			OrdQty ordQty, OrdPrice ordPrice, OrdType ordType, TrdDirection direction, TrdAction action,
			long ownerUniqueId) {
		super(uniqueId, strategyId, accountId, subAccountId, instrument, ordQty, ordPrice, ordType, direction);
		this.action = action;
		this.ownerUniqueId = ownerUniqueId != 0L ? ownerUniqueId : uniqueId();
	}

	@Override
	public long ownerUniqueId() {
		return ownerUniqueId;
	}

	public TrdAction action() {
		return action;
	}

}
