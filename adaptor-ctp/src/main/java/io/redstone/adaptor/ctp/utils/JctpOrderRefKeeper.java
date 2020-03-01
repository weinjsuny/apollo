package io.redstone.adaptor.ctp.utils;

import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import org.eclipse.collections.api.map.primitive.MutableObjectLongMap;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableMaps;
import io.redstone.adaptor.ctp.exception.OrderRefNotFoundException;

/**
 * 
 * @author phoneix
 *
 *         TODO - Persistence
 */

public class JctpOrderRefKeeper {

	private MutableObjectLongMap<String> orderRefToOrdSysId = MutableMaps.newObjectLongHashMap(Capacity.L10_SIZE_1024);

	private MutableLongObjectMap<String> ordSysIdToOrderRef = MutableMaps.newLongObjectHashMap(Capacity.L10_SIZE_1024);

	private final static JctpOrderRefKeeper InnerInstance = new JctpOrderRefKeeper();

	private JctpOrderRefKeeper() {
	}

	public static void put(String orderRef, long ordSysId) {
		InnerInstance.orderRefToOrdSysId.put(orderRef, ordSysId);
		InnerInstance.ordSysIdToOrderRef.put(ordSysId, orderRef);
	}

	public static long getOrdSysId(String orderRef) throws OrderRefNotFoundException {
		long ordSysId = InnerInstance.orderRefToOrdSysId.get(orderRef);
		if (ordSysId == 0)
			throw new OrderRefNotFoundException(orderRef);
		return ordSysId;
	}

	public static String getOrderRef(long ordSysId) throws OrderRefNotFoundException {
		String orderRef = InnerInstance.ordSysIdToOrderRef.get(ordSysId);
		if (orderRef == null)
			throw new OrderRefNotFoundException(ordSysId);
		return orderRef;
	}

}
