package io.ffreedom.redstone.core.balance;

public final class Balance {

	private int accountId;
	private double availableMargin;
	private double availableCredit;

	private Balance(int accountId) {
		this.accountId = accountId;
	}

	public final static Balance create(int accountId) {
		return new Balance(accountId);
	}

	public int getAccountId() {
		return accountId;
	}

	public double getAvailableMargin() {
		return availableMargin;
	}

	public Balance setAvailableMargin(double availableMargin) {
		this.availableMargin = availableMargin;
		return this;
	}

	public double getAvailableCredit() {
		return availableCredit;
	}

	public Balance setAvailableCredit(double availableCredit) {
		this.availableCredit = availableCredit;
		return this;
	}

}
