package io.apollo.engine.position;

import io.apollo.core.position.impl.BasePositionT1;
import io.gemini.definition.order.Order;
import io.gemini.definition.order.enums.OrdStatus;
import io.gemini.definition.order.structure.OrdQty;

public class ChinaStockPosition extends BasePositionT1 {

	public ChinaStockPosition(int accountId, int instrumentId, int tradeableQty) {
		super(accountId, instrumentId, tradeableQty);
	}

	@Override
	public void updatePosition(Order order) {
		OrdStatus status = order.status();
		OrdQty qty = order.qty();
		switch (order.direction()) {
		case Long:
			switch (status) {
			case PartiallyFilled:
			case Filled:
				setCurrentQty(currentQty() + qty.filledQty() - qty.lastFilledQty());
				break;
			default:
				break;
			}
			break;
		case Short:
			switch (status) {
			case PendingNew:
				setTradeableQty(tradeableQty() - qty.offerQty());
				break;
			case Canceled:
			case NewRejected:
				setTradeableQty(tradeableQty() + qty.offerQty() - qty.lastFilledQty());
				break;
			case PartiallyFilled:
			case Filled:
				setCurrentQty(currentQty() - qty.filledQty() + qty.lastFilledQty());
				break;
			default:
				break;
			}
		default:
			break;
		}
	}

}
