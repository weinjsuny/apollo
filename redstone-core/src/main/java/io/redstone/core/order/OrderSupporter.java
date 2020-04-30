package io.redstone.core.order.utils;

import io.redstone.core.order.specific.ChildOrder;
import io.redstone.core.order.structure.OrdReport;

public final class OrderOperator {

	// TODO Call this update order
	public static void update(ChildOrder order, OrdReport report) {
		order.updateOrdStatus(report.getOrdStatus());
		switch (order.ordStatus()) {
		case PartiallyFilled:
			// Set FilledQty
			order.ordQty().filledQty(report.getFilledQty());
			// Add NewTrade record
			order.ordTradeSet().addNewTrade(report.getEpochMillis(), report.getExecutePrice(),
					report.getFilledQty() - order.ordQty().lastFilledQty());
			break;
		case Filled:
			// Set FilledQty
			order.ordQty().filledQty(report.getFilledQty());
			// Add NewTrade Record
			order.ordTradeSet().addNewTrade(report.getEpochMillis(), report.getExecutePrice(),
					report.getFilledQty() - order.ordQty().lastFilledQty());
			// Calculation AvgPrice
			order.ordPrice().calculateAvgPrice(order.ordTradeSet());
			break;
		default:
			break;
		}
	}

}