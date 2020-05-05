package io.redstone.core.order.structure;

import org.eclipse.collections.api.list.MutableList;

/**
 * [offerPrice] & [avgPrice] fix use {@link MarketConstant#PriceMultiplier}
 */
public final class OrdPrice {

	/**
	 * 委托价格
	 */
	private long offerPrice;
	/**
	 * 成交均价
	 */
	private long trdAvgPrice;
	/**
	 * 以最优价格
	 */
	private boolean isBestPrice;

	private OrdPrice(long offerPrice) {
		this.offerPrice = offerPrice;
	}

	private OrdPrice(boolean isBestPrice) {
		this.isBestPrice = isBestPrice;
	}

	public static OrdPrice withOffer(long offerPrice) {
		return new OrdPrice(offerPrice);
	}

	public static OrdPrice withBestPrice() {
		return new OrdPrice(true);
	}

	public long offerPrice() {
		return offerPrice;
	}

	public OrdPrice offerPrice(long offerPrice) {
		if (this.offerPrice == 0)
			this.offerPrice = offerPrice;
		return this;
	}

	public long trdAvgPrice() {
		return trdAvgPrice;
	}

	public boolean isBestPrice() {
		return isBestPrice;
	}

	public OrdPrice calculateAvgPrice(TrdList trdList) {
		if (!trdList.isEmpty()) {
			MutableList<TrdRecord> records = trdList.records();
			// 计算总成交金额
			long totalTurnover = records.sumOfLong(trade -> trade.trdPrice() * trade.trdQty());
			// 计算总成交量
			long totalQty = records.sumOfInt(trade -> trade.trdQty());
			if (totalQty > 0L)
				this.trdAvgPrice = totalTurnover / totalQty;
			return this;
		}
		return this;
	}

}
