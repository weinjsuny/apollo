package io.redstone.core.adaptor;

import java.io.Closeable;

import javax.annotation.Nonnull;

import io.mercury.common.fsm.Enable;
import io.mercury.financial.instrument.Instrument;
import io.redstone.core.account.Account;
import io.redstone.core.order.specific.ChildOrder;

public interface Adaptor extends Closeable, Enable {

	int adaptorId();

	String adaptorName();

	Account account();

	boolean startup();

	/**
	 * 订阅行情
	 * 
	 * @param subscribeMarketData
	 * @return
	 */
	boolean subscribeMarketData(@Nonnull Instrument... instruments);

	/**
	 * 发送新订单
	 * 
	 * @param order
	 * @return
	 */
	boolean newOredr(@Nonnull ChildOrder order);

	/**
	 * 发送撤单请求
	 * 
	 * @param order
	 * @return
	 */
	boolean cancelOrder(@Nonnull ChildOrder order);

	/**
	 * 查询持仓
	 * 
	 * @param account
	 * @return
	 */
	boolean queryOrder(@Nonnull Account account);

	/**
	 * 查询持仓
	 * 
	 * @param account
	 * @return
	 */
	boolean queryPositions(@Nonnull Account account);

	/**
	 * 查询余额
	 * 
	 * @param account
	 * @return
	 */
	boolean queryBalance(@Nonnull Account account);

	public static enum AdaptorStatus {

		MdEnable, MdDisable, TdEnable, TdDisable

	}

}
